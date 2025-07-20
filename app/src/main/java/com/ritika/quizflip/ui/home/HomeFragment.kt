package com.ritika.quizflip.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.ritika.quizflip.R
import com.ritika.quizflip.data.SharedViewModel
import com.ritika.quizflip.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val navController by lazy {
        requireActivity().findNavController(R.id.nav_host_fragment_content_main)
    }

    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        binding.fab.setOnClickListener {
            showCreateCollectionDialog()
        }

        return binding.root
    }

    private fun showCreateCollectionDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_create_collection, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.inputTitle)
        val descInput = dialogView.findViewById<EditText>(R.id.inputDescription)

        AlertDialog.Builder(requireContext())
            .setTitle("New Collection")
            .setView(dialogView)
            .setPositiveButton("Create") { _, _ ->
                val title = titleInput.text.toString()
                val desc = descInput.text.toString()
                if (title.isNotBlank()) {
                    viewModel.addCollection(title, desc)
                    Toast.makeText(requireContext(), "Collection created", Toast.LENGTH_SHORT).show()
                    navController.navigate(
                        HomeFragmentDirections.actionNavHomeToNavGallery()
                    )
                } else {
                    Toast.makeText(requireContext(), "Title required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
