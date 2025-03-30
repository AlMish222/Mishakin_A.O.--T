package com.example.dz_1


import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import classes.library.LibraryItems
import com.example.dz_1.databinding.LibraryItemsBinding

class ItemsViewHolder(private val binding: LibraryItemsBinding, private val context: Context): RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(libraryItems: LibraryItems) = with(binding) {
        im.setImageResource(libraryItems.imageId)
        tvTtile.text = "Название: ${libraryItems.name}"
        tvTtile.alpha = changeAlpha(libraryItems)
        itemId.text = "ID: ${libraryItems.id}"
        itemId.alpha = changeAlpha(libraryItems)
        cardView.elevation = changeElevation(libraryItems)
        cardView.setOnClickListener(){
            Toast.makeText(context, "Элемент с id ${libraryItems.id}", Toast.LENGTH_SHORT).show()
            libraryItems.isAvailable = !libraryItems.isAvailable
            cardView.elevation = changeElevation(libraryItems)
            tvTtile.alpha = changeAlpha(libraryItems)
            itemId.alpha = changeAlpha(libraryItems)
        }
    }

    private fun changeElevation(libraryItems: LibraryItems): Float{
        return if (libraryItems.isAvailable) 10f else 1f
    }

    private fun changeAlpha(libraryItems: LibraryItems): Float {
        return if (libraryItems.isAvailable) 1f else 0.3f
    }
}