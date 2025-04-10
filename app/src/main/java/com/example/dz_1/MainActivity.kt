package com.example.dz_1

import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import classes.library.Books
import classes.library.Discs
import classes.library.Newspapers
import com.example.dz_1.databinding.ActivityMainBinding
import createLibraryItems

class MainActivity : AppCompatActivity() {

    private val libraryItems = createLibraryItems().toMutableList()
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainViewModel by viewModels()
    private val viewModel2: ItemsDetailsViewModel by viewModels()

    private val swipe = object : Swipe(){
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapter.deleteLibListItem(viewHolder.adapterPosition)
        }
    }
    private val touchHelper = ItemTouchHelper(swipe)

    private val adapter = ItemsAdapter(libraryItems) { items ->
        val intent = Intent(this, InformationAboutItemsActivity::class.java).apply {
            putExtra(ITEM_ID, items.id)
            putExtra(ITEM_NAME, items.name)
            putExtra(ITEM_STATUS, false)
            putExtra(ITEM_IMAGE, items.imageId)
            putExtra(IS_AVAILABLE, items.isAvailable)
            when (items) {
                is Books -> {
                    putExtra(BOOK_PAGES, items.pages)
                    putExtra(BOOK_AUTHOR, items.author)
                    putExtra(ITEM_TYPE, "Book")
                }
                is Newspapers -> {
                    putExtra(NEWSPAPER_NUMBER, items.number)
                    putExtra(NEWSPAPER_MONTH, items.month)
                    putExtra(ITEM_TYPE, "Newspaper")
                }
                is Discs -> {
                    putExtra(DISC_TYPE, items.type)
                    putExtra(ITEM_TYPE, "Disc")
                }
            }
        }
        startActivity(intent)
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { data ->
                val itemType = data.getStringExtra(ITEM_TYPE) ?: ""
                val newItem = viewModel2.createItem(
                    id = data.getIntExtra(ITEM_ID, 0),
                    name = data.getStringExtra(ITEM_NAME) ?: "",
                    isAvailable = data.getBooleanExtra(IS_AVAILABLE, true),
                    imageId = data.getIntExtra(ITEM_IMAGE, 1),
                    extra1 = when (itemType) {
                        "Book" -> data.getIntExtra(BOOK_PAGES, 0).toString()
                        "Newspaper" -> data.getIntExtra(NEWSPAPER_NUMBER, 0).toString()
                        "Disc" -> data.getStringExtra(DISC_TYPE) ?: ""
                        else -> ""
                    },
                    extra2 = when (itemType) {
                        "Book" -> data.getStringExtra(BOOK_AUTHOR) ?: ""
                        "Newspaper" -> data.getStringExtra(NEWSPAPER_MONTH) ?: ""
                        else -> ""
                    },
                    itemType = itemType
                )
                viewModel.addItem(newItem)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(binding.root)

        viewModel.libraryItems.observe(this) { items ->
            adapter.updateList(items)
        }

        init()
    }

    private fun init() {
        binding.apply {
            touchHelper.attachToRecyclerView(rcView)
            rcView.layoutManager = GridLayoutManager(this@MainActivity, 1)
            rcView.adapter = adapter
            addButton.setOnClickListener{
                val popup = PopupMenu(this@MainActivity, it)
                val inflater: MenuInflater = popup.menuInflater
                inflater.inflate(R.menu.popup_menu_item, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.books -> {
                            startForResult.launch(InformationAboutItemsActivity.createIntent(this@MainActivity).apply {
                                putExtra(ITEM_STATUS, true)
                                putExtra(ITEM_TYPE, "Book")
                            })
                            true
                        }
                        R.id.newspapers -> {
                            startForResult.launch(InformationAboutItemsActivity.createIntent(this@MainActivity).apply {
                                putExtra(ITEM_STATUS, true)
                                putExtra(ITEM_TYPE, "Newspaper")
                            })
                            true
                        }
                        R.id.discs -> {
                            startForResult.launch(InformationAboutItemsActivity.createIntent(this@MainActivity).apply {
                                putExtra(ITEM_STATUS, true)
                                putExtra(ITEM_TYPE, "Disc")
                            })
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }
        }
    }

    companion object {
        const val ITEM_TYPE = "itemType"
        const val ITEM_IMAGE = "im"
        const val ITEM_STATUS = "newItem"
        const val ITEM_ID = "id"
        const val IS_AVAILABLE = "isAvailable"
        const val ITEM_NAME = "name"
        const val BOOK_PAGES = "countPage"
        const val BOOK_AUTHOR = "author"
        const val DISC_TYPE = "type"
        const val NEWSPAPER_NUMBER = "number"
        const val NEWSPAPER_MONTH = "month"
    }
}
