package com.example.dz_1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import classes.library.Books
import classes.library.Discs
import classes.library.LibraryItems
import classes.library.Newspapers
import com.example.dz_1.databinding.ActivitySecReviewBinding


class InformationAboutItemsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySecReviewBinding.inflate(layoutInflater)
    }
    private lateinit var items: LibraryItems
    private val viewModel: ItemsDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(binding.root)

        val newItem = intent.getBooleanExtra(ITEM_STATUS, true)

        if (!newItem) {
            showItem()
        } else {
            createNewItem()
        }
    }

    private fun showItem() {
        val itemType = intent.getStringExtra(ITEM_TYPE) ?: ""

        items = viewModel.createItem(
            id = intent.getIntExtra(ITEM_ID, 0),
            name = intent.getStringExtra(ITEM_NAME) ?: "",
            isAvailable = intent.getBooleanExtra(IS_AVAILABLE, true),
            imageId = intent.getIntExtra(ITEM_IMAGE, 1),
            extra1 = when (itemType) {
                "Book" -> intent.getIntExtra(BOOK_PAGES, 0).toString()
                "Newspaper" -> intent.getIntExtra(NEWSPAPER_NUMBER, 0).toString()
                "Disc" -> intent.getStringExtra(DISC_TYPE) ?: ""
                else -> ""
            },
            extra2 = when (itemType) {
                "Book" -> intent.getStringExtra(BOOK_AUTHOR) ?: ""
                "Newspaper" -> intent.getStringExtra(NEWSPAPER_MONTH) ?: ""
                else -> ""
            },
            itemType = itemType
        )
        setOldItems()
    }

    private fun createNewItem() {
        with(binding) {
            nameEditText.hint = "Название"
            idEditText.hint = "Id"
            isAvailableText.hint = "Доступность: \"true/false\""
            when(intent.getStringExtra(ITEM_TYPE)) {
                "Book" -> {
                    im.setImageResource(R.drawable.book1)
                    usersItem.text = "Ваша книга"
                    tvExtra1.hint = "Кол-во страниц"
                    tvExtra2.hint = "Автор"
                }
                "Newspaper" -> {
                    im.setImageResource(R.drawable.newspaper1)
                    usersItem.text = "Ваша газета"
                    tvExtra1.hint = "Номер издания"
                    tvExtra2.hint = "Месяц издания"
                }
                "Disc" -> {
                    im.setImageResource(R.drawable.disc1)
                    usersItem.text = "Ваш диск"
                    tvExtra1.hint = "Тип диска"
                }
            }
            saveButton.setOnClickListener{
                items = viewModel.createItem(
                    idEditText.text.toString().toIntOrNull() ?: INDEX_DEFAULT_VALUE,
                    nameEditText.text.toString(),
                    isAvailableText.text.toString().toBoolean(),
                    when (intent.getStringExtra(ITEM_TYPE)) {
                        "Book" -> R.drawable.book1
                        "Newspaper" -> R.drawable.newspaper1
                        "Disc" -> R.drawable.disc1

                        else -> 0
                    },
                    tvExtra1.text.toString(),
                    tvExtra2.text.toString(),
                    intent.getStringExtra(ITEM_TYPE) ?: ""
                )
                setNewItem()
            }
        }
    }

    private fun setNewItem() {
        val resultIntent = viewModel.createResultIntent(items)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    @SuppressLint("SetTextI18n")
    private fun setOldItems() {
        with(binding) {
            usersItem.text = items.name
            when (items) {
                is Books -> {
                    idEditText.setText("ID: ${items.id}")
                    nameEditText.setText("Название: ${items.name}")
                    isAvailableText.setText("Доступность: ${items.isAvailable}")
                    im.setImageResource(items.imageId)
                    tvExtra1.setText("Кол-во страниц: ${(items as Books).pages}")
                    tvExtra2.setText("Автор: ${(items as Books).author}")
                }
                is Newspapers -> {
                    idEditText.setText("ID: ${items.id}")
                    nameEditText.setText("Название: ${items.name}")
                    isAvailableText.setText("Доступность: ${items.isAvailable}")
                    im.setImageResource(items.imageId)
                    tvExtra1.setText("Номер издания: ${(items as Newspapers).number}")
                    tvExtra2.setText("Месяц издания: ${(items as Newspapers).month}")
                }
                is Discs -> {
                    idEditText.setText("ID: ${items.id}")
                    nameEditText.setText("Название: ${items.name}")
                    isAvailableText.setText("Доступность: ${items.isAvailable}")
                    im.setImageResource(items.imageId)
                    tvExtra1.setText("Тип диска: ${(items as Discs).type}")
                    tvExtra2.isGone = true
                }
            }
            nameEditText.isFocusable = false
            idEditText.isFocusable = false
            isAvailableText.isFocusable = false
            tvExtra1.isFocusable = false
            tvExtra2.isFocusable = false
            saveButton.isGone = true
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
        const val INDEX_DEFAULT_VALUE = -1

        fun createIntent(context: Context): Intent {
            return Intent(context, InformationAboutItemsActivity::class.java)
            }
    }
}