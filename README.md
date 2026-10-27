# HTC Weather Sky Live Wallpaper (Android)

**Live wallpaper** that loops classic **HTC Weather** sky videos (sunny, rain, clouds, etc.) on your home screen, driven by **Open-Meteo** on the device. Works as a **standalone app** — you do not need the HTC Weather app or the flip clock widget.

> **Not affiliated with HTC.** Community port for modern 64-bit Android. Sky **MP4 files** are HTC assets distributed via a separate companion package (see below).

**Related projects**

- [HTC Sense Bokeh Live Wallpaper](https://github.com/neptaligonzalez7-afk/htc-sense-live-wallpaper) — Sense-style bokeh background (separate package).
- [HTC Weather Flip Clock](https://github.com/neptaligonzalez7-afk/htc-weather-flip-clock) — 4×2 flip clock widget with Open-Meteo.

## Screenshots

Preview frames from each sky clip (MP4s ship in the video companion, not in this repo):

| Sunny | Rain (day) | Thunder (night) | Snow (day) |
|-------|------------|-----------------|------------|
| ![Sunny sky](./screenshots/portrait/weather_sunny.jpg) | ![Rain day](./screenshots/portrait/weather_rain_day.jpg) | ![Thunder night](./screenshots/portrait/weather_thunderstorm_night.jpg) | ![Snow day](./screenshots/portrait/weather_snow_day.jpg) |

**[Full gallery — all 36 portrait + landscape clips](./screenshots/README.md)**

## Download

Get the latest APK from **[Releases](https://github.com/neptaligonzalez7-afk/htc-weather-sky-livewallpaper/releases)**.

| Release | File | versionCode | versionName |
|---------|------|-------------|-------------|
| **Latest** | `HTC-Weather-Sky-LiveWallpaper.apk` | **106** | `1.1.5-aspect-crop-gl` |

Package: **`com.htc.android.wallpaper.weather`** (does not replace Sense Bokeh or the flip clock).

## Requirements

- Android **7.0+** (API 24), **targetSdk 36**
- Live wallpaper support (One UI, Nova, etc.)
- **Video companion** app **`com.htc.htcweatherassets`** — contains the sky MP4 resources (large download, install once)

### Video companion

The wallpaper app is small; the **sky videos** ship in HTC’s resource package:

1. Obtain **HtcWeatherVideo** / `com.htc.htcweatherassets` (community mirrors often host `HtcWeatherVideo.apk`; ~146 MB).
2. Install that APK **before or after** Weather Sky.
3. Without it, the wallpaper runs but **cannot play** sky clips (logcat will mention missing `com.htc.htcweatherassets`).

This repo does **not** redistribute HTC’s video APK due to size and licensing.

## Install

1. Install **video companion** (`com.htc.htcweatherassets`) if needed.
2. Download **HTC Weather Sky** from **Releases** and install.
3. **Wallpapers → Live wallpapers → HTC Weather Sky** (or launcher picker → gear).
4. Set location:
   - **Automatic (GPS / network)** — allow location when prompted.
   - **Manual** — type a city → **Search cities** → **tap a row** in the list (saves and syncs immediately).

Open **Weather Sky location** from the app drawer or wallpaper settings gear anytime to change city or mode.

## What it does

- Maps Open-Meteo conditions to HTC sky video indices (day/night variants).
- Loops the matching MP4 full-screen with **OpenGL center-crop** (correct aspect on tall phones).
- Refreshes weather periodically and on boot / app update.
- Optional high refresh rate while visible.

## Troubleshooting

| Symptom | What to try |
|---------|-------------|
| Black or no sky | Install **`com.htc.htcweatherassets`**; re-select the wallpaper. |
| Stretched / squashed video | Use **v1.1.5+** (`1.1.5-aspect-crop-gl`). |
| Wrong city | Manual mode: search and **tap** a list result (typing alone does not geocode). |
| Settings seem idle | Check status text; wait for “Syncing…” / toast after tap. |

## Build from source

See [BUILD.md](./BUILD.md). Source is under `src/`, APK shell under `weather-product/`.

## Signing

Release APKs are **debug-signed** for sideloading.

## License

See [LICENSE](./LICENSE). HTC Sense / Weather names and sky video assets are trademarks of their respective owners.

## Reporting issues

Open a **GitHub Issue** with device model, Android version, launcher, APK **versionName**, and whether **`com.htc.htcweatherassets`** is installed.

## Support (optional)

If this wallpaper is useful on your phone, you can [buy me a coffee](https://buymeacoffee.com/neta4457).
