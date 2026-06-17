package com.example.wishlistapp.data

data class Wish(
    val id: Long = 0L,
    val title: String = "",
    val description: String = "",
    val priority: WishPriority = WishPriority.Medium
)

enum class WishPriority {
    Low,
    Medium,
    High
}

object DummyWish {
    val wishList = listOf(
        Wish(
            id = 1L,
            title = "Apple Watch Series 11",
            description = "Track workouts, sleep, and health goals from the wrist.",
            priority = WishPriority.High
        ),
        Wish(
            id = 2L,
            title = "Noise-cancelling headphones",
            description = "Lightweight headphones for studying and travel.",
            priority = WishPriority.Medium
        ),
        Wish(
            id = 3L,
            title = "Desk lamp",
            description = "Warm LED lamp with adjustable brightness.",
            priority = WishPriority.Low
        )
    )
}
