# Owl Launcher — Design Guide

Reference: [vibe-nothing-ui-design](https://github.com/wangbh030722/vibe-nothing-ui-design) (MIT), translated from web to Android. The rules below are rewritten in our own words.

## Decisions

- **Theme:** follows the system by default, with Light, Dark, and AMOLED options.
- **Fonts:** Doto (dot display), Geist (UI), Geist Mono (labels and data). All SIL OFL 1.1.
- **Icons:** monochrome by default.
- **Dot-matrix clock:** required in the first version.
- **Scope:** home screen, widgets, and settings.

## Colors (tokens)

| Token | Dark | Light | AMOLED |
|---|---|---|---|
| `bg` | `#000000` | `#F2F2F2` | `#000000` |
| `surface` | `#0C0C0C` | `#FFFFFF` | `#000000` |
| `raised` | `#171717` | `#EDEDED` | `#0C0C0C` |
| `line` | `#262626` | `#E2E2E2` | `#1A1A1A` |
| `line2` | `#3A3A3A` | `#CFCFCF` | `#2A2A2A` |
| `muted` | `#5A5A5A` | `#9A9A9A` | `#5A5A5A` |
| `secondary` | `#8C8C8C` | `#585A5A` | `#8C8C8C` |
| `primary` | `#EDEDED` | `#1C1C1C` | `#EDEDED` |
| `display` | `#FFFFFF` | `#000000` | `#FFFFFF` |
| `accent` | `#D71921` | `#D71921` | `#D71921` |
| `accentText` | `#FF4438` | `#C2141C` | `#FF4438` |
| `success` | `#7BE38A` | `#3D8B4A` | `#7BE38A` |
| `warning` | `#F2C94C` | `#9C6B00` | `#F2C94C` |
| `error` | `#FF5247` | `#D23B30` | `#FF5247` |

In code, all tokens live in one place: `launcher3/src/com/android/launcher3/owl/OwlTheme.kt` (`OwlTokens`), with modes `SYSTEM`, `LIGHT`, `DARK`, and `AMOLED`. Fonts are in `launcher3/res/font/`, with helpers in `OwlFonts.kt`.

## Hard rules

1. Monochrome as the base, at most four grays per screen, all taken from tokens.
2. Red (`accent`) only for signals: notifications that need action, input required, recording or live dots. Never on buttons, tabs, or hover.
3. Active state is an inverted black-and-white fill.
4. Functional text uses sans-serif (Geist).
5. No shadows or gradients. Depth comes from 1dp hairlines and spacing. Cards have no border.
6. Radius only: 6dp (controls), 8dp (cards and dialogs), pill (chips and switches).
7. All values come from tokens. No raw hex in layouts.

## Spacing and geometry

- 4dp scale: 4, 8, 12, 16, 20, 24, 32, 40, 64.
- Module padding 16–24dp.
- Hairlines 1dp.

## Typography

| Role | Font | Notes |
|---|---|---|
| Dot display | Doto | Clock, date, and standalone numbers |
| UI | Geist | Titles at 600, body at 400 |
| Labels and data | Geist Mono | Uppercase, 9–10sp, letter-spacing 0.08–0.14em |

Standalone numbers (time, date, notification counts) use Doto. Small units stay in Geist Mono.

Known limitation: the bundled Doto is a static weight, so the `ROND` axis (round dots) is not applied. A variable font would be needed for that.

## Dot-grid background

- Dot radius about 1dp, spacing about 40dp (converted from the 120px web reference).
- Implemented as `OwlDotGridView` in `com.android.launcher3.owl`.

## Dot-matrix clock

- `OwlDotClockView` draws `HH:mm` with the 5×7 glyphs in `DotFont`.
- Redraws once per minute, aligned to the minute boundary. The timer stops while the window is not visible.

## Icons

- Monochrome themed icons are on by default in All Apps (`ALLAPPS_THEMED_ICONS` = true).
- Transparent background. Black glyph in Light, white glyph in Dark. Colors are set in `themed_icon_*` in `libs_systemui/iconloaderlib/res/values*/colors.xml`, not from Material You dynamic colors.
- Launcher icon: Owl mark on black, with a monochrome layer. Logos in `docs/brand/`.

## Components by screen

**Home screen:** dot-grid background, app labels in Geist Mono, dot-matrix clock at the top.

**Widgets:** not styled yet. Planned: translucent card with hairline border, title in Geist 600, description in Geist Mono 10sp.

**Settings:** Owl theme choice (System, Light, Dark, AMOLED). Planned: list rows with hairlines, pill switches (white track when on, black knob), dialogs with 8dp radius.

**All Apps:** not styled yet. Planned: list rows with hairlines, outlined search field, no accent color.

## Motion

- Easing `ease-in-out` only. No spring or bounce.
- Controls 200ms, theme change 350ms.
- Pressed state: opacity 0.6.

## Final check

Place a screen next to the web reference. If someone can't tell which one is the Owl Launcher, keep refining until they match.

## Fonts and licenses

Licenses for the bundled fonts are in `docs/fonts/OFL/`.

## Implementation status

Done in code: tokens, Doto/Geist/Geist Mono fonts, dot-grid background, dot-matrix clock, monochrome transparent icons, icon pack selection page, settings and widget-picker colors, All Apps surface colors, uppercase mono app labels.

Not yet done:
- Doto ROND axis (needs a variable Doto font; only static weights are bundled).
- Settings detail: hairline list rows, pill switches, and 8dp dialogs (only colors are applied).
- Widget picker and widget cards: glass cards, label typography, layout.
- Status icons (9x9 dot grid) and the 25x25 glyph matrix hero.
- All Apps blur and bottom-sheet background (still uses Launcher3 blur path).
