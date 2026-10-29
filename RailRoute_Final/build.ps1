param(
    [switch]$Run
)

$ErrorActionPreference = "Stop"

$workspaceDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $workspaceDir

$msysPaths = @(
    "C:\msys64\mingw64\bin",
    "C:\msys64\ucrt64\bin",
    "C:\msys64\clang64\bin",
    "E:\Msys\mingw64\bin"
)
$resolvedToolchainDir = $null
foreach ($p in $msysPaths) {
    if (Test-Path (Join-Path $p "g++.exe")) {
        $resolvedToolchainDir = $p
        $env:PATH = "$p;$env:PATH"
        break
    }
}

$javaOutputDir = "build/output/java"
if (-not (Test-Path $javaOutputDir)) {
    New-Item -ItemType Directory -Force -Path $javaOutputDir | Out-Null
}

$cppOutputDir = "build/output"
if (-not (Test-Path $cppOutputDir)) {
    New-Item -ItemType Directory -Force -Path $cppOutputDir | Out-Null
}

$allSourceFiles = Get-ChildItem -Path "src" -Include "*.java", "*.cpp", "*.h" -Recurse | Select-Object -ExpandProperty FullName

$commentRegex = "(//|/\*|\*/)"
$violations = @()
foreach ($file in $allSourceFiles) {
    $matches = Select-String -Path $file -Pattern $commentRegex
    if ($matches) {
        $violations += $file
    }
}

$asmFiles = Get-ChildItem -Path "src" -Filter "*.asm" -Recurse | Select-Object -ExpandProperty FullName
foreach ($afile in $asmFiles) {
    $amatches = Select-String -Path $afile -Pattern ";"
    if ($amatches) {
        $violations += $afile
    }
}

$hashPattern = "^[\s]*" + [char]35
$ps1Files = Get-ChildItem -Path "." -Filter "*.ps1" | Select-Object -ExpandProperty FullName
foreach ($pfile in $ps1Files) {
    $pmatches = Select-String -Path $pfile -Pattern $hashPattern
    if ($pmatches) {
        $violations += $pfile
    }
}

