package com.example.dz_1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import classes.library.LibraryItems
import com.example.dz_1.databinding.LibraryItemsBinding


class ItemsAdapter(
    private val onItemClickListener: (LibraryItems) -> Unit
): ListAdapter<LibraryItems, ItemsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LibraryItemsBinding.inflate(LayoutInflater.from(parent.context))
        return ItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener{
            onItemClickListener(getItem(position))
        }
    }
}