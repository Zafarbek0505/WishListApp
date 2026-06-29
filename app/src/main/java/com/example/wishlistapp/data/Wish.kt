package com.example.wishlistapp.data

data class Wish(
    val id: Long = 0L,
    val title: String = "",
    val description: String = "",
    val priority: WishPriority = WishPriority.Medium,
    val category: String = "Fashion",
    val targetPrice: Double = 0.0,
    val savedAmount: Double = 0.0,
    val dueDate: String = "",
    val reminder: String = "1 week before",
    val status: WishStatus = WishStatus.InProgress,
    val imageSeed: Int = 0
)

enum class WishPriority {
    Low,
    Medium,
    High
}

enum class WishStatus(val label: String) {
    Planned("Planned"),
    InProgress("In Progress"),
    Bought("Bought")
}

object DummyWish {
    val wishList = listOf(
        Wish(
            id = 1L,
            title = "Coach Tabby Shoulder Bag",
            description = "A timeless bag that goes with everything.",
            priority = WishPriority.High,
            category = "Fashion",
            targetPrice = 350.0,
            savedAmount = 175.0,
            dueDate = "Dec 25, 2026",
            imageSeed = 1
        ),
        Wish(
            id = 2L,
            title = "Sony WH-1000XM5",
            description = "Noise cancelling headphones.",
            priority = WishPriority.Medium,
            category = "Tech",
            targetPrice = 399.0,
            savedAmount = 120.0,
            dueDate = "Aug 12, 2026",
            imageSeed = 2
        ),
        Wish(
            id = 3L,
            title = "Daniel Wellington Classic Watch",
            description = "Minimal design, everyday elegance.",
            priority = WishPriority.Low,
            category = "Fashion",
            targetPrice = 199.0,
            savedAmount = 90.0,
            dueDate = "Sep 3, 2026",
            imageSeed = 3
        ),
        Wish(
            id = 4L,
            title = "Smeg Espresso Machine",
            description = "Perfect for my morning coffee.",
            priority = WishPriority.High,
            category = "Home",
            targetPrice = 579.0,
            savedAmount = 210.0,
            dueDate = "Nov 18, 2026",
            imageSeed = 4
        )
    )
}
