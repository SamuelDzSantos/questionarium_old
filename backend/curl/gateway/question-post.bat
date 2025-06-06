@echo off
REM Substitua <SEU_TOKEN_JWT> pelo token gerado no login
set TOKEN=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2ODQyMTQyNjM3MjVhNTcwNjhkYjlhMTIiLCJyb2xlIjoiVVNFUiIsImlzcyI6InF1ZXN0aW9uYXJpdW0tYXV0aCIsImV4cCI6MTc0OTI0NzQ2OCwiaWF0IjoxNzQ5MTYxMDY4LCJlbWFpbCI6Inl1a2kuYWxleGFuZHJlQGhvdG1haWwuY29tIn0.223ymGYoH07cfogjBa7EjwVgyi-hkVdOZ-eJ1iKVS0o


curl -X POST "http://localhost:14000/questions" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %TOKEN%" ^
  -d "{\"multipleChoice\":true,\"numberLines\":5,\"educationLevel\":\"ENSINO_SUPERIOR\",\"header\":\"Qual o resultado de 2+2?\",\"headerImage\":null,\"enable\":true,\"accessLevel\":\"PUBLIC\",\"tagIds\":[1,2],\"alternatives\":[{\"description\":\"3\",\"imagePath\":null,\"isCorrect\":false,\"explanation\":\"Errado porque 2+2 é 4\",\"alternativeOrder\":1},{\"description\":\"4\",\"imagePath\":null,\"isCorrect\":true,\"explanation\":\"Porque 2+2 = 4\",\"alternativeOrder\":2}]}"
pause
