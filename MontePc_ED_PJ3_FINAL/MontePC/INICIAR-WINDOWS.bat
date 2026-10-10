@echo off
cd /d "%~dp0"
if not exist "dist\MontePC.jar" (
 echo Abra o projeto no NetBeans e use Limpar e Construir antes de executar este arquivo.
 pause
 exit /b 1
)
java -jar "dist\MontePC.jar"
if errorlevel 1 pause
