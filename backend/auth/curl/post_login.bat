@echo off
curl -X POST http://localhost:15000/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"login\":\"usuario1\",\"password\":\"senha123\"}"
pause
