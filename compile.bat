@echo off
setlocal

set "PROJECT_ROOT=%~dp0"
set "SRC_DIR=%PROJECT_ROOT%src"
set "BIN_DIR=%PROJECT_ROOT%bin"

echo [COMPILING] AcademIQ: University Management System...

:: Create bin if it doesn't exist
if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"

:: Compile all java files recursively
for /r "%SRC_DIR%" %%f in (*.java) do (
    javac -d "%BIN_DIR%" -sourcepath "%SRC_DIR%" "%%f"
)

:: Check compilation
if %errorlevel% == 0 (
    echo [SUCCESS] Build completed.
) else (
    echo [ERROR] Build failed.
    pause
    exit /b %errorlevel%
)

endlocal
