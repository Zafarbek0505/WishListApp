package com.example.wishlistapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.wishlistapp.data.DummyWish
import com.example.wishlistapp.data.Wish
import com.example.wishlistapp.data.WishPriority
import com.example.wishlistapp.data.WishStatus

class WishViewModel : ViewModel() {
    private val _wishes = mutableStateListOf<Wish>()

    val wishes: List<Wish> = _wishes

    fun getWish(id: Long): Wish? = _wishes.firstOrNull { it.id == id }

    init {
        val savedWishes = Graph.loadWishes()
        _wishes.addAll(savedWishes.ifEmpty { DummyWish.wishList })
        persist()
    }

    fun saveWish(
        id: Long?,
        title: String,
        description: String,
        priority: WishPriority,
        category: String,
        targetPrice: Double,
        savedAmount: Double,
        dueDate: String,
        reminder: String
    ) {
        val cleanTitle = title.trim()
        val cleanDescription = description.trim()
        val cleanCategory = category.trim().ifBlank { "General" }
        val cleanDueDate = dueDate.trim()
        val cleanReminder = reminder.trim().ifBlank { "1 week before" }

        if (cleanTitle.isBlank()) {
            return
        }

        if (id == null || id == 0L) {
            addWish(
                Wish(
                    title = cleanTitle,
                    description = cleanDescription,
                    priority = priority,
                    category = cleanCategory,
                    targetPrice = targetPrice.coerceAtLeast(0.0),
                    savedAmount = savedAmount.coerceIn(0.0, targetPrice.coerceAtLeast(0.0)),
                    dueDate = cleanDueDate,
                    reminder = cleanReminder,
                    imageSeed = (_wishes.size % 5) + 1
                )
            )
            return
        }

        val existing = getWish(id)
        if (existing != null) {
            updateWish(
                existing.copy(
                    title = cleanTitle,
                    description = cleanDescription,
                    priority = priority,
                    category = cleanCategory,
                    targetPrice = targetPrice.coerceAtLeast(0.0),
                    savedAmount = savedAmount.coerceIn(0.0, targetPrice.coerceAtLeast(0.0)),
                    dueDate = cleanDueDate,
                    reminder = cleanReminder
                )
            )
        }
    }

    fun addWish(wish: Wish) {
        _wishes.add(wish.copy(id = nextId()))
        persist()
    }

    fun updateWish(wish: Wish) {
        val index = _wishes.indexOfFirst { it.id == wish.id }
        if (index >= 0) {
            _wishes[index] = wish
            persist()
        }
    }

    fun markWishAsBought(id: Long) {
        getWish(id)?.let { wish ->
            updateWish(wish.copy(status = WishStatus.Bought, savedAmount = wish.targetPrice))
        }
    }

    fun deleteWish(id: Long) {
        getWish(id)?.let(::deleteWish)
    }

    fun deleteWish(wish: Wish) {
        _wishes.removeAll { it.id == wish.id }
        persist()
    }

    private fun nextId(): Long = (_wishes.maxOfOrNull { it.id } ?: 0L) + 1L

    private fun persist() {
        Graph.saveWishes(_wishes)
    }
}
