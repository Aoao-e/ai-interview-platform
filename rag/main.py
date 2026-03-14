import time

from app_config import load_config
from thread_worker import start_workers


def main() -> None:
    cfg = load_config("config.yaml")
    start_workers(cfg)
    print(f"started {cfg.worker.threads} worker threads")

    while True:
        time.sleep(10)


if __name__ == "__main__":
    main()
