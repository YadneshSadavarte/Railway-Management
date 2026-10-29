@echo off
cd /d "%~dp0"
echo ========================================================
echo Launching RailRoute - Enterprise Railway Portal
echo ========================================================
java --enable-native-access=ALL-UNNAMED -cp "build/output/java;lib/sqlite-jdbc.jar" Main
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ========================================================
    echo Application exited with error code %ERRORLEVEL%
    echo ========================================================
    pause
)
