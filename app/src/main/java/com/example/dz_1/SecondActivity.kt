package com.example.dz_1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import classes.library.Books
import classes.library.Discs
import classes.library.LibraryItems
import classes.library.Newspapers
import com.example.dz_1.databinding.ActivitySecReviewBinding


class SecondActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySecReviewBinding.inflate(layoutInflater)
    }
    private lateinit var items: LibraryItems

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
        items = when (intent.getStringExtra(ITEM_TYPE)) {

            "Book" -> Books(
                intent.getIntExtra(ITEM_ID, 0),
                intent.getStringExtra(ITEM_NAME) ?: "",
                intent.getBooleanExtra(IS_AVAILABLE, true),
                intent.getIntExtra(ITEM_IMAGE, 1),
                intent.getStringExtra(BOOK_AUTHOR) ?: "",
                intent.getIntExtra(BOOK_PAGES, 0),
            )
            
            "Newspaper" -> Newspapers(
                intent.getIntExtra(ITEM_ID, 0),
                intent.getStringExtra(ITEM_NAME) ?: "",
                intent.getBooleanExtra(IS_AVAILABLE, true),
                intent.getIntExtra(ITEM_IMAGE, 1),
                intent.getIntExtra(NEWSPAPER_NUMBER, 0),
                intent.getStringExtra(NEWSPAPER_MONTH) ?: ""
            )

            "Disc" -> Discs(
                intent.getIntExtra(ITEM_ID, 0),
                intent.getStringExtra(ITEM_NAME) ?: "",
                intent.getBooleanExtra(IS_AVAILABLE, true),
                intent.getIntExtra(ITEM_IMAGE, 1),
                intent.getStringExtra(DISC_TYPE) ?: ""
            )
            else -> throw IllegalArgumentException("Неизвестный тип данных")
        }
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
                items = when (intent.getStringExtra(ITEM_TYPE)) {
                    "Book" -> Books(
                        idEditText.text.toString().toIntOrNull() ?: INDEX_DEFAULT_VALUE,
                        nameEditText.text.toString(),
                        isAvailableText.text.toString().toBoolean(),
                        R.drawable.book1,
                        tvExtra2.text.toString(),
                        tvExtra1.text.toString().toIntOrNull() ?: INDEX_DEFAULT_VALUE
                    )
                    "Newspaper" -> Newspapers(
                        idEditText.text.toString().toIntOrNull() ?: INDEX_DEFAULT_VALUE,
                        nameEditText.text.toString(),
                        isAvailableText.text.toString().toBoolean(),
                        R.drawable.newspaper1,
                        tvExtra1.text.toString().toIntOrNull() ?: INDEX_DEFAULT_VALUE,
                        tvExtra2.text.toString()
                    )
                    "Disc" -> Discs(
                        idEditText.text.toString().toIntOrNull() ?: INDEX_DEFAULT_VALUE,
                        nameEditText.text.toString(),
                        isAvailableText.text.toString().toBoolean(),
                        R.drawable.disc1,
                        tvExtra1.text.toString()
                    )

                    else -> throw IllegalArgumentException("Неизвестный тип данных")
                }
                setNewItem()
            }
        }
    }

    private fun setNewItem() {
        val resultIntent = when (items) {
            is Books -> Intent().apply {
                putExtra(ITEM_ID, items.id)
                putExtra(ITEM_NAME, items.name)
                putExtra(IS_AVAILABLE, items.isAvailable)
                putExtra(ITEM_IMAGE, items.imageId)
                putExtra(BOOK_PAGES, (items as Books).pages)
                putExtra(BOOK_AUTHOR, (items as Books).author)
                putExtra(ITEM_TYPE, "Book")
            }
            is Newspapers -> Intent().apply {
                putExtra(ITEM_ID, items.id)
                putExtra(ITEM_NAME, items.name)
                putExtra(IS_AVAILABLE, items.isAvailable)
                putExtra(ITEM_IMAGE, items.imageId)
                putExtra(NEWSPAPER_NUMBER, (items as Newspapers).number)
                putExtra(NEWSPAPER_MONTH, (items as Newspapers).month)
                putExtra(ITEM_TYPE, "Newspaper")
            }
            is Discs -> Intent().apply {
                putExtra(ITEM_ID, items.id)
                putExtra(ITEM_NAME, items.name)
                putExtra(IS_AVAILABLE, items.isAvailable)
                putExtra(ITEM_IMAGE, items.imageId)
                putExtra(DISC_TYPE, (items as Discs).type)
                putExtra(ITEM_TYPE, "Disc")
            }
            else -> throw IllegalArgumentException("Неизвестный тип данных")
        }
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
            return Intent(context, SecondActivity::class.java)
            }
    }
}