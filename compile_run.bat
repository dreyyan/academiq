@echo off
setlocal EnableDelayedExpansion

:: -----------------------------
:: PATH SETTINGS
:: -----------------------------
set "PROJECT_ROOT=%~dp0"
set "SRC_DIR=%PROJECT_ROOT%src"
set "BIN_DIR=%PROJECT_ROOT%bin"
set "MAIN_CLASS=ums.UniversityManagementSystem"

echo [COMPILING] AcademIQ: University Management System...
echo Source: %SRC_DIR%
echo Target: %BIN_DIR%
echo.

:: Clean bin
if exist "%BIN_DIR%\ums" rmdir /s /q "%BIN_DIR%"
mkdir "%BIN_DIR%"

set "COMPILED=0"
set "FAILED=0"

:: Compile all java files
for /r "%SRC_DIR%\ums" %%f in (*.java) do (
    :: Compile without verbose output
    javac -d "%BIN_DIR%" -sourcepath "%SRC_DIR%" -cp "%BIN_DIR%" "%%f" 2>nul
    if !errorlevel! EQU 0 (
        echo [OK] %%~nxf
        set /a COMPILED+=1
    ) else (
        echo [ERROR] %%~nxf
        set /a FAILED+=1
        :: Show compilation error details
        javac -d "%BIN_DIR%" -sourcepath "%SRC_DIR%" -cp "%BIN_DIR%" "%%f"
    )
)

echo.
echo ========================================
echo SUMMARY: %COMPILED% succeeded, %FAILED% failed
echo ========================================

:: Prompt user before running
if %FAILED% EQU 0 (
    if exist "%BIN_DIR%\ums\UniversityManagementSystem.class" (
        echo.
        echo [OK] Main class ready
        echo Press ENTER to run the compiled program...
        pause >nul
        :: Clear screen before running
        cls
        java -cp "%BIN_DIR%" %MAIN_CLASS%
    ) else (
        echo [ERROR] Main class missing!
    )
) else (
    echo [ERROR] Fix compilation errors above.
)

pause
endlocal