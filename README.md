# 📰 News Feed Simulator (Compose Multiplatform)

Aplikasi **News Feed Simulator** berbasis Android/KMP yang dibangun menggunakan **Kotlin Coroutines** dan **Kotlin Flow**. Proyek ini merupakan pemenuhan Tugas Praktikum Modul 2 untuk mata kuliah Pengembangan Aplikasi Mobile (PAM) di Institut Teknologi Sumatera (ITERA).

Aplikasi ini menyimulasikan aliran data berita secara *real-time* dengan antarmuka yang modern, dinamis, dan responsif menggunakan **Jetpack Compose (Clean Slate Light Theme)**.

---

## 📸 Screenshots
*(Tampilan antarmuka aplikasi dengan mode "Clean Slate Light Theme")*

<div align="center">
  <img width="200" height="1600" alt="Image" src="https://github.com/user-attachments/assets/3a5ec67c-2d7b-4d1f-a4fa-4791d0acdc71" />
  <img width="200" height="1600" alt="Image" src="https://github.com/user-attachments/assets/51c06855-e9ad-48e0-9be9-38491a5fe7c7" />
  <img width="200" height="1600" alt="Image" src="https://github.com/user-attachments/assets/2517e896-4aed-40cd-8871-1e9c6dbbe98d" />
  <img width="200" height="1600" alt="Image" src="https://github.com/user-attachments/assets/5d90dbb4-788b-4be3-b550-c6b0c64764a0" />
</div>

---

## ✨ Fitur Utama (Sesuai Kriteria Tugas)

Proyek ini telah memenuhi seluruh 5 kriteria utama dari penugasan Modul 2:

1. **Simulasi Flow Otomatis (2 Detik)** ⏱️
   - Menggunakan `flow { ... }` builder dengan `while(true)` dan `delay(2000)` untuk secara konstan mengirim (`emit`) data `RawNews` baru dari lapisan *repository* ke UI setiap 2 detik.
2. **Kategori Filter (Operators)** 🔍
   - Menggunakan Flow Operator `.filter { ... }` pada *stream* data untuk menyortir berita berdasarkan kategori tab yang sedang aktif ("All", "Tech", "Campus", "Sports") secara reaktif.
3. **Data Transformation (Map)** 🔄
   - Menggunakan Flow Operator `.map { ... }` untuk mengubah model `RawNews` (kasar) menjadi `DisplayNews` (siap saji) sebelum dirender oleh Jetpack Compose UI.
4. **Counter "Dibaca" (StateFlow)** 📊
   - Menggunakan `MutableStateFlow` dan `.asStateFlow()` untuk mengelola dan membagikan *state* global jumlah berita yang telah dibuka oleh pengguna di pojok kanan atas layar secara reaktif.
5. **Asynchronous Fetching (Coroutines)** ⚡
   - Saat sebuah kartu berita ditekan, rincian detail tidak langsung muncul. Sistem menggunakan `suspend function`, `withContext(Dispatchers.Default)`, dan `scope.launch` untuk menyimulasikan pengambilan data dari *server API* dengan jeda 1.5 detik (terdapat indikator *loading* memutar).

---

## 🎨 Desain Antarmuka (UI/UX)

* **Clean Slate Light Theme:** Skema warna kustom premium yang bersahabat untuk mata pembaca, menggunakan perpaduan warna *Slate*, *Teal*, *Sky Blue*, dan *Orange*.
* **Expandable Cards:** Menerapkan fungsi `animateContentSize()` dengan kurva *spring* agar kartu berita dapat membuka konten panjang ke bawah (*accordion*) secara mulus tanpa memerlukan *dialog pop-up*.
* **Animated Visibility:** Transisi kemunculan setiap berita baru ke dalam *feed* menggunakan efek `fadeIn` dan `slideInVertically`.

---

## 🛠️ Stack Teknologi

* **Bahasa Utama:** Kotlin 
* **UI Framework:** Compose Multiplatform / Jetpack Compose
* **Asynchronous & Concurrency:** Kotlin Coroutines (`launch`, `Dispatchers.Default`)
* **Reactive Streams:** Kotlin Flow (`flow`, `StateFlow`, `filter`, `map`)

---

## 🚀 Cara Menjalankan Proyek

Pastikan Anda memiliki [Android Studio (versi terbaru yang mendukung Compose)](https://developer.android.com/studio) terinstal di sistem Anda.

1. **Clone Repository ini:**
   ```bash
   git clone [https://github.com/USERNAME_GITHUB_ANDA/NAMA_REPOSITORY_ANDA.git](https://github.com/USERNAME_GITHUB_ANDA/NAMA_REPOSITORY_ANDA.git)
