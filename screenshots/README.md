# Sky video previews

Still frames (2 s into each loop) from the **`com.htc.htcweatherassets`** MP4s used by the live wallpaper.  
Videos are **not** included in this repo; install the [video companion](../README.md#video-companion) on your phone to play them.

## Portrait (`weather_*`) — typical phone wallpaper

| | | |
|:---:|:---:|:---:|
| ![Sunny](./portrait/weather_sunny.jpg) | ![Clear](./portrait/weather_clear.jpg) | ![Partly sunny](./portrait/weather_partly_sunny.jpg) |
| sunny | clear | partly_sunny |
| ![Partly cloud day](./portrait/weather_partly_cloud_day.jpg) | ![Partly cloud night](./portrait/weather_partly_cloud_night.jpg) | ![Cloudy day](./portrait/weather_cloudy_day.jpg) |
| partly_cloud_day | partly_cloud_night | cloudy_day |
| ![Cloudy night](./portrait/weather_cloudy_night.jpg) | ![Rain day](./portrait/weather_rain_day.jpg) | ![Rain night](./portrait/weather_rain_night.jpg) |
| cloudy_night | rain_day | rain_night |
| ![Thunder day](./portrait/weather_thunderstorm_day.jpg) | ![Thunder night](./portrait/weather_thunderstorm_night.jpg) | ![Snow day](./portrait/weather_snow_day.jpg) |
| thunderstorm_day | thunderstorm_night | snow_day |
| ![Snow night](./portrait/weather_snow_night.jpg) | ![Fog day](./portrait/weather_fog_day.jpg) | ![Fog night](./portrait/weather_fog_night.jpg) |
| snow_night | fog_day | fog_night |
| ![Windy day](./portrait/weather_windy_day.jpg) | ![Windy night](./portrait/weather_windy_night.jpg) | ![Hot](./portrait/weather_hot.jpg) |
| windy_day | windy_night | hot |

## Landscape (`weather_l_*`) — wider surfaces

| | | |
|:---:|:---:|:---:|
| ![L sunny](./landscape/weather_l_sunny.jpg) | ![L clear](./landscape/weather_l_clear.jpg) | ![L partly sunny](./landscape/weather_l_partly_sunny.jpg) |
| ![L rain day](./landscape/weather_l_rain_day.jpg) | ![L rain night](./landscape/weather_l_rain_night.jpg) | ![L thunder day](./landscape/weather_l_thunderstorm_day.jpg) |

Full set: see [`landscape/`](./landscape/) (18 files).

## Regenerate

From a machine with the HTC video companion APK and FFmpeg:

```powershell
.\scripts\capture-sky-frames.ps1
```
