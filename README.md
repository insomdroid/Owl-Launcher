# Owl Launcher

Launcher Android minimalis berbasis Launcher3 (Trebuchet), dengan gaya visual yang tenang: monokrom, titik, dan garis tipis.

- **Package:** `com.insomdroid.owl`
- **Versi:** 0.1.0 (build pertama, debug)
- **Target:** Android 11 (minSdk 30) sampai Android 17 (targetSdk 37)

## Asal proyek

Basis kode diambil dari branch `trebuchet-stable` di `insomdroid/porting-nothing`, commit `429b7c8`. Basis itu sendiri adalah Launcher3 (AOSP) yang sudah dimodifikasi.

Perubahan di repo ini:
- Nama app dan package ID diganti menjadi Owl Launcher.
- Fitur-fitur yang berkaitan dengan gaya visual baru akan ditambahkan bertahap.

## Lisensi

Kode di repo ini berlisensi **Apache License 2.0** (lihat `LICENSE`), mengikuti Launcher3 dari AOSP. Pemberitahuan hak cipta asli di setiap file sumber dipertahankan.

Proyek ini tidak berafiliasi dengan, tidak didukung oleh, dan tidak memakai aset dari Nothing Technology Limited. Tidak ada font, ikon, atau aset proprietary Nothing di repo ini.

## Build

Build dijalankan lewat GitHub Actions (`.github/workflows/lineage-base.yml`). Secara lokal:

```
cd launcher3
gradle :app:assembleDebug
```

Membutuhkan Java 17, Gradle 8.10, dan Android SDK platform 37 dengan build-tools 36.0.0.

Build sukses hanya membuktikan kompilasi. Pengujian di perangkat dilakukan terpisah.

## Status

| Tahap | Status |
|---|---|
| Basis Launcher3 dapat dibangun | Belum diverifikasi di CI untuk repo ini |
| Nama dan package ID | Selesai |
| Lisensi | Selesai |
| Gaya visual (warna, icon, workspace, All Apps) | Belum dimulai |
| Logo dan ikon | Belum dimulai |
