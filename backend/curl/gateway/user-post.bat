@echo off
curl -X POST "http://localhost:14000/users" ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"ALE\",\"email\":\"yuki.alexandre@hotmail.com\",\"password\":\"senha123\",\"roles\":[\"USER\"]}"
pause
