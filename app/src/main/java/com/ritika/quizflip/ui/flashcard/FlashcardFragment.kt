// FlashcardFragment.kt
package com.ritika.quizflip.ui.flashcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ritika.quizflip.R
import com.ritika.quizflip.data.Flashcard
import com.ritika.quizflip.data.FlashcardAdapter
import com.ritika.quizflip.data.SharedViewModel
import com.ritika.quizflip.databinding.FragmentFlashcardBinding

class FlashcardFragment : Fragment() {

    private var _binding: FragmentFlashcardBinding? = null
    private val binding get() = _binding!!

    private val args: FlashcardFragmentArgs by navArgs()
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: FlashcardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlashcardBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        setupRecyclerView()
        observeFlashcards()
        setupButtons()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = FlashcardAdapter(
            flashcards = emptyList(),
            onEditClick = { flashcard -> showEditFlashcardDialog(flashcard) },
            onDeleteClick = { flashcard -> showDeleteConfirmation(flashcard) }
        )
        binding.flashcardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.flashcardRecyclerView.adapter = adapter
    }

    private fun observeFlashcards() {
        viewModel.flashcards.observe(viewLifecycleOwner) { allFlashcards ->
            val collectionFlashcards = allFlashcards.filter { it.collectionId == args.collectionId }
            adapter.updateList(collectionFlashcards)

            // Update slideshow button visibility
            binding.startSlideshowButton.visibility = if (collectionFlashcards.isNotEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }

            updateEmptyView(collectionFlashcards)
        }
    }

    private fun updateEmptyView(collectionFlashcards: List<Flashcard>) {
        if (collectionFlashcards.isEmpty()) {
            binding.flashcardRecyclerView.visibility = View.GONE
            binding.emptyStateText.visibility = View.VISIBLE
        } else {
            binding.flashcardRecyclerView.visibility = View.VISIBLE
            binding.emptyStateText.visibility = View.GONE
        }
    }

    private fun setupButtons() {
        binding.addFlashcardButton.setOnClickListener {
            showAddFlashcardDialog()
        }

        binding.startSlideshowButton.setOnClickListener {
            val flashcards = viewModel.getFlashcardsForCollection(args.collectionId)
            if (flashcards.isNotEmpty()) {
                val action = FlashcardFragmentDirections.actionFlashcardToSlideshow(
                    args.collectionId, args.collectionTitle
                )
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "No flashcards to show", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddFlashcardDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_flashcard, null)
        val questionInput = dialogView.findViewById<EditText>(R.id.inputQuestion)
        val answerInput = dialogView.findViewById<EditText>(R.id.inputAnswer)

        AlertDialog.Builder(requireContext())
            .setTitle("Add Flashcard")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val question = questionInput.text.toString().trim()
                val answer = answerInput.text.toString().trim()

                if (question.isNotBlank() && answer.isNotBlank()) {
                    viewModel.addFlashcard(args.collectionId, question, answer)
                    Toast.makeText(requireContext(), "Flashcard added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Both question and answer are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditFlashcardDialog(flashcard: Flashcard) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_flashcard, null)
        val questionInput = dialogView.findViewById<EditText>(R.id.inputQuestion)
        val answerInput = dialogView.findViewById<EditText>(R.id.inputAnswer)

        questionInput.setText(flashcard.question)
        answerInput.setText(flashcard.answer)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Flashcard")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val question = questionInput.text.toString().trim()
                val answer = answerInput.text.toString().trim()

                if (question.isNotBlank() && answer.isNotBlank()) {
                    viewModel.updateFlashcard(flashcard.id, question, answer)
                    Toast.makeText(requireContext(), "Flashcard updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Both question and answer are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmation(flashcard: Flashcard) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Flashcard")
            .setMessage("Are you sure you want to delete this flashcard?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteFlashcard(flashcard.id)
                Toast.makeText(requireContext(), "Flashcard deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
