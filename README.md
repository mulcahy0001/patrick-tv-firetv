# Patrick TV for Fire TV

This repository contains the Patrick TV website and a native Fire TV wrapper. The Android app opens the hosted Patrick TV guide full-screen and maps the Fire TV remote directly to guide navigation.

## Remote controls

- Direction pad: move through channels and programs
- Select: tune or open the selected program
- Back: close a dialog; press twice to leave Patrick TV
- Play/Pause: tune the selected program

## Automatic APK build

Every push to `main` runs the GitHub Actions workflow and publishes `PatrickTV.apk` under the `firetv-latest` release. The project is intentionally built as a debug APK for simple personal sideloading; Android signs debug builds automatically.

## Fire TV installation

1. Enable **Developer Options > Install unknown apps > Downloader** on the Fire TV.
2. Open Downloader and enter the public APK release URL.
3. Download and install `PatrickTV.apk`.
4. Open Patrick TV from **Your Apps & Channels**.

The app requires an internet connection because it loads the hosted Patrick TV guide.
