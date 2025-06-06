@echo off
curl -X POST "http://localhost:14000/auth/login" ^
-H "Content-Type: application/json" ^
-d "{\"login\":\"yuki.alexandre@hotmail.com\",\"password\":\"senha123\"}"