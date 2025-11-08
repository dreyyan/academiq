@echo off
setlocal

:: Set project root
set "PROJECT_ROOT=%~dp0"
set "SRC_DIR=%PROJECT_ROOT%src"
set "BIN_DIR=%PROJECT_ROOT%bin"

echo ========================================
echo    COMPILING UNIVERSITY MANAGEMENT SYSTEM
echo ========================================

:: Create bin directory if it doesn't exist
if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"

:: Compile all .java files recursively
javac -d "%BIN_DIR%" -sourcepath "%SRC_DIR%" "%SRC_DIR%\ums\UniversityManagementSystem.java"

:: Check if compilation was successful
if %errorlevel% == 0 (
    echo.
    echo ========================================
    echo    COMPILATION SUCCESSFUL!
    echo    Classes output to: %BIN_DIR%
    echo ========================================
) else (
    echo.
    echo XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    echo    COMPILATION FAILED!
    echo    Check the errors above.
    echo XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    exit /b %errorlevel%
)

endlocal