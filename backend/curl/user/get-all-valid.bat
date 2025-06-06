@echo off
curl -X GET "http://localhost:14002/users" ^
  -H "Content-Type: application/json"
pause
