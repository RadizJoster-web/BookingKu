# BookingKu — Aplikasi Pemesanan Tiket Bioskop

**UTS Mobile Programming — Universitas Budi Luhur**
Semester Genap 2025/2026

## Identitas

| Nama             | NIM |
|------------------|-----|
| _Radiz Dirganta_ | _2411500107_ |

## Deskripsi Aplikasi

**BookingKu** adalah aplikasi Android sederhana untuk memesan tiket bioskop.
Pengguna dapat mengisi form pemesanan (nama, email, no. HP, film, studio, snack,
tanggal, jam, dan jumlah tiket), kemudian aplikasi akan menampilkan ringkasan
pemesanan beserta total harga di halaman hasil. Dari halaman hasil, pengguna
juga dapat membagikan booking, menghubungi bioskop, mengirim email
konfirmasi, dan membuka website bioskop.

## Pemenuhan Ketentuan UTS

| No | Ketentuan | Implementasi |
|----|-----------|--------------|
| 1  | **2 Activity** | `MainActivity` (form input) & `ResultActivity` (output) |
| 2a | **Explicit Intent** | `MainActivity` → `ResultActivity` saat tombol _Pesan Tiket_ |
| 2b | **Implicit Intent** | `ACTION_SEND` (share), `ACTION_DIAL` (telepon), `ACTION_SENDTO` (email), `ACTION_VIEW` (browser) |
| 3  | **Form Validation** | Field tidak boleh kosong, email harus mengandung `@`, no. HP & jumlah tiket harus angka |
| 4  | **Komponen UI** | `EditText`, `Spinner`, `RadioButton`, `CheckBox`, `ImageView`, `Button`, `TextView` |
| 5  | **SharedPreferences** ⭐ | Menyimpan booking terakhir (`PREF_NAME = "BookingKuPref"`), ditampilkan di Fragment |
| 6  | **Fragment** ⭐ | `HistoryFragment` menampilkan riwayat booking terakhir di `ResultActivity` |

### Fitur Bonus

- **DatePicker** — memilih tanggal tayang
- **TimePicker** — memilih jam tayang
- **4 jenis Implicit Intent** (share, telepon, email, browser)
- **Custom UI** — header gradient, kartu, warna konsisten
- **Penghitungan total harga otomatis** berdasarkan studio + snack + jumlah tiket

## Alur Aplikasi

```
[MainActivity]
   ├─ Isi form (nama, email, hp, film, studio, snack, tanggal, jam, jumlah)
   ├─ Tekan "Pesan Tiket"
   ├─ Validasi form (jika gagal -> tampilkan error)
   └─ Explicit Intent + putExtra() ─────────────────┐
                                                     ▼
                                            [ResultActivity]
                                              ├─ Tampilkan detail
                                              ├─ Hitung total
                                              ├─ Simpan ke SharedPreferences
                                              ├─ Tampilkan HistoryFragment (booking lama)
                                              └─ Tombol Share/Telepon/Email/Website
                                                  -> Implicit Intent ke aplikasi lain
```

## Struktur File Penting

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/myapp/
│   ├── MainActivity.java       # Form input + validasi + explicit intent
│   ├── ResultActivity.java     # Tampilkan hasil + implicit intent + SharedPreferences
│   └── HistoryFragment.java    # Fragment riwayat booking
└── res/
    ├── layout/
    │   ├── activity_main.xml
    │   ├── activity_result.xml
    │   └── fragment_history.xml
    ├── drawable/
    │   ├── ic_movie.xml        # Logo bioskop
    │   ├── bg_card.xml
    │   └── bg_header.xml
    └── values/
        ├── strings.xml
        ├── colors.xml
        └── themes.xml          # + style LabelForm, InputForm, ResultRow
```

## Cara Menjalankan

1. Buka project dengan **Android Studio**.
2. Tunggu Gradle sync selesai.
3. Hubungkan smartphone (mode developer + USB debugging) **ATAU** jalankan emulator.
4. Klik **Run** ▶ (atau `Shift + F10`).
5. Untuk membuat APK: **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
   File APK akan tersedia di `app/build/outputs/apk/debug/app-debug.apk`.

## Catatan

- `minSdk = 24`, `targetSdk = 36`, ditulis dalam **Java**.
- Aplikasi sudah ditest pada perangkat real Android.
