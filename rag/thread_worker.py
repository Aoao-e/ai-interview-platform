import threading
import time
import traceback

from app_config import AppConfig
from rag_service import generate_qa
from redis_service import claim_task, complete_task, create_redis_client, fail_task


def worker_loop(worker_id: int, cfg: AppConfig) -> None:
    client = create_redis_client(cfg)
    while True:
        try:
            task = claim_task(client, cfg.redis.key_prefix, worker_id)
        except Exception as exc:
            print(f"[trace] worker={worker_id} claim_task error: {exc}")
            time.sleep(cfg.redis.poll_seconds)
            continue

        if not task:
            time.sleep(cfg.redis.poll_seconds)
            continue

        key, requirements = task
        try:
            qa_pairs = generate_qa(requirements=requirements, cfg=cfg)
            complete_task(client, key, qa_pairs)
            print(f"[worker-{worker_id}] done: {key}")
        except Exception as exc:
            error_text = f"{exc}\n{traceback.format_exc()}"
            fail_task(client, key, error_text)
            print(f"[worker-{worker_id}] failed: {key} -> {error_text}")


def start_workers(cfg: AppConfig) -> list[threading.Thread]:
    threads: list[threading.Thread] = []
    for index in range(cfg.worker.threads):
        thread = threading.Thread(target=worker_loop, args=(index, cfg), daemon=True)
        thread.start()
        threads.append(thread)
    return threads
