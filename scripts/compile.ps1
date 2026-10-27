$ErrorActionPreference = "Stop"
$root = Split-Path $PSScriptRoot -Parent
Set-Location $root

if (-not $env:JAVA_HOME) {
    $jdk = "C:\Program Files\Microsoft\jdk-17.0.20.101-hotspot"
    if (Test-Path $jdk) { $env:JAVA_HOME = $jdk }
}
if ($env:JAVA_HOME) {
    $env:Path = "$env:JAVA_HOME\bin;" + $env:Path
}

$tools = Join-Path $root "tools"
if (-not (Test-Path (Join-Path $tools "json.jar"))) {
    throw "Missing tools\json.jar — see BUILD.md"
}

New-Item -ItemType Directory -Force -Path stub-classes, weather-classes, weather-out, weather-product\unknown | Out-Null
Remove-Item -Recurse -Force stub-classes, weather-classes, weather-out -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path stub-classes, weather-classes, weather-out | Out-Null

Write-Host "Compiling stubs..."
$stubMap = @{}
$bridgeRoot = (Resolve-Path "bridge-stubs").Path
$lwpRoot = (Resolve-Path "stubs").Path
Get-ChildItem -Recurse "bridge-stubs" -Filter *.java | ForEach-Object {
    $rel = $_.FullName.Substring($bridgeRoot.Length + 1)
    $stubMap[$rel] = $_.FullName
}
Get-ChildItem -Recurse "stubs" -Filter *.java | ForEach-Object {
    $rel = $_.FullName.Substring($lwpRoot.Length + 1)
    $stubMap[$rel] = $_.FullName
}
& javac -encoding UTF-8 -d stub-classes @($stubMap.Values)
if ($LASTEXITCODE -ne 0) { throw "stub javac failed" }

Write-Host "Compiling Weather Sky live wallpaper..."
$src = Get-ChildItem -Recurse "src\com\htc\android\wallpaper\weather" -Filter *.java | ForEach-Object { $_.FullName }
& javac -encoding UTF-8 -classpath "stub-classes;$tools\json.jar" -d weather-classes $src
if ($LASTEXITCODE -ne 0) { throw "weather lwp javac failed" }

$classFiles = Get-ChildItem -Recurse weather-classes -Filter *.class | ForEach-Object { $_.FullName }
& java -cp "$tools\r8.jar" com.android.tools.r8.D8 --min-api 24 --output weather-out $classFiles
if ($LASTEXITCODE -ne 0) { throw "d8 failed" }

Copy-Item -Force weather-out\classes.dex weather-product\unknown\classes.dex
Write-Host "Dex: weather-product\unknown\classes.dex"
