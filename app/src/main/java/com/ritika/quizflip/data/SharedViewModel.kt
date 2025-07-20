// SharedViewModel.kt
package com.ritika.quizflip.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _collections = MutableLiveData<List<Collection>>(mutableListOf())
    val collections: LiveData<List<Collection>> = _collections

    private val _flashcards = MutableLiveData<List<Flashcard>>(mutableListOf())
    val flashcards: LiveData<List<Flashcard>> = _flashcards

    private var nextCollectionId = 1
    private var nextFlashcardId = 1

    fun addCollection(title: String, description: String) {
        val currentList = _collections.value?.toMutableList() ?: mutableListOf()
        val newCollection = Collection(nextCollectionId++, title, description)
        currentList.add(newCollection)
        _collections.value = currentList
    }

    fun addFlashcard(collectionId: Int, question: String, answer: String) {
        val currentList = _flashcards.value?.toMutableList() ?: mutableListOf()
        val newFlashcard = Flashcard(nextFlashcardId++, collectionId, question, answer)
        currentList.add(newFlashcard)
        _flashcards.value = currentList
    }

    fun updateFlashcard(flashcardId: Int, question: String, answer: String) {
        val currentList = _flashcards.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == flashcardId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(question = question, answer = answer)
            _flashcards.value = currentList
        }
    }

    fun deleteFlashcard(flashcardId: Int) {
        val currentList = _flashcards.value?.toMutableList() ?: mutableListOf()
        currentList.removeAll { it.id == flashcardId }
        _flashcards.value = currentList
    }

    fun getFlashcardsForCollection(collectionId: Int): List<Flashcard> {
        return _flashcards.value?.filter { it.collectionId == collectionId } ?: emptyList()
    }
}