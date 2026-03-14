# AI Interview Platform

AI 面试题生成平台，根据用户输入自动生成面试题，并提供收藏、历史记录等功能。

效果预览：

http://121.196.160.85/

## 技术栈

- SpringBoot
- Python
- Redis
- MySQL
- Nginx

## 功能

- AI 自动生成面试题
- 面试题收藏
- 历史记录查看
- RAG 知识增强生成

## 快速开始

### 1. 配置后端

打开：

spring/src/main/resources/application.yml

补全：

- MySQL 数据库信息
- SMTP 邮件配置

### 2. 安装 Python 依赖

进入 `rag` 目录：

```bash
pip install -r requirements.txt
```
### 3. 导入数据库

执行：

database.sql


### 4. 启动 Nginx

进入 nginx 目录：

nginx.exe

### 5. 启动服务

依次启动：

redis
springboot
python rag/main.py

### 

如果有问题或可以优化的地方，欢迎提出建议。
联系方式
Email：aoaoo2025@163.com

###
