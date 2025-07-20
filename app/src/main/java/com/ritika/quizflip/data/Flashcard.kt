package com.ritika.quizflip.data

data class Flashcard(
    val id: Int,
    val collectionId: Int,
    val question: String,
    val answer: String
)
