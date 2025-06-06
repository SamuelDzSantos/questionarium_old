@echo off
curl -X POST http://localhost:15000/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"login\":\"usuario1\",\"password\":\"senha123\",\"role\":\"ROLE_USER\"}"
pause
