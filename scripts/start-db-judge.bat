@echo off
echo Starting Postgres and judge containers (detached)...
docker-compose -f docker-compose.host-backend.yml up --build -d
if errorlevel 1 (
  echo Failed to start containers. Check Docker Desktop and try again.
  exit /b 1
)
echo Containers started.
echo Next steps:
echo 1) Build and run backend locally:
echo    cd backend
echo    mvnw.cmd -DskipTests package
echo    java -jar target\demo-0.0.1-SNAPSHOT.jar
echo 2) Run the frontend locally:
echo    scripts\run-frontend-local.bat
