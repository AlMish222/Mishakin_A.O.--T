package com.example.dz_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.dz_1.databinding.ActivityMainBinding
import createLibraryItems

class MainActivity : AppCompatActivity() {

    val libraryItems = createLibraryItems()

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val adapter = ItemsAdapter(this)

    private val swipe = object : Swipe(){
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapter.deleteLibListItem(createLibraryItems(), viewHolder.adapterPosition)
        }
    }

    private val touchHelper = ItemTouchHelper(swipe)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.apply {
            touchHelper.attachToRecyclerView(rcView)
            rcView.layoutManager = GridLayoutManager(this@MainActivity, 1)
            rcView.adapter = adapter
            adapter.setNewLibList(libraryItems)
        }
    }
}

