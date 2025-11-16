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

:: Create bin folder if missing
if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"

set "COMPILED=0"
set "SKIPPED=0"
set "FAILED=0"

:: --- Compile critical dependencies first ---
set "CRITICAL_FILES=Logger.java Settings.java ConsoleUI.java"
for %%f in (%CRITICAL_FILES%) do (
    if exist "%SRC_DIR%\ums\util\%%f" (
        javac -d "%BIN_DIR%" -sourcepath "%SRC_DIR%" -cp "%BIN_DIR%" "%SRC_DIR%\ums\util\%%f"
        if !errorlevel! EQU 0 (
            echo [OK] %%f
            set /a COMPILED+=1
        ) else (
            echo [ERROR] %%f
            set /a FAILED+=1
        )
    )
)

:: --- Compile the rest of java files only if newer ---
for /r "%SRC_DIR%\ums" %%f in (*.java) do (
    set "SRC_FILE=%%f"
    set "REL_PATH=%%f:%SRC_DIR%\=%%"
    set "CLASS_FILE=%BIN_DIR%\!REL_PATH:.java=.class!"

    :: Skip critical files already compiled
    for %%c in (%CRITICAL_FILES%) do (
        if "%%~nxf"=="%%c" set "COMPILE_FILE="
    )

    :: Check if class file doesn't exist or java file is newer
    if not exist "!CLASS_FILE!" set "COMPILE_FILE=1"
    if exist "!CLASS_FILE!" for %%A in ("!SRC_FILE!") do for %%B in ("!CLASS_FILE!") do (
        if %%~tA GTR %%~tB set "COMPILE_FILE=1"
    )

    if defined COMPILE_FILE (
        javac -d "%BIN_DIR%" -sourcepath "%SRC_DIR%" -cp "%BIN_DIR%" "%%f"
        if !errorlevel! EQU 0 (
            echo [OK] %%~nxf
            set /a COMPILED+=1
        ) else (
            echo [ERROR] %%~nxf
            set /a FAILED+=1
        )
        set "COMPILE_FILE="
    ) else (
        echo [SKIP] %%~nxf
        set /a SKIPPED+=1
    )
)

echo.
echo ========================================
echo SUMMARY: %COMPILED% compiled, %SKIPPED% skipped, %FAILED% failed
echo ========================================

:: Prompt user before running
if %FAILED% EQU 0 (
    if exist "%BIN_DIR%\ums\UniversityManagementSystem.class" (
        cls
        java -cp "%BIN_DIR%" %MAIN_CLASS%
    ) else (
        echo [ERROR] Main class missing!
    )
) else (
    echo [ERROR] Fix compilation errors above.
)
echo.
echo Press any key to exit...
pause >nul
endlocal
