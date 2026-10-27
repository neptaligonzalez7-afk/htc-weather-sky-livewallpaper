$ErrorActionPreference = "Stop"
$root = Split-Path $PSScriptRoot -Parent
Set-Location $root

$tools = Join-Path $root "tools"
foreach ($jar in @("apktool.jar", "uber-apk-signer.jar", "r8.jar", "json.jar")) {
    if (-not (Test-Path (Join-Path $tools $jar))) {
        throw "Missing tools\$jar — see BUILD.md"
    }
}

& powershell -NoProfile -ExecutionPolicy Bypass -File (Join-Path $PSScriptRoot "sync-weather-raw.ps1")
if ($LASTEXITCODE -ne 0) { throw "sync-weather-raw failed" }

& powershell -NoProfile -ExecutionPolicy Bypass -File (Join-Path $PSScriptRoot "compile.ps1")
if ($LASTEXITCODE -ne 0) { throw "compile failed" }

java -jar "$tools\apktool.jar" b -f weather-product -o weather-sky-unsigned.apk
if ($LASTEXITCODE -ne 0) { throw "apktool failed" }

if (Test-Path "$tools\zipalign.py") {
    python "$tools\zipalign.py" weather-sky-unsigned.apk weather-sky-aligned.apk
} else {
    Copy-Item -Force weather-sky-unsigned.apk weather-sky-aligned.apk
}

java -jar "$tools\uber-apk-signer.jar" --skipZipAlign -a weather-sky-aligned.apk -o signed-weather-sky
Copy-Item -Force signed-weather-sky\weather-sky-aligned-debugSigned.apk "HTC-Weather-Sky-LiveWallpaper.apk"
Write-Host "Output: $root\HTC-Weather-Sky-LiveWallpaper.apk"
