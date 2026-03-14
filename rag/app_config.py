from dataclasses import dataclass
from pathlib import Path
import yaml


@dataclass(frozen=True)
class RedisConfig:
    host: str
    port: int
    db: int
    password: str | None
    key_prefix: str
    poll_seconds: float


@dataclass(frozen=True)
class WorkerConfig:
    threads: int


@dataclass(frozen=True)
class ModelConfig:
    api_key: str
    chat_model: str
    embedding_model: str


@dataclass(frozen=True)
class RagConfig:
    chunk_size: int
    chunk_overlap: int
    top_k: int


@dataclass(frozen=True)
class AppConfig:
    redis: RedisConfig
    worker: WorkerConfig
    model: ModelConfig
    rag: RagConfig


def load_config(path: str = "config.yaml") -> AppConfig:
    config_path = Path(path)
    if not config_path.exists():
        raise FileNotFoundError(f"Config file not found: {config_path.resolve()}")

    raw = yaml.safe_load(config_path.read_text(encoding="utf-8")) or {}

    redis_cfg = raw.get("redis", {})
    worker_cfg = raw.get("worker", {})
    model_cfg = raw.get("model", {})
    rag_cfg = raw.get("rag", {})

    return AppConfig(
        redis=RedisConfig(
            host=str(redis_cfg.get("host", "127.0.0.1")),
            port=int(redis_cfg.get("port", 6379)),
            db=int(redis_cfg.get("db", 0)),
            password=redis_cfg.get("password"),
            key_prefix=str(redis_cfg.get("key_prefix", "service:")),
            poll_seconds=float(redis_cfg.get("poll_seconds", 1.0)),
        ),
        worker=WorkerConfig(threads=int(worker_cfg.get("threads", 4))),
        model=ModelConfig(
            api_key=str(model_cfg.get("api_key", "")),
            chat_model=str(model_cfg.get("chat_model", "qwen-3-max")),
            embedding_model=str(model_cfg.get("embedding_model", "text-embedding-v4")),
        ),
        rag=RagConfig(
            chunk_size=int(rag_cfg.get("chunk_size", 500)),
            chunk_overlap=int(rag_cfg.get("chunk_overlap", 80)),
            top_k=int(rag_cfg.get("top_k", 4)),
        ),
    )
