# Owl Launcher

A minimal Android launcher built on Launcher3 (Trebuchet), with a calm visual style: monochrome, dots, and hairlines.

- **Package:** `com.insomdroid.owl`
- **Version:** 0.1.1 (debug build, optimized: R8 and resource shrink, arm64 only)
- **Support:** Android 12 (minSdk 31) to Android 16 (targetSdk 36)

## Origin

The code base comes from the `trebuchet-stable` branch of `insomdroid/porting-nothing`, commit `429b7c8`. That base is Launcher3 (AOSP) with local modifications.

Changes in this repo:
- App name and package ID changed to Owl Launcher.
- Visual style added on top of Launcher3: typography, dot-grid background, dot-matrix clock, monochrome icons, and two built-in widgets.

## Design

See [docs/DESIGN.md](docs/DESIGN.md). The visual reference is [vibe-nothing-ui-design](https://github.com/wangbh030722/vibe-nothing-ui-design) (MIT), translated by hand into Android code. No code from that repository is copied.

## Features

- **Appearance:** follows the system light or dark setting. There is no separate theme option.
- **Typography:** Doto (dot display), Geist (UI), Geist Mono (app labels). All fonts are SIL OFL 1.1, see `docs/fonts/OFL/`.
- **Home screen:** no dot background. The wallpaper from the system shows through.
- **Icons:** monochrome icons by default, with a transparent background and a black (light) or white (dark) glyph. Applies to home screen and All Apps.
- **Icon packs:** Settings → Icons → Icon pack. Pick Default or an installed icon pack; the choice applies to home screen and All Apps immediately. Supports the common pack intents (Nova, Apex, ADW, GO).
- **Widgets:**
  - *Owl clock*: time in the dot-matrix face, updated once a minute.
  - *Media*: previous, play or pause, and next for the app that is playing. Uses media key events, so no extra permission is needed.
- **Settings:** grouped into Icons, Home screen, App drawer, and Gestures and more.
- **Launcher icon:** Owl mark on black, with a monochrome layer for themed icons. Logos are in `docs/brand/`.

## License

Code in this repo is licensed under the **Apache License 2.0** (see `LICENSE`), following Launcher3 from AOSP. Copyright notices in the original source files are kept.

Owl Launcher is not affiliated with, endorsed by, or sponsored by Nothing Technology Limited. No Nothing fonts, icons, or other proprietary assets are included.

## Build

Builds run on GitHub Actions (`.github/workflows/lineage-base.yml`). To build locally:

```
cd launcher3
gradle :app:assembleDebug
```

Requires Java 17, Gradle 8.10, and Android SDK platform 36 with build-tools 36.0.0.

Builds run only on manual trigger (`workflow_dispatch`), not on every push. A successful build only proves that the code compiles. Behavior and appearance must be checked on a device.

## Status

| Item | Status |
|---|---|
| Launcher3 base builds in CI | Verified (debug APK) |
| Name, package ID, license | Done |
| Dot-grid background | Removed |
| Monochrome icons on home and All Apps | Implemented, not yet verified on device |
| Icon pack selection applies everywhere | Implemented, not yet verified on device |
| Owl clock and media widgets | Implemented, not yet verified on device |
| Settings: suggestions and icon theme toggle removed | Done |
| Widget card typography and All Apps background | Not started |
| Android 17 (API 37) target | Waiting for platform 37 in CI |
