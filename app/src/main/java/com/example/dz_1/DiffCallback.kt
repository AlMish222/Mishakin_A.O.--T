package com.example.dz_1

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import classes.library.LibraryItems

class DiffCallback : DiffUtil.ItemCallback<LibraryItems>() {

    override fun areItemsTheSame(oldItem: LibraryItems, newItem: LibraryItems): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: LibraryItems, newItem: LibraryItems): Boolean {
        return oldItem == newItem
    }
}