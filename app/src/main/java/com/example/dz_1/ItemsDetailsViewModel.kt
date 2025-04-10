package com.example.dz_1

import android.content.Intent
import androidx.lifecycle.ViewModel
import classes.library.Books
import classes.library.Discs
import classes.library.LibraryItems
import classes.library.Newspapers

class ItemsDetailsViewModel : ViewModel() {

    fun createItem(
        id: Int,
        name: String,
        isAvailable: Boolean,
        imageId: Int,
        extra1: String,
        extra2: String,
        itemType: String
    ): LibraryItems {
        return when (itemType) {
            "Book" -> Books(id, name, isAvailable, imageId, extra2, extra1.toInt())
            "Newspaper" -> Newspapers(id, name, isAvailable, imageId, extra1.toInt(), extra2)
            "Disc" -> Discs(id, name, isAvailable, imageId, extra1)
            else -> throw IllegalArgumentException("Неизвестный тип данных")
        }
    }

    fun createResultIntent(items: LibraryItems): Intent {
        return Intent().apply {
            putExtra(ITEM_TYPE, when (items) {
                is Books -> "Book"
                is Newspapers -> "Newspaper"
                is Discs -> "Disc"
                else -> 0
            })
            putExtra(ITEM_ID, items.id)
            putExtra(ITEM_NAME, items.name)
            putExtra(IS_AVAILABLE, items.isAvailable)
            putExtra(ITEM_IMAGE, items.imageId)

            when (items) {
                is Books -> {
                    putExtra(BOOK_PAGES, items.pages)
                    putExtra(BOOK_AUTHOR, items.author)
                }
                is Newspapers -> {
                    putExtra(NEWSPAPER_NUMBER, items.number)
                    putExtra(NEWSPAPER_MONTH, items.month)
                }
                is Discs -> {
                    putExtra(DISC_TYPE, items.type)
                }
            }
        }
    }
    companion object {
        const val ITEM_TYPE = "itemType"
        const val ITEM_IMAGE = "im"
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