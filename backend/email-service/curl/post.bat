@echo off
curl -X POST http://localhost:14002/users ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"ALEXANDRE ALE\",\"email\":\"yuki.alexandre@hotmail.com\",\"password\":\"admin\",\"roles\":[\"USER\"]}"
pause
