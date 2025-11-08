@echo off
setlocal

:: Set paths
set "PROJECT_ROOT=%~dp0"
set "BIN_DIR=%PROJECT_ROOT%bin"
set "DATA_DIR=%PROJECT_ROOT%data"

echo ========================================
echo    RUNNING UNIVERSITY MANAGEMENT SYSTEM
echo ========================================

:: Check if bin directory exists
if not exist "%BIN_DIR%" (
    echo.
    echo [ERROR] bin directory not found!
    echo Run compile.bat first.
    pause
    exit /b 1
)

:: Check for main class
if not exist "%BIN_DIR%\ums\UniversityManagementSystem.class" (
    echo.
    echo [ERROR] Main class not found in bin!
    echo Make sure compilation was successful.
    pause
    exit /b 1
)

:: Launch in a new external CMD window
start cmd /k "echo Running University Management System... && java -cp \"%BIN_DIR%\" ums.UniversityManagementSystem && echo. && echo Program finished. && pause"
