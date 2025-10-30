# executor/Dockerfile
FROM python:3.10-bullseye

# Install Java (for code execution)
RUN apt-get update && apt-get install -y openjdk-17-jdk-headless && apt-get clean

WORKDIR /app

COPY requirements.txt /app/
RUN pip install --no-cache-dir -r requirements.txt

COPY . /app

CMD ["python", "app.py"]
