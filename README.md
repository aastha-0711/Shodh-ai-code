# Shodh-a-Code (Prototype)

This repository contains a prototype coding contest platform with a Spring Boot backend and a Next.js frontend. Submissions are executed inside a small Docker-based judge container.

Quick overview

- backend: Spring Boot app (Java 21) exposing REST endpoints under `/api`
- frontend: Next.js app (React + Tailwind) with join/contest UI
- judge: small Docker image used to compile and run Java submissions
- db: Postgres (configured in docker-compose)

Run the full stack (Docker Desktop required)

1. From the repository root:

```bash
docker-compose up --build
```

This builds the judge image, the backend (multi-stage maven build), and the frontend.

Helper scripts

- `scripts\run-frontend-local.bat` — installs and runs the frontend locally (Windows).
- `scripts\submit-example.bat` — attempts to POST a sample Java submission to the backend (Windows). You may need to extract the returned submissionId and poll manually.

Host-backend workflow (Windows-friendly)

If you prefer running the backend on your host (recommended on Windows because of Docker socket differences), use the helper to start only the database and judge containers:

```cmd
scripts\start-db-judge.bat
```

Then from a separate terminal build and run the backend locally:

```cmd
cd backend
mvnw.cmd -DskipTests package
java -jar target\demo-0.0.1-SNAPSHOT.jar
```

Finally run the frontend locally:

```cmd
scripts\run-frontend-local.bat
```

This flow avoids mounting the Docker socket into the backend container and works well on Windows.

Usage

- Open http://localhost:3000 and join the sample contest (use Contest ID `1` and any username).
- Submit a Java program that defines `public class Main { public static void main(String[] args){...} }`.

API

- GET /api/contests/{contestId}
- GET /api/contests/{contestId}/leaderboard
- POST /api/submissions { problemId, username, code, language }
- GET /api/submissions/{submissionId}

Security & notes

- This is a demo prototype. Running untrusted code in Docker has risks. For production, use stricter isolation (gVisor, Firecracker), resource limits, and input/output sanitization.
- The compose setup mounts the host Docker socket into the `backend` container so the backend can `docker run` submissions when it itself runs inside a container. This is convenient for local demos but is insecure on shared hosts because it gives the container full control over the Docker daemon. Only use for local testing.
- Windows caveat: Docker Desktop on Windows uses a different backend (named pipes). Mounting `/var/run/docker.sock` works as expected on Linux/macOS. On Windows, if you run into issues controlling Docker from the backend container, run the backend on the host (see section "Run backend locally") or configure Docker Desktop to expose the daemon via a TCP socket (not recommended for general use).
