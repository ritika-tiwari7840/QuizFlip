package com.ritika.quizflip.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ritika.quizflip.databinding.ItemFlashcardBinding

class FlashcardAdapter(
    private var flashcards: List<Flashcard>,
    private val onEditClick: (Flashcard) -> Unit,
    private val onDeleteClick: (Flashcard) -> Unit
) : RecyclerView.Adapter<FlashcardAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemFlashcardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFlashcardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flashcard = flashcards[position]
        holder.binding.apply {
            questionText.text = flashcard.question
            answerText.text = flashcard.answer

            editButton.setOnClickListener {
                onEditClick(flashcard)
            }

            deleteButton.setOnClickListener {
                onDeleteClick(flashcard)
            }
        }
    }

    override fun getItemCount() = flashcards.size

    fun updateList(newList: List<Flashcard>) {
        flashcards = newList
        notifyDataSetChanged()
    }
}