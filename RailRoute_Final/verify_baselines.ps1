$diffs = 0
$refDir = Join-Path $PSScriptRoot "baseline_reference"
$outDir = Join-Path $PSScriptRoot "baseline_outputs"
foreach ($f in (Get-ChildItem $refDir)) {
    $refBytes = [System.IO.File]::ReadAllBytes($f.FullName)
    $outPath = Join-Path $outDir $f.Name
    $outBytes = [System.IO.File]::ReadAllBytes($outPath)
    $cmp = Compare-Object $refBytes $outBytes
    if ($cmp) {
        Write-Host "DIFF: $($f.Name)"
        $diffs++
    }
}
if ($diffs -eq 0) {
    Write-Host "ALL BASELINE FILES MATCH 100% BYTE-FOR-BYTE"
} else {
    Write-Host "FOUND $diffs DIFFERENCE(S)"
}
