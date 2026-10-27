# Building from source

Requires **JDK 17+**, **Python 3** (optional zipalign helper), and build jars under `tools/`:

| File | Purpose |
|------|---------|
| `apktool.jar` | Repack APK resources |
| `r8.jar` | D8 dex (`com.android.tools.r8.D8`) |
| `json.jar` | Open-Meteo JSON parsing at compile time |
| `uber-apk-signer.jar` | Debug-sign APK |

These are not committed (size / licensing). Copy them from your Android build setup or the [apktool](https://apktool.org/) / [R8](https://developer.android.com/build/multidex) distributions.

Builds **bundle** sky MP4s from a local **HtcWeatherVideo** APK (not committed to git):

1. Place `HtcWeatherVideo-android34-signed.apk` (or set `HTC_WEATHER_VIDEO_APK`) — used only on your machine to copy `res/raw` into `weather-product`.
2. Run:

```powershell
cd path\to\htc-weather-sky-livewallpaper
.\scripts\build.ps1
```

Output: `HTC-Weather-Sky-LiveWallpaper.apk` (~146 MB) in the repo root.
