# WishList App

A modern Android application built with **Kotlin** and **Jetpack Compose** that lets users create, manage, and organize personal wishlist items. All data is stored locally on the device using **Room Database**.

---

## Features

- Add wishlist items easily
- View all saved items in a clean list UI
- Delete or update items
- Offline-first with Room Database
- Responsive Jetpack Compose UI
- Back-navigation aware top bar

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Primary language |
| Jetpack Compose | UI toolkit |
| Room Database | Local data persistence |
| Navigation Compose | Screen navigation |
| Material 3 | Design system |
| MVVM | Architecture pattern |

---

## Architecture

This project follows the **MVVM (Model-View-ViewModel)** pattern:

- **Model** — Room database entities and DAOs
- **View** — Jetpack Compose screens (`HomeView`, `AddEditView`)
- **ViewModel** — Business logic and UI state management

---

## Project Structure

```
app/src/main/java/com/example/wishlistapp/
├── MainActivity.kt       # App entry point
├── HomeView.kt           # Main screen listing wishlist items
├── TopBar.kt             # Reusable top app bar component
└── ui/theme/             # Color, typography, and theme definitions
```

---

## Getting Started

1. Clone the repository
2. Open the project in **Android Studio**
3. Sync Gradle and run the app on an emulator or physical device (API 24+)

---

## License

This project is for personal/educational use.
