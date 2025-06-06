@echo off
curl -X POST "http://localhost:14000/users" ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"ALEXANDRE\",\"email\":\"yuki.alexandre@hotmail.com\",\"password\":\"senha123\",\"roles\":[\"USER\"]}"
pause
