package com.example.dz_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import classes.library.Books
import classes.library.Discs
import classes.library.LibraryItems
import classes.library.Newspapers
import com.example.dz_1.databinding.ActivityMainBinding
import createLibraryItems

class MainActivity : AppCompatActivity(), OnItemClickListener {

    private val libraryItems = createLibraryItems().toMutableList()
    private lateinit var adapter: ItemsAdapter
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    companion object {
        private const val REQUEST_ADD_ITEM = 1001
        private const val MODE_VIEW = "view"
        private const val MODE_ADD = "add"
    }

    private val swipe = object : Swipe(){
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapter.deleteLibListItem(viewHolder.adapterPosition)
        }
    }
    private val touchHelper = ItemTouchHelper(swipe)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(binding.root)
        initRecyclerView()

        binding.addButton.setOnClickListener{
            showAddItmeScreen()
        }

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

    private fun showAddItmeScreen() {
        val selElement = arrayOf(
            "Book",
            "Newspaper",
            "Disc"
        )
        AlertDialog.Builder(this)
            .setTitle("Выберите элемент")
            .setItems(selElement) {_, which ->
            val type = when (which) {
                0 -> "book"
                1 -> "newspaper"
                2 -> "disc"
                else -> return@setItems
            }
            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra("MODE", "add")
                putExtra("ITEM_TYPE", type)
            }
            startActivityForResult(intent, REQUEST_ADD_ITEM)
        }
        .setNegativeButton("Отмена", null).show()
    }


    override fun onItemClick(item: LibraryItems, position: Int) {
        val intent = Intent(this, SecondActivity::class.java).apply {
            putExtra("MODE", MODE_VIEW)
            when (item) {
                is Books -> putExtra("BOOK", item)
                is Discs -> putExtra("DISC", item)
                is Newspapers -> putExtra("NEWSPAPER", item)
            }
        }
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == REQUEST_ADD_ITEM && resultCode == RESULT_OK) {
            val newItem = data?.run {
                when {
                    hasExtra("BOOK") -> getParcelableExtra<Books>("BOOK")
                    hasExtra("DISC") -> getParcelableExtra<Discs>("DISC")
                    hasExtra("NEWSPAPER") -> getParcelableExtra<Newspapers>("NEWSPAPER")
                    else -> null
                }
            }
            newItem?.let {
                libraryItems.add(it)
                adapter.setNewLibList(libraryItems)
            }
        }
    }
}
