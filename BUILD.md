# Building from source

Requires **JDK 17+**, **Python 3** (optional zipalign helper), and build jars under `tools/`:

| File | Purpose |
|------|---------|
| `apktool.jar` | Repack APK resources |
| `r8.jar` | D8 dex (`com.android.tools.r8.D8`) |
| `json.jar` | Open-Meteo JSON parsing at compile time |
| `uber-apk-signer.jar` | Debug-sign APK |

These are not committed (size / licensing). Copy them from your Android build setup or the [apktool](https://apktool.org/) / [R8](https://developer.android.com/build/multidex) distributions.

```powershell
cd path\to\htc-weather-sky-livewallpaper
.\scripts\build.ps1
```

Output: `HTC-Weather-Sky-LiveWallpaper.apk` in the repo root.
