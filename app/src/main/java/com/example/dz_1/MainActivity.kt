package com.example.dz_1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import classes.library.LibraryItems
import com.example.dz_1.databinding.ActivityMainBinding
import createLibraryItems
import interfaces.OnItemClickListener

class MainActivity : AppCompatActivity(), OnItemClickListener {

    private val libraryItems = createLibraryItems()
    private lateinit var adapter: ItemsAdapter

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(binding.root)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = ItemsAdapter(this)

        binding.rcView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 1)
            adapter = this@MainActivity.adapter
            touchHelper.attachToRecyclerView(this)
        }
        adapter.setNewLibList(libraryItems)

    }

    private val swipe = object : Swipe(){
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapter.deleteLibListItem(viewHolder.adapterPosition)
        }
    }

    private val touchHelper = ItemTouchHelper(swipe)

    override fun onItemClick(item: LibraryItems, position: Int) {
        Toast.makeText(this, "Элемент с id ${item.id}", Toast.LENGTH_SHORT).show()

        item.isAvailable = !item.isAvailable
        adapter.notifyItemChanged(position)
    }
}

