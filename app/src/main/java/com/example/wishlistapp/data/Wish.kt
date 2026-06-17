package com.example.wishlistapp.data

data class Wish(
    val id: Long = 0L,
    val title: String = "",
    val description: String = ""
)
object DummyWish{
    val wishList = listOf(
        Wish(title = "Apple Watch model 11 ", description = "Apple Watch is the most modern watches around all tipe of watches" ),
        Wish(title = "Apple Watch model 11 ", description = "Apple Watch is the most modern watches around all tipe of watches" ),
        Wish(title = "Apple Watch model 11 ", description = "Apple Watch is the most modern watches around all tipe of watches" ),
        Wish(title = "Apple Watch model 11 ", description = "Apple Watch is the most modern watches around all tipe of watches" ),
        Wish(title = "Apple Watch model 11 ", description = "Apple Watch is the most modern watches around all tipe of watches" ),
        Wish(title = "Apple Watch model 11 ", description = "Apple Watch is the most modern watches around all tipe of watches" ),
        Wish(title = "Apple Watch model 11 ", description = "Apple Watch is the most modern watches around all tipe of watches" ),
        Wish(title = "Apple Watch model 11 ", description = "Apple Watch is the most modern watches around all tipe of watches" ),
        Wish(title = "Apple Watch model 11 ", description = "Apple Watch is the most modern watches around all tipe of watches" ),
        Wish(title = "Apple Watch model 11 ", description = "Apple Watch is the most modern watches around all tipe of watches" ),
        Wish(title = "Apple Watch model 11 ", description = "Apple Watch is the most modern watches around all tipe of watches" ),
    )
}