if ($violations.Count -gt 0) {
    Write-Error "STRICT RULE VIOLATION: Comments found in the following files:`n$($violations -join "`n")"
    exit 1
}

$totalChecked = $allSourceFiles.Count + $asmFiles.Count + $ps1Files.Count
Write-Host "Zero comments verified across $totalChecked source files (Java, C++, Assembly, PowerShell)." -ForegroundColor Green

$javaFiles = Get-ChildItem -Path "src" -Filter "*.java" -Recurse | Select-Object -ExpandProperty FullName
Write-Host "Compiling $($javaFiles.Count) Java files into $javaOutputDir..." -ForegroundColor Cyan

$classpath = "lib/sqlite-jdbc.jar"
$javacCmd = @(
    "javac",
    "-d", $javaOutputDir,
    "-cp", $classpath
) + $javaFiles

& $javacCmd[0] $javacCmd[1..($javacCmd.Length - 1)]

if ($LASTEXITCODE -ne 0) {
    Write-Error "Java compilation failed with exit code $LASTEXITCODE."
    exit $LASTEXITCODE
}
Write-Host "Java compilation successful: Build output in $javaOutputDir" -ForegroundColor Green

Write-Host "Compiling C++ OpenGL 2D Route Visualizer..." -ForegroundColor Cyan

$cppFiles = @(
    "src/graphics/glutMain.cpp",
    "src/graphics/MapRenderer.cpp",
    "src/graphics/ClippingUtil.cpp"
)
$exeTarget = "build/output/railroute_visualizer.exe"
if (Test-Path $exeTarget) {
    Remove-Item $exeTarget -Force -ErrorAction SilentlyContinue
    if (Test-Path $exeTarget) {
        $tempOld = "railroute_visualizer_" + [Guid]::NewGuid().ToString("N").Substring(0,8) + ".old"
        Rename-Item -Path $exeTarget -NewName $tempOld -Force -ErrorAction SilentlyContinue
    }
}

$gppBinary = if ($resolvedToolchainDir) { (Join-Path $resolvedToolchainDir "g++.exe") } else { "g++" }
$gppCmd = @(
    $gppBinary,
    "-O2"
) + $cppFiles + @(
    "-o", $exeTarget,
    "-static-libgcc",
    "-static-libstdc++",
    "-lfreeglut",
    "-lopengl32",
    "-lglu32"
)

& $gppCmd[0] $gppCmd[1..($gppCmd.Length - 1)]

if ($LASTEXITCODE -ne 0) {
    Write-Error "C++ OpenGL compilation failed with exit code $LASTEXITCODE."
    exit $LASTEXITCODE
}
Write-Host "C++ compilation successful: Executable generated at $exeTarget" -ForegroundColor Green

$requiredDlls = @("libfreeglut.dll", "libwinpthread-1.dll")
foreach ($dllName in $requiredDlls) {
    $destDll = "build/output/$dllName"
    if (-not (Test-Path $destDll)) {
        $candidateDirs = @($resolvedToolchainDir, "C:\msys64\mingw64\bin", "C:\msys64\ucrt64\bin")
        foreach ($cdir in $candidateDirs) {
            if ($cdir -and (Test-Path (Join-Path $cdir $dllName))) {
                Copy-Item -Path (Join-Path $cdir $dllName) -Destination $destDll -Force -ErrorAction SilentlyContinue
                break
            }
        }
    }
    if (-not (Test-Path $destDll)) {
        Write-Error "Required runtime DLL '$dllName' not found in toolchain directories."
        exit 1
    }
}

$objdumpCmd = Get-Command objdump -ErrorAction SilentlyContinue
if ($objdumpCmd) {
    Write-Host "Post-build dependency list for $($exeTarget):" -ForegroundColor Cyan
    & objdump -p $exeTarget | Select-String "DLL Name" | ForEach-Object { Write-Host "  $_" -ForegroundColor Gray }
}

Write-Host "Checking 64-bit Assembly toolchain..." -ForegroundColor Cyan
$nasmCmd = Get-Command nasm -ErrorAction SilentlyContinue
if ($nasmCmd) {
    Write-Host "Assembling 64-bit Win64 route distance program with NASM..." -ForegroundColor Cyan
    $asmSrc = "src/COA/assembly/distance_calc.asm"
    $objTarget = "build/output/distance_calc.obj"
    $asmExeTarget = "build/output/distance_calc.exe"

    & nasm -f win64 $asmSrc -o $objTarget
    if ($LASTEXITCODE -eq 0) {
        & gcc -static-libgcc $objTarget -o $asmExeTarget
        if ($LASTEXITCODE -eq 0) {
            Write-Host "64-bit Assembly compilation successful: Executable generated at $asmExeTarget" -ForegroundColor Green
        } else {
            Write-Warning "Linking distance_calc.obj with gcc failed. Trace will run via integrated engine."
        }
    } else {
        Write-Warning "Assembling distance_calc.asm failed. Trace will run via integrated engine."
    }
} else {
    Write-Host "Note: 'nasm' assembler not found in PATH. 64-bit COA trace execution will run via integrated engine." -ForegroundColor Yellow
}

Write-Host "Build complete for all 4 Stages!" -ForegroundColor Green
Write-Host "Run application using:" -ForegroundColor Cyan
Write-Host "java -cp `"build/output/java;lib/sqlite-jdbc.jar`" Main" -ForegroundColor White

if ($Run) {
    Write-Host "Launching RailRoute GUI..." -ForegroundColor Magenta
    java --enable-native-access=ALL-UNNAMED -cp "$javaOutputDir;lib/sqlite-jdbc.jar" Main
}
