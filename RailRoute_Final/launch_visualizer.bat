@echo off
cd /d "%~dp0"
set PATH=C:\msys64\ucrt64\bin;%~dp0build\output;%PATH%
echo ========================================================
echo Launching RailRoute 2D OpenGL Visualizer
echo ========================================================
build\output\railroute_visualizer.exe data\active_route.txt
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ========================================================
    echo Visualizer exited with error code %ERRORLEVEL%
    echo ========================================================
    pause
)
