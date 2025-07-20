package com.ritika.quizflip.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.ritika.quizflip.R
import com.ritika.quizflip.data.Flashcard
import com.ritika.quizflip.data.SharedViewModel
import com.ritika.quizflip.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!

    private val args: SlideshowFragmentArgs by navArgs()
    private lateinit var viewModel: SharedViewModel

    private var flashcards: List<Flashcard> = emptyList()
    private var currentIndex = 0
    private var isShowingAnswer = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        setupObservers()
        setupButtons()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.flashcards.observe(viewLifecycleOwner) { allFlashcards ->
            flashcards = allFlashcards.filter { it.collectionId == args.collectionId }
            if (flashcards.isNotEmpty()) {
                displayCurrentFlashcard()
            }
        }
    }

    private fun setupButtons() {
        binding.previousButton.setOnClickListener {
            if (currentIndex > 0) {
                addButtonPressEffect(it)
                currentIndex--
                isShowingAnswer = false
                displayCurrentFlashcard()
            }
        }

        binding.nextButton.setOnClickListener {
            if (currentIndex < flashcards.size - 1) {
                addButtonPressEffect(it)
                currentIndex++
                isShowingAnswer = false
                displayCurrentFlashcard()
            }
        }

        binding.flipButton.setOnClickListener {
            addButtonPressEffect(it)
            flipCardWithAnimation()
        }

        binding.shuffleButton.setOnClickListener {
            addButtonPressEffect(it)
            flashcards = flashcards.shuffled()
            currentIndex = 0
            isShowingAnswer = false
            displayCurrentFlashcard()
        }

        binding.flashcard.setOnClickListener {
            flipCardWithAnimation()
        }
    }

    private fun addButtonPressEffect(view: View) {
        try {
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.button_press)
            view.startAnimation(animation)
        } catch (e: Exception) {
            // Ignore animation errors
        }
    }

    private fun flipCardWithAnimation() {
        try {
            val flipOut = AnimationUtils.loadAnimation(requireContext(), R.anim.card_flip_out)

            flipOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    // Update content
                    isShowingAnswer = !isShowingAnswer
                    updateCardContent()

                    // Start flip in animation
                    try {
                        val flipIn = AnimationUtils.loadAnimation(requireContext(), R.anim.card_flip_in)
                        binding.cardContent.startAnimation(flipIn)
                        binding.cardLabel.startAnimation(flipIn)
                    } catch (e: Exception) {
                        // Animation failed, content is already updated
                    }
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })

            binding.cardContent.startAnimation(flipOut)
            binding.cardLabel.startAnimation(flipOut)

        } catch (e: Exception) {
            // Fallback: just update content without animation
            isShowingAnswer = !isShowingAnswer
            updateCardContent()
        }
    }

    private fun displayCurrentFlashcard() {
        if (flashcards.isEmpty()) return

        updateCardContent()
        updateNavigationButtons()
        updateCounter()
    }

    private fun updateCardContent() {
        if (flashcards.isEmpty()) return

        val flashcard = flashcards[currentIndex]

        binding.apply {
            if (isShowingAnswer) {
                cardContent.text = flashcard.answer
                cardLabel.text = "Answer"
                flipButton.text = "Show Question"
            } else {
                cardContent.text = flashcard.question
                cardLabel.text = "Question"
                flipButton.text = "Show Answer"
            }
        }
    }

    private fun updateNavigationButtons() {
        binding.apply {
            previousButton.isEnabled = currentIndex > 0
            nextButton.isEnabled = currentIndex < flashcards.size - 1

            previousButton.alpha = if (currentIndex > 0) 1.0f else 0.5f
            nextButton.alpha = if (currentIndex < flashcards.size - 1) 1.0f else 0.5f
        }
    }

    private fun updateCounter() {
        binding.cardCounter.text = "${currentIndex + 1} / ${flashcards.size}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}