# Extract one JPG per sky MP4 from com.htc.htcweatherassets companion APK.
$ErrorActionPreference = "Stop"
$root = Split-Path $PSScriptRoot -Parent
$apk = Join-Path $root "..\weather-app\video-companion\HtcWeatherVideo-android34-signed.apk"
if (-not (Test-Path $apk)) {
    $apk = "c:\Users\ngonz26\Documents\HTC\weather-app\video-companion\HtcWeatherVideo-android34-signed.apk"
}
if (-not (Test-Path $apk)) {
    throw "Set path to HtcWeatherVideo-android34-signed.apk (video companion)."
}

$ff = (Get-Command ffmpeg -ErrorAction SilentlyContinue).Source
if (-not $ff) {
    $ff = (Get-ChildItem "$env:LOCALAPPDATA\Microsoft\WinGet\Packages\Gyan.FFmpeg*\*\bin\ffmpeg.exe" -ErrorAction SilentlyContinue | Select-Object -First 1).FullName
}
if (-not $ff) { throw "Install FFmpeg (winget install Gyan.FFmpeg)" }

$outRoot = Join-Path $root "screenshots"
$tmp = Join-Path $env:TEMP "htc-sky-mp4"
New-Item -ItemType Directory -Force -Path "$outRoot\portrait", "$outRoot\landscape", $tmp | Out-Null
Add-Type -AssemblyName System.IO.Compression.FileSystem
$z = [System.IO.Compression.ZipFile]::OpenRead($apk)
foreach ($e in $z.Entries) {
    if ($e.FullName -notlike "res/raw/*.mp4") { continue }
    $name = [IO.Path]::GetFileNameWithoutExtension($e.Name)
    $sub = if ($name -like "weather_l_*") { "landscape" } else { "portrait" }
    $mp4 = Join-Path $tmp $e.Name
    [System.IO.Compression.ZipFileExtensions]::ExtractToFile($e, $mp4, $true)
    $jpg = Join-Path (Join-Path $outRoot $sub) ($name + ".jpg")
    & $ff -y -hide_banner -loglevel error -ss 2 -i $mp4 -frames:v 1 -vf "scale=720:-2" -q:v 3 $jpg
    Remove-Item -Force $mp4 -ErrorAction SilentlyContinue
}
$z.Dispose()
Write-Host "Wrote JPGs under $outRoot"
