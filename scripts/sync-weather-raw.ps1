# Copy sky MP4s from a local HtcWeatherVideo APK into weather-product/res/raw (build-time only).
$ErrorActionPreference = "Stop"
$root = Split-Path $PSScriptRoot -Parent
$candidates = @(
    (Join-Path $root "..\weather-app\video-companion\HtcWeatherVideo-android34-signed.apk"),
    (Join-Path $root "..\weather-app\video-companion\HtcWeatherVideo.apk"),
    $env:HTC_WEATHER_VIDEO_APK
) | Where-Object { $_ -and (Test-Path $_) }
if ($candidates.Count -eq 0) {
    throw "Set HTC_WEATHER_VIDEO_APK to HtcWeatherVideo*.apk or place it under ../weather-app/video-companion/"
}
$companion = $candidates[0]
$rawDst = Join-Path $root "weather-product\res\raw"
$publicXml = Join-Path $root "weather-product\res\values\public.xml"

New-Item -ItemType Directory -Force -Path $rawDst | Out-Null
Add-Type -AssemblyName System.IO.Compression.FileSystem
$z = [System.IO.Compression.ZipFile]::OpenRead($companion)
$names = New-Object System.Collections.Generic.List[string]
foreach ($e in $z.Entries) {
    if ($e.FullName -notlike "res/raw/weather*.mp4") { continue }
    $dest = Join-Path $rawDst $e.Name
    [System.IO.Compression.ZipFileExtensions]::ExtractToFile($e, $dest, $true)
    $names.Add([IO.Path]::GetFileNameWithoutExtension($e.Name))
}
$z.Dispose()
if ($names.Count -lt 36) { throw "Expected 36 MP4s in $companion" }

$names.Sort()
$rawLines = New-Object System.Collections.Generic.List[string]
$id = 0x7f050000
foreach ($n in $names) {
    $rawLines.Add("    <public type=`"raw`" name=`"$n`" id=`"0x$('{0:x}' -f $id)`" />")
    $id++
}
$static = @'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <public type="drawable" name="bokeh_wallpaper_thumbnail" id="0x7f020000" />
    <public type="drawable" name="wallpaper_icon" id="0x7f020001" />
    <public type="layout" name="city_list_item" id="0x7f030000" />
    <public type="xml" name="weather_video" id="0x7f040000" />
'@
$stringBlock = @'
    <public type="string" name="app_name" id="0x7f060000" />
    <public type="string" name="company" id="0x7f060001" />
    <public type="string" name="weather_video_name" id="0x7f060002" />
    <public type="string" name="weather_video_desc" id="0x7f060003" />
    <public type="string" name="settings_activity_name" id="0x7f060004" />
</resources>
'@
Set-Content -Path $publicXml -Value ($static + "`n" + ($rawLines -join "`n") + "`n" + $stringBlock) -Encoding UTF8
Write-Host "Bundled $($names.Count) MP4s from $companion"
