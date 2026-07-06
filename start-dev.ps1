# Script para arrancar los servicios de desarrollo
# Uso: .\start-dev.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  AyG Presentaciones IA - Dev Startup  " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que exista Java
$javaHome = "C:\JAVA\openjdk-24"
if (-not (Test-Path $javaHome)) {
    Write-Host "[ERROR] No se encuentra Java en $javaHome" -ForegroundColor Red
    Write-Host "Por favor, actualiza la ruta en este script." -ForegroundColor Yellow
    exit 1
}

# Verificar que exista OpenAI API Key
if (-not $env:OPENAI_API_KEY) {
    Write-Host "[ADVERTENCIA] OPENAI_API_KEY no está configurada" -ForegroundColor Yellow
    Write-Host "El backend arrancará pero fallará al procesar contenido." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Configúrala con: " -ForegroundColor Yellow
    Write-Host '  $env:OPENAI_API_KEY = "tu-api-key-aqui"' -ForegroundColor White
    Write-Host ""
    $continue = Read-Host "¿Continuar de todos modos? (s/n)"
    if ($continue -ne "s" -and $continue -ne "S") {
        exit 0
    }
}

Write-Host "[1/3] Iniciando Backend (Spring Boot)..." -ForegroundColor Green
$backendJob = Start-Job -ScriptBlock {
    param($javaHome)
    $env:JAVA_HOME = $javaHome
    $env:Path = "$javaHome\bin;$env:Path"
    Set-Location "C:\workspace-test-ia\aygPresentacionesIA\backend"
    mvn spring-boot:run
} -ArgumentList $javaHome

Write-Host "      Backend iniciando en segundo plano (Job ID: $($backendJob.Id))" -ForegroundColor Gray
Start-Sleep -Seconds 3

Write-Host "[2/3] Iniciando Frontend (Vite)..." -ForegroundColor Green
$frontendJob = Start-Job -ScriptBlock {
    Set-Location "C:\workspace-test-ia\aygPresentacionesIA\frontend"
    npm run dev
}

Write-Host "      Frontend iniciando en segundo plano (Job ID: $($frontendJob.Id))" -ForegroundColor Gray
Start-Sleep -Seconds 2

Write-Host "[3/3] Esperando servicios..." -ForegroundColor Green
Write-Host ""

# Esperar a que los servicios estén listos
$timeout = 60
$elapsed = 0
$backendReady = $false
$frontendReady = $false

while ($elapsed -lt $timeout -and (-not $backendReady -or -not $frontendReady)) {
    Start-Sleep -Seconds 2
    $elapsed += 2
    
    if (-not $backendReady) {
        try {
            $response = Invoke-WebRequest -Uri "http://localhost:8080/api/projects" -Method GET -TimeoutSec 1 -ErrorAction SilentlyContinue
            $backendReady = $true
            Write-Host "[OK] Backend listo en http://localhost:8080" -ForegroundColor Green
        } catch {
            # Sigue esperando
        }
    }
    
    if (-not $frontendReady) {
        try {
            $response = Invoke-WebRequest -Uri "http://localhost:5174" -Method GET -TimeoutSec 1 -ErrorAction SilentlyContinue
            $frontendReady = $true
            Write-Host "[OK] Frontend listo en http://localhost:5174" -ForegroundColor Green
        } catch {
            # Sigue esperando
        }
    }
    
    if (-not $backendReady -or -not $frontendReady) {
        Write-Host "." -NoNewline -ForegroundColor Gray
    }
}

Write-Host ""
Write-Host ""

if ($backendReady -and $frontendReady) {
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "  ✓ Servicios listos                    " -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Backend:  http://localhost:8080" -ForegroundColor Cyan
    Write-Host "Frontend: http://localhost:5174" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Presiona Ctrl+C para detener los servicios" -ForegroundColor Yellow
    Write-Host ""
    
    # Mostrar logs en tiempo real
    Write-Host "=== Logs (Ctrl+C para salir) ===" -ForegroundColor Cyan
    try {
        while ($true) {
            $backendOutput = Receive-Job -Job $backendJob -Keep | Select-Object -Last 5
            $frontendOutput = Receive-Job -Job $frontendJob -Keep | Select-Object -Last 5
            
            if ($backendOutput) {
                Write-Host "[Backend]" -ForegroundColor Magenta
                $backendOutput | ForEach-Object { Write-Host "  $_" }
            }
            
            if ($frontendOutput) {
                Write-Host "[Frontend]" -ForegroundColor Blue
                $frontendOutput | ForEach-Object { Write-Host "  $_" }
            }
            
            Start-Sleep -Seconds 3
        }
    } finally {
        Write-Host ""
        Write-Host "Deteniendo servicios..." -ForegroundColor Yellow
        Stop-Job -Job $backendJob, $frontendJob
        Remove-Job -Job $backendJob, $frontendJob -Force
        Write-Host "Servicios detenidos." -ForegroundColor Green
    }
} else {
    Write-Host "[ERROR] Los servicios no arrancaron en el tiempo esperado" -ForegroundColor Red
    Write-Host ""
    Write-Host "Revisa los logs de los jobs:" -ForegroundColor Yellow
    Write-Host "  Receive-Job -Job $($backendJob.Id)" -ForegroundColor White
    Write-Host "  Receive-Job -Job $($frontendJob.Id)" -ForegroundColor White
    Write-Host ""
    Write-Host "Limpia los jobs con:" -ForegroundColor Yellow
    Write-Host "  Stop-Job -Job $($backendJob.Id), $($frontendJob.Id)" -ForegroundColor White
    Write-Host "  Remove-Job -Job $($backendJob.Id), $($frontendJob.Id) -Force" -ForegroundColor White
}
