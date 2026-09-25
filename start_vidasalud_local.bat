@echo off
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot"
set "MAVEN_HOME=C:\Users\dylan\.gemini\antigravity\scratch\apache-maven-3.9.6"
set "PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%"

echo ======================================================================
echo             LEVANTANDO PLATAFORMA VIDASALUD (EP1)
echo ======================================================================

echo [1/4] Iniciando ms-vidasalud-catalog en puerto 8082...
start "Catalog (8082)" cmd /k "cd /d C:\Users\dylan\.gemini\antigravity\scratch\vidasalud\vidasalud_ep1_code\ms-vidasalud-catalog && set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot&& set PATH=C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot\bin;C:\Users\dylan\.gemini\antigravity\scratch\apache-maven-3.9.6\bin;%PATH%&& mvn spring-boot:run"

timeout /t 5 /nobreak >nul

echo [2/4] Iniciando ms-vidasalud-appointments en puerto 8081...
start "Appointments (8081)" cmd /k "cd /d C:\Users\dylan\.gemini\antigravity\scratch\vidasalud\vidasalud_ep1_code\ms-vidasalud-appointments && set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot&& set PATH=C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot\bin;C:\Users\dylan\.gemini\antigravity\scratch\apache-maven-3.9.6\bin;%PATH%&& mvn spring-boot:run"

timeout /t 5 /nobreak >nul

echo [3/4] Iniciando ms-vidasalud-bff en puerto 8080...
start "BFF (8080)" cmd /k "cd /d C:\Users\dylan\.gemini\antigravity\scratch\vidasalud\vidasalud_ep1_code\ms-vidasalud-bff && set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot&& set PATH=C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot\bin;C:\Users\dylan\.gemini\antigravity\scratch\apache-maven-3.9.6\bin;%PATH%&& mvn spring-boot:run"

timeout /t 5 /nobreak >nul

echo [4/4] Iniciando Frontend Angular en puerto 4200...
start "Frontend Angular (4200)" cmd /k "cd /d C:\Users\dylan\.gemini\antigravity\scratch\vidasalud\vidasalud_ep1_code\frontend-vidasalud && npm start"

echo.
echo Todos los servicios han sido lanzados en sus terminales independientes.
pause
