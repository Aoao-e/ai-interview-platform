import json
import os
import urllib.error
import urllib.request
from dataclasses import dataclass
from typing import List, Tuple

from app_config import AppConfig


PROMPT_TEMPLATE = """你是一名资深面试官，请根据岗位需求生成面试题和参考答案。
严格输出 JSON 数组，格式如下：
[
  {{"question": "...", "answer": "..."}}
]
注意：在你输出的问题前加上‘question’，在你输出的answer前加上‘answer’
不要输出任何额外文本。

岗位信息：
- 职位: {job}
- 语气: {tone}
- 题目数量: {count}
- 额外信息: {extra_info}
例如：

[
- 职位: Java后端开发
- 语气: 专业的
- 题目数量: 2
- 额外信息: null
   [
       "question":"请简述 Java 线程池的核心参数及其工作流程。",
       "answer":"核心参数包括 corePoolSize、maximumPoolSize、keepAliveTime、unit、workQueue、threadFactory 及 handler。工作流程为：任务提交后，若核心线程未满则创建核心线程执行；若已满则放入队列；若队列满则创建非核心线程；若达到最大线程数则执行拒绝策略。"
   ]
   [
        "question":"MySQL 索引失效的常见场景有哪些？",
        "answer":"常见场景包括：模糊查询以%开头、使用函数或运算、类型隐式转换、违背最左前缀原则、OR 连接条件中有无索引列、IS NULL 或 IS NOT NULL 优化器判断全表扫描更优等。"
   ]
]
"""


@dataclass(frozen=True)
class Requirements:
    job: str
    tone: str
    count: int
    extra_info: str

    def to_prompt_text(self) -> str:
        return (
            f"职位: {self.job}\n"
            f"语气: {self.tone}\n"
            f"题目数量: {self.count}\n"
            f"额外信息: {self.extra_info}"
        )


def _extract_json_array(text: str) -> list:
    body = text.strip()
    try:
        data = json.loads(body)
        return data if isinstance(data, list) else []
    except json.JSONDecodeError:
        start = body.find("[")
        end = body.rfind("]")
        if start == -1 or end == -1 or end <= start:
            return []
        try:
            data = json.loads(body[start : end + 1])
            return data if isinstance(data, list) else []
        except json.JSONDecodeError:
            return []


def _invoke_dashscope_compatible(prompt: str, model: str, api_key: str) -> str:
    endpoint = os.getenv(
        "DASHSCOPE_COMPAT_ENDPOINT",
        "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions",
    )

    payload = {
        "model": model,
        "messages": [{"role": "user", "content": prompt}],
        "temperature": 0.7,
    }
    data = json.dumps(payload).encode("utf-8")

    req = urllib.request.Request(
        endpoint,
        data=data,
        headers={
            "Content-Type": "application/json",
            "Authorization": f"Bearer {api_key}",
        },
        method="POST",
    )

    try:
        with urllib.request.urlopen(req, timeout=90) as resp:
            body = resp.read().decode("utf-8", errors="replace")
    except urllib.error.HTTPError as exc:
        detail = exc.read().decode("utf-8", errors="replace")
        raise RuntimeError(
            f"dashscope http error status={exc.code}, endpoint={endpoint}, body={detail}"
        ) from exc
    except urllib.error.URLError as exc:
        raise RuntimeError(f"dashscope url error endpoint={endpoint}: {exc}") from exc

    try:
        result = json.loads(body)
    except json.JSONDecodeError as exc:
        raise RuntimeError(f"dashscope invalid json response: {body[:500]}") from exc

    choices = result.get("choices") or []
    if not choices:
        raise RuntimeError(f"dashscope empty choices: {result}")

    message = (choices[0] or {}).get("message") or {}
    content = message.get("content")
    if not isinstance(content, str):
        raise RuntimeError(f"dashscope invalid message content: {result}")
    return content


def generate_qa(requirements: Requirements, cfg: AppConfig) -> List[Tuple[str, str]]:
    api_key = (cfg.model.api_key or "").strip()
    if not api_key:
        raise RuntimeError("Missing api_key in config")

    prompt = PROMPT_TEMPLATE.format(
        job=requirements.job,
        tone=requirements.tone,
        count=requirements.count,
        extra_info=requirements.extra_info,
    )
    raw_text = _invoke_dashscope_compatible(prompt=prompt, model=cfg.model.chat_model, api_key=api_key)

    qa_raw = _extract_json_array(raw_text)
    qa_pairs: List[Tuple[str, str]] = []
    for item in qa_raw:
        if not isinstance(item, dict):
            continue
        question = str(item.get("question", "")).strip()
        answer = str(item.get("answer", "")).strip()
        if question and answer:
            qa_pairs.append(("question:"+question, "answer:"+answer))
            print("rag-answer:","question:"+question, "answer:"+answer)
    return qa_pairs
