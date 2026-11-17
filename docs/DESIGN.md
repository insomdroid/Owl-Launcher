# Owl Launcher — Panduan Desain

Sumber acuan: [vibe-nothing-ui-design](https://github.com/wangbh030722/vibe-nothing-ui-design) (MIT), diterjemahkan dari web ke Android. Aturan ditulis ulang dengan kata-kata sendiri.

## Keputusan

- **Tema:** mengikuti sistem, dengan pilihan Light, Dark, dan AMOLED.
- **Font:** Doto (tampilan dot), Geist (UI), Geist Mono (label dan data). Semua SIL OFL 1.1.
- **Ikon:** monokrom secara default.
- **Jam dot-matrix:** wajib di versi pertama.
- **Cakupan:** homescreen, widget, dan pengaturan.

## Warna (token)

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

Di Android: semua token ada di satu tempat, `launcher3/src/com/android/launcher3/owl/OwlTheme.kt` (`OwlTokens`), dengan mode `SYSTEM`, `LIGHT`, `DARK`, dan `AMOLED`. Font ada di `launcher3/res/font/`, dan helpernya di `OwlFonts.kt`.

## Aturan keras

1. Monokrom sebagai dasar, maksimal empat abu-abu per layar, semuanya dari token.
2. Merah (`accent`) hanya untuk sinyal: notifikasi yang butuh tindakan, status yang butuh input, dot rekaman atau live. Jangan dipakai di tombol, tab, atau hover.
3. Status aktif berupa inversi hitam-putih.
4. Teks fungsional memakai sans-serif (Geist).
5. Tanpa shadow dan gradien. Kedalaman dibuat dengan hairline 1dp dan spasi. Kartu tanpa border.
6. Radius hanya 6dp (kontrol), 8dp (kartu, dialog), dan pill (chip, switch).
7. Semua nilai diambil dari token, tanpa hex mentah di layout.

## Spacing dan geometri

- Skala 4dp: 4, 8, 12, 16, 20, 24, 32, 40, 64.
- Padding modul 16–24dp.
- Hairline 1dp.

## Tipografi

| Peran | Font | Catatan |
|---|---|---|
| Dot display | Doto, ROND 100, weight ≤ 500 | Untuk jam, tanggal, dan angka berdiri sendiri |
| UI | Geist | Judul 600, isi 400 |
| Label dan data | Geist Mono | Uppercase, 9–10sp, letter-spacing 0.08–0.14em |

Angka berdiri sendiri (jam, tanggal, jumlah notifikasi) memakai Doto. Satuan kecil tetap Geist Mono.

## Latar dot field

- Titik radius sekitar 1.3px (dalam dp dikonversi dari referensi web), jarak 120px (sekitar 40dp).
- Diberi blend difference di belakang kartu.
- Diimplementasikan sebagai `DotGridDrawable` di `nothingstyle/`.

## Jam dot-matrix

- Diimplementasikan sebagai `DotMatrixClockView` di `nothingstyle/`, dengan glyph 5×7 (`DotFont`).
- Animasi detik hanya berjalan saat view terlihat, untuk menghemat baterai.

## Ikon

- Default monokrom lewat `MonoIconThemeFactory` yang sudah ada di basis.
- Ikon status 9×9 berupa lingkaran penuh, tanpa mask atau dotted border.
- Ikon fungsional: garis 1.5dp, ujung bulat, `stroke` mengikuti warna teks.

## Komponen per layar

**Homescreen:** latar dot field, label ikon dalam Geist Mono uppercase, hotseat dengan jam dot-matrix.

**Widget:** kartu kaca transparan dengan hairline, judul Geist 600, deskripsi Geist Mono 10sp.

**Pengaturan:** daftar dengan hairline, switch pill (track putih saat aktif, knob hitam), chip segmen untuk pilihan tema, dialog radius 8dp dengan tombol teks dan tombol inversi.

**All Apps:** daftar dengan hairline, pencarian berupa field outline, tanpa warna aksen.

## Motion

- Easing `ease-in-out` saja, tanpa spring atau bounce.
- Kontrol 200ms, ganti tema 350ms.
- Tekan: opacity 0.6.

## Tes akhir

Letakkan layar di samping referensi web. Jika orang asing bisa membedakan mana yang bukan produk Owl Launcher, perbaiki sampai sama.
