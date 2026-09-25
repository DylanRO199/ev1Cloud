@echo off
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot"
set "MAVEN_HOME=C:\Users\dylan\.gemini\antigravity\scratch\apache-maven-3.9.6"
set "PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%"

echo ========================================================
echo   Compilando y verificando todos los microservicios
echo ========================================================

echo.
echo [1/3] Compilando ms-vidasalud-catalog...
cd /d "C:\Users\dylan\.gemini\antigravity\scratch\vidasalud\vidasalud_ep1_code\ms-vidasalud-catalog"
call mvn clean test -DskipTests=false
if %ERRORLEVEL% NEQ 0 (
    echo Error al compilar ms-vidasalud-catalog
    exit /b %ERRORLEVEL%
)

echo.
echo [2/3] Compilando ms-vidasalud-appointments...
cd /d "C:\Users\dylan\.gemini\antigravity\scratch\vidasalud\vidasalud_ep1_code\ms-vidasalud-appointments"
call mvn clean test -DskipTests=false
if %ERRORLEVEL% NEQ 0 (
    echo Error al compilar ms-vidasalud-appointments
    exit /b %ERRORLEVEL%
)

echo.
echo [3/3] Compilando ms-vidasalud-bff...
cd /d "C:\Users\dylan\.gemini\antigravity\scratch\vidasalud\vidasalud_ep1_code\ms-vidasalud-bff"
call mvn clean test -DskipTests=false
if %ERRORLEVEL% NEQ 0 (
    echo Error al compilar ms-vidasalud-bff
    exit /b %ERRORLEVEL%
)

echo.
echo ========================================================
echo   TODOS LOS MICROSERVICIOS COMPILAN Y PASAN SUS TESTS!
echo ========================================================
