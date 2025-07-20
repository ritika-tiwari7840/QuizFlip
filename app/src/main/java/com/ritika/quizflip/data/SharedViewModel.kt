package com.ritika.quizflip.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _collections = MutableLiveData<List<Collection>>(mutableListOf())
    val collections: LiveData<List<Collection>> = _collections

    private val _flashcards = MutableLiveData<List<Flashcard>>(mutableListOf())
    val flashcards: LiveData<List<Flashcard>> = _flashcards

    private lateinit var prefsHelper: SharedPrefsHelper

    private var nextCollectionId = 1
    private var nextFlashcardId = 1

    // Call this once in your activity or fragment
    fun initPrefs(context: Context) {
        prefsHelper = SharedPrefsHelper(context)

        val savedCollections = prefsHelper.loadCollections()
        val savedFlashcards = prefsHelper.loadFlashcards()

        _collections.value = savedCollections
        _flashcards.value = savedFlashcards

        nextCollectionId = (savedCollections.maxOfOrNull { it.id } ?: 0) + 1
        nextFlashcardId = (savedFlashcards.maxOfOrNull { it.id } ?: 0) + 1
    }

    fun addCollection(title: String, description: String) {
        val currentList = _collections.value?.toMutableList() ?: mutableListOf()
        val newCollection = Collection(nextCollectionId++, title, description)
        currentList.add(newCollection)
        _collections.value = currentList
        prefsHelper.saveCollections(currentList)
    }

    fun addFlashcard(collectionId: Int, question: String, answer: String) {
        val currentList = _flashcards.value?.toMutableList() ?: mutableListOf()
        val newFlashcard = Flashcard(nextFlashcardId++, collectionId, question, answer)
        currentList.add(newFlashcard)
        _flashcards.value = currentList
        prefsHelper.saveFlashcards(currentList)
    }

    fun updateFlashcard(flashcardId: Int, question: String, answer: String) {
        val currentList = _flashcards.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == flashcardId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(question = question, answer = answer)
            _flashcards.value = currentList
            prefsHelper.saveFlashcards(currentList)
        }
    }

    fun deleteFlashcard(flashcardId: Int) {
        val currentList = _flashcards.value?.toMutableList() ?: mutableListOf()
        currentList.removeAll { it.id == flashcardId }
        _flashcards.value = currentList
        prefsHelper.saveFlashcards(currentList)
    }

    fun getFlashcardsForCollection(collectionId: Int): List<Flashcard> {
        return _flashcards.value?.filter { it.collectionId == collectionId } ?: emptyList()
    }
}
