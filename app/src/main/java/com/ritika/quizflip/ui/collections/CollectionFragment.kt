// CollectionFragment.kt
package com.ritika.quizflip.ui.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ritika.quizflip.data.Collection
import com.ritika.quizflip.data.CollectionAdapter
import com.ritika.quizflip.data.SharedViewModel
import com.ritika.quizflip.databinding.FragmentCollectionBinding

class CollectionFragment : Fragment() {

    private var _binding: FragmentCollectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: CollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        setupRecyclerView()
        observeCollections()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = CollectionAdapter(
            collections = emptyList(),
            onItemClick = { collection ->
                navigateToFlashcards(collection)
            }
        )
        binding.collectionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.collectionRecyclerView.adapter = adapter
    }

    private fun observeCollections() {
        viewModel.collections.observe(viewLifecycleOwner) { collections ->
            adapter.updateList(collections)
        }
    }

    private fun navigateToFlashcards(collection: Collection) {
        val action = CollectionFragmentDirections.actionCollectionToFlashcard(
            collection.id, collection.title
        )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}