package com.example.dz_1

import androidx.recyclerview.widget.RecyclerView
import classes.library.LibraryItems
import com.example.dz_1.databinding.LibraryItemsBinding

class ItemsViewHolder(
    private val binding: LibraryItemsBinding,
): RecyclerView.ViewHolder(binding.root) {

    fun bind(libraryItems: LibraryItems) = with(binding) {
        im.setImageResource(libraryItems.imageId)
        tvTtile.text = itemView.context.getString(R.string.titleName, libraryItems.name)
        itemId.text = itemView.context.getString(R.string.prItemId, libraryItems.id)

        //updateItemState(libraryItems)

//        cardView.setOnClickListener {
//            listener.onItemClick(libraryItems, adapterPosition)
//        }
    }

//    private fun updateItemState(libraryItems: LibraryItems) = with(binding) {
//        val alpha = if (libraryItems.isAvailable) 1f else 0.3f
//        tvTtile.alpha = alpha
//        itemId.alpha = alpha
//        cardView.elevation = if (libraryItems.isAvailable) 10f else 1f
//    }

//    private fun changeElevation(libraryItems: LibraryItems): Float{
//        return if (libraryItems.isAvailable) 10f else 1f
//    }
//
//    private fun changeAlpha(libraryItems: LibraryItems): Float {
//        return if (libraryItems.isAvailable) 1f else 0.3f
//    }
}