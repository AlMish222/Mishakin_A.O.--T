package com.example.dz_1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import classes.library.LibraryItems
import com.example.dz_1.databinding.LibraryItemsBinding


class ItemsAdapter(
    private val liblist: MutableList<LibraryItems>,
    private val onItemClickListener: ((LibraryItems) -> Unit)
): RecyclerView.Adapter<ItemsViewHolder>() {

    fun setNewLibList(newLibList: MutableList<LibraryItems>) {
        liblist.clear()
        liblist.addAll(newLibList)
        notifyDataSetChanged()
    }

    fun addLibListItem(items: LibraryItems) {
        liblist.addAll(listOf(items)) //
        notifyDataSetChanged()
    }

    fun deleteLibListItem(position: Int) {
        liblist.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LibraryItemsBinding.inflate(LayoutInflater.from(parent.context))
        return ItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(liblist[position])
        holder.itemView.setOnClickListener{
            onItemClickListener(liblist[position])
        }
    }

    override fun getItemCount(): Int = liblist.size
}