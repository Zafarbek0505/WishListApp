package com.example.wishlistapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wishlistapp.data.DummyWish
import com.example.wishlistapp.data.Wish
import com.example.wishlistapp.data.WishPriority
import com.example.wishlistapp.data.WishRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WishViewModel(private val wishRepository: WishRepository) : ViewModel() {
    private val _wishes = mutableStateListOf<Wish>().apply {
        addAll(DummyWish.wishList)
    }

    val wishes: List<Wish> = _wishes

    fun getWish(id: Long): Wish? = _wishes.firstOrNull { it.id == id }

    fun saveWish(id: Long?, title: String, description: String, priority: WishPriority) {
        val cleanTitle = title.trim()
        val cleanDescription = description.trim()

        if (cleanTitle.isBlank()) {
            return
        }

        if (id == null || id == 0L) {
            _wishes.add(
                Wish(
                    id = nextId(),
                    title = cleanTitle,
                    description = cleanDescription,
                    priority = priority
                )
            )
            return
        }

        val index = _wishes.indexOfFirst { it.id == id }
        if (index >= 0) {
            _wishes[index] = _wishes[index].copy(
                title = cleanTitle,
                description = cleanDescription,
                priority = priority
            )
        }
    }

    fun deleteWish(id: Long) {
        _wishes.removeAll { it.id == id }
    }

    private fun nextId(): Long = (_wishes.maxOfOrNull { it.id } ?: 0L) + 1L
    lateinit var getAllWishes: Flow<List<Wish>>

    init {
        viewModelScope.launch { getAllWishes = wishRepository.getWishes() }
    }

    fun addWish(wish: Wish) {
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.addAWish(wish)
        }
    }

    fun getAWishById(id: Long): Flow<Wish> {
        return wishRepository.getWishById(id)
    }

    fun updateWish(wish: Wish) {
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.updateAWish(wish)
        }
    }

    fun deleteWish(wish: Wish) {
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.deleteAWish(wish)
        }
    }
}
