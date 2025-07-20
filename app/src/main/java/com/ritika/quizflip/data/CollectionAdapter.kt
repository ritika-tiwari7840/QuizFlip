// CollectionAdapter.kt
package com.ritika.quizflip.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ritika.quizflip.databinding.ItemCollectionsBinding

class CollectionAdapter(
    private var collections: List<Collection>,
    private val onItemClick: (Collection) -> Unit
) : RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCollectionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(collections[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCollectionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = collections[position]
        holder.binding.titleText.text = item.title
        holder.binding.descriptionText.text = item.description
    }

    override fun getItemCount() = collections.size

    fun updateList(newList: List<Collection>) {
        collections = newList
        notifyDataSetChanged()
    }
}