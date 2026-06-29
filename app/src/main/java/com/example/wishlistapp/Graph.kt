package com.example.wishlistapp

import android.content.Context
import com.example.wishlistapp.data.Wish
import com.example.wishlistapp.data.WishPriority
import com.example.wishlistapp.data.WishStatus
import org.json.JSONArray
import org.json.JSONObject

object Graph {
    private const val StoreName = "wishlist_store"
    private const val WishesKey = "wishes"

    private lateinit var appContext: Context

    fun provide(context: Context) {
        appContext = context.applicationContext
    }

    fun loadWishes(): List<Wish> {
        val raw = preferences().getString(WishesKey, null) ?: return emptyList()
        val array = JSONArray(raw)
        return List(array.length()) { index ->
            val item = array.getJSONObject(index)
            Wish(
                id = item.optLong("id"),
                title = item.optString("title"),
                description = item.optString("description"),
                priority = item.optEnum("priority", WishPriority.Medium),
                category = item.optString("category", "General"),
                targetPrice = item.optDouble("targetPrice", 0.0),
                savedAmount = item.optDouble("savedAmount", 0.0),
                dueDate = item.optString("dueDate"),
                reminder = item.optString("reminder", "1 week before"),
                status = item.optEnum("status", WishStatus.InProgress),
                imageSeed = item.optInt("imageSeed", 0)
            )
        }
    }

    fun saveWishes(wishes: List<Wish>) {
        val array = JSONArray()
        wishes.forEach { wish ->
            array.put(
                JSONObject()
                    .put("id", wish.id)
                    .put("title", wish.title)
                    .put("description", wish.description)
                    .put("priority", wish.priority.name)
                    .put("category", wish.category)
                    .put("targetPrice", wish.targetPrice)
                    .put("savedAmount", wish.savedAmount)
                    .put("dueDate", wish.dueDate)
                    .put("reminder", wish.reminder)
                    .put("status", wish.status.name)
                    .put("imageSeed", wish.imageSeed)
            )
        }
        preferences().edit().putString(WishesKey, array.toString()).apply()
    }

    private fun preferences() = appContext.getSharedPreferences(StoreName, Context.MODE_PRIVATE)

    private inline fun <reified T : Enum<T>> JSONObject.optEnum(name: String, fallback: T): T {
        return runCatching { enumValueOf<T>(optString(name, fallback.name)) }.getOrDefault(fallback)
    }
}
