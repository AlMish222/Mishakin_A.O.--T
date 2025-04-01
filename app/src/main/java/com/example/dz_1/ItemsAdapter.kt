package com.example.dz_1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import classes.library.LibraryItems
import com.example.dz_1.databinding.LibraryItemsBinding
import interfaces.OnItemClickListener


class ItemsAdapter(
    private val listener: OnItemClickListener
): RecyclerView.Adapter<ItemsViewHolder>() {

    private val libList = mutableListOf<LibraryItems>()

    fun setNewLibList(newLibList: List<LibraryItems>) {
        libList.clear()
        libList.addAll(newLibList)
        notifyDataSetChanged()
    }

    fun deleteLibListItem(position: Int) {
        libList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LibraryItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ItemsViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(libList[position])
    }

    override fun getItemCount(): Int {
        return libList.size
    }


}

//class DiffCallback : DiffUtil.ItemCallback<LibraryItems>() {
//    override fun areItemsTheSame(oldItem: LibraryItems, newItem: LibraryItems): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: LibraryItems, newItem: LibraryItems): Boolean {
//        return oldItem == newItem
//    }
//
//}