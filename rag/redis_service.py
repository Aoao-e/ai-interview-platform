import json
import threading
from typing import Optional, Tuple

import redis

from app_config import AppConfig
from rag_service import Requirements


def create_redis_client(cfg: AppConfig) -> redis.Redis:
    return redis.Redis(
        host=cfg.redis.host,
        port=cfg.redis.port,
        db=cfg.redis.db,
        password=cfg.redis.password,
        decode_responses=True,
    )


def _parse_requirements_from_hash(data: dict[str, str]) -> Requirements:
    job = (data.get("job") or "").strip()
    tone = (data.get("tone") or "").strip()
    extra_info = (data.get("extrainfo") or "").strip()
    count_raw = (data.get("count") or "").strip()

    if not job:
        raise ValueError("job is empty")
    if not tone:
        raise ValueError("tone is empty")
    if not count_raw:
        raise ValueError("count is empty")

    try:
        count = int(count_raw)
    except ValueError as exc:
        raise ValueError(f"count is invalid: {count_raw!r}") from exc

    return Requirements(job=job, tone=tone, count=count, extra_info=extra_info)


def claim_task(client: redis.Redis, key_prefix: str, worker_id: int) -> Optional[Tuple[str, Requirements]]:
    thread = threading.current_thread()
    saw_key = False

    for key in client.scan_iter(f"{key_prefix}*"):
        saw_key = True
        with client.pipeline() as pipe:
            try:
                pipe.watch(key)
                data = pipe.hgetall(key)
                is_deal = (data.get("is_deal") or "").strip().lower()
                if is_deal in {"true", "processing"}:
                    pipe.unwatch()
                    continue

                try:
                    requirements = _parse_requirements_from_hash(data)
                except ValueError as exc:
                    pipe.multi()
                    pipe.hset(key, mapping={"is_deal": "failed", "error": str(exc)})
                    pipe.execute()
                    print(
                        f"[trace] worker={worker_id} thread={thread.name}({thread.ident}) "
                        f"invalid requirements key={key}: {exc}"
                    )
                    continue

                print(
                    f"[trace] worker={worker_id} thread={thread.name}({thread.ident}) read key={key} "
                    f"job={requirements.job!r} tone={requirements.tone!r} "
                    f"count={requirements.count!r} extrainfo={requirements.extra_info!r}"
                )

                pipe.multi()
                pipe.hset(key, "is_deal", "processing")
                pipe.execute()
                print(
                    f"[trace] worker={worker_id} thread={thread.name}({thread.ident}) claimed key={key}"
                )
                return key, requirements
            except redis.WatchError:
                continue

    if not saw_key:
        print(
            f"[trace] worker={worker_id} thread={thread.name}({thread.ident}) no key matched pattern={key_prefix}*"
        )
    return None


def format_qa_for_redis(qa_pairs: list[tuple[str, str]]) -> str:
    # Keep existing wire format: {{question,answer}{question,answer}}
    if not qa_pairs:
        return "{{}}"

    parts = []
    for question, answer in qa_pairs:
        q = question.replace("{", "(").replace("}", ")")
        a = answer.replace("{", "(").replace("}", ")")
        parts.append(f"{{{q} {a}}}")
    return "{" + "".join(parts) + "}"


def complete_task(client: redis.Redis, key: str, qa_pairs: list[tuple[str, str]]) -> None:
    qa_text = format_qa_for_redis(qa_pairs)
    client.hset(key, mapping={"question_answer": qa_text, "is_deal": "true", "error": ""})


def fail_task(client: redis.Redis, key: str, error: str) -> None:
    client.hset(key, mapping={"is_deal": "failed", "error": error[:1000]})
