@echo off
curl -X POST "http://localhost:14000/auth/login" ^
  -H "Content-Type: application/json" ^
  -d "{\"login\":\"user1@quest.com\",\"password\":\"senha123\"}"
pause