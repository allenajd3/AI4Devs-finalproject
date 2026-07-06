@echo off
REM Script simple para arrancar los servicios
REM Uso: start-dev.cmd

echo ========================================
echo   AyG Presentaciones IA - Dev Startup
echo ========================================
echo.

REM Verificar OpenAI API Key
if "%OPENAI_API_KEY%"=="" (
    echo [ADVERTENCIA] OPENAI_API_KEY no esta configurada
    echo El backend arrancara pero fallara al procesar contenido.
    echo.
    echo Configurala con:
    echo   set OPENAI_API_KEY=tu-api-key-aqui
    echo.
    pause
)

echo [1/2] Iniciando Backend (Spring Boot)...
start "Backend - Spring Boot" cmd /k "cd backend && set JAVA_HOME=C:\JAVA\openjdk-24 && set Path=C:\JAVA\openjdk-24\bin;%Path% && mvn spring-boot:run"

timeout /t 5 /nobreak >nul

echo [2/2] Iniciando Frontend (Vite)...
start "Frontend - Vite" cmd /k "cd frontend && npm run dev"

echo.
echo ========================================
echo   Servicios iniciados
echo ========================================
echo.
echo Backend:  http://localhost:8080
echo Frontend: http://localhost:5174
echo.
echo Cierra las ventanas de terminal para detener los servicios
echo.
pause
