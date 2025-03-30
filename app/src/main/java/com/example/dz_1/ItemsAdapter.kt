package com.example.dz_1

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import classes.library.LibraryItems
import com.example.dz_1.databinding.LibraryItemsBinding


class ItemsAdapter(private val context: Context): RecyclerView.Adapter<ItemsViewHolder>() {

    private val libList = mutableListOf<LibraryItems>()

    fun setNewLibList(newLibList: List<LibraryItems>) {
        libList.addAll(newLibList)
        notifyDataSetChanged()
    }

    fun deleteLibListItem(currentData: List<LibraryItems>, position: Int) {
        libList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LibraryItemsBinding.inflate((LayoutInflater.from(parent.context)))
        return ItemsViewHolder(view, context)
    }

    override fun getItemCount(): Int {
        return libList.size
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(libList[position])
    }
}