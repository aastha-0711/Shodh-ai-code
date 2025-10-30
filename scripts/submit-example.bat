@echo off
REM Example: submit sample Main.java for problem 1 and poll until finished
setlocal enabledelayedexpansion
set CODE=import java.util.*; public class Main { public static void main(String[] args) { Scanner s = new Scanner(System.in); int a=s.nextInt(), b=s.nextInt(); System.out.println(a+b); } }
echo Submitting example solution...
for /f "delims=" %%i in ('curl -s -X POST http://localhost:8080/api/submissions -H "Content-Type: application/json" -d "{\"problemId\":1,\"username\":\"demo\",\"language\":\"java\",\"code\":\"%CODE%\"}"') do set RESP=%%i
echo Response: %RESP%
for /f "tokens:2 delims:,:{}" %%a in ('echo %RESP% ^| findstr /o "submissionId"') do set SUBID=%%a
REM crude extract of number
echo Waiting for result (submission id may need manual extraction): %RESP%
echo Use curl http://localhost:8080/api/submissions/<id> to poll.
