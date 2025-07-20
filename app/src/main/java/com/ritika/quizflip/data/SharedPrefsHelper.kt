package com.ritika.quizflip.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefsHelper(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("quizflip_prefs", Context.MODE_PRIVATE)

    private val gson = Gson()

    fun saveCollections(collections: List<Collection>) {
        val json = gson.toJson(collections)
        prefs.edit().putString("collections", json).apply()
    }

    fun loadCollections(): List<Collection> {
        val json = prefs.getString("collections", null)
        return if (json != null) {
            val type = object : TypeToken<List<Collection>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun saveFlashcards(flashcards: List<Flashcard>) {
        val json = gson.toJson(flashcards)
        prefs.edit().putString("flashcards", json).apply()
    }

    fun loadFlashcards(): List<Flashcard> {
        val json = prefs.getString("flashcards", null)
        return if (json != null) {
            val type = object : TypeToken<List<Flashcard>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}
