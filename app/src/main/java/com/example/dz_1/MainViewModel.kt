package com.example.dz_1

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import classes.library.Books
import classes.library.Discs
import classes.library.LibraryItems
import classes.library.Newspapers
import createLibraryItems

class MainViewModel : ViewModel() {

    private val _libraryItems = MutableLiveData<List<LibraryItems>>(createLibraryItems())
    val libraryItems: LiveData<List<LibraryItems>> = _libraryItems

    private val _isAddNewItem = MutableLiveData<Boolean>(false)
    val isAddNewItem: LiveData<Boolean> = _isAddNewItem

    private val _selectItem = MutableLiveData<LibraryItems?>()
    val selectItem: LiveData<LibraryItems?> = _selectItem

    private val _addItemType = MutableLiveData<String?>()
    val addItemType: LiveData<String?> = _addItemType

    private val _closeFragment = MutableLiveData<Boolean>(false)
    val closeFragment: LiveData<Boolean> = _closeFragment

    private val _scrollPosition = MutableLiveData<Int>(0)
    val scrollPosition: LiveData<Int> = _scrollPosition

    private val _informationFragmentVisibility = MutableLiveData<Boolean>()
    val informationFragmentVisibility: LiveData<Boolean> = _informationFragmentVisibility

    init {
        //val creator = createLibraryItems()
        _libraryItems.value = createLibraryItems()
    }

    fun deleteItem(position: Int) {
        val currentList = _libraryItems.value?.toMutableList()
        currentList?.removeAt(position)
        _libraryItems.value = currentList ?: emptyList()
    }

    fun addItem(items: LibraryItems) {
        val currentList = _libraryItems.value?.toMutableList() ?: mutableListOf()
        currentList.add(items)
        _libraryItems.value = currentList
        _isAddNewItem.value = false
        _scrollPosition.value = currentList.size - 1
    }

    fun makeInformationInvisible() {
        _informationFragmentVisibility.value = false
    }

    fun saveScrollPosition(position: Int) {
        _scrollPosition.value = position
    }

    fun closeFragment() {
        _closeFragment.value = true
    }

    fun selectItem(items: LibraryItems) {
        _selectItem.value = items
        _isAddNewItem.value = false
        _informationFragmentVisibility.value = true
    }

    fun selectNewItemTypeBook() {
        _addItemType.value = "Book"
    }

    fun selectNewItemTypeNewspaper() {
        _addItemType.value = "Newspaper"
    }

    fun selectNewItemTypeDisc() {
        _addItemType.value = "Disc"
    }

    fun startAddNewItem() {
        _selectItem.value = null
        _isAddNewItem.value = true
        _informationFragmentVisibility.value = true
    }

//    fun createItem(
//        id: Int,
//        name: String,
//        isAvailable: Boolean,
//        imageId: Int,
//        extra1: String,
//        extra2: String,
//        itemType: String
//    ): LibraryItems {
//        return when (itemType) {
//            "Book" -> Books(id, name, isAvailable, imageId, extra2, extra1.toInt())
//            "Newspaper" -> Newspapers(id, name, isAvailable, imageId, extra1.toInt(), extra2)
//            "Disc" -> Discs(id, name, isAvailable, imageId, extra1)
//            else -> throw IllegalArgumentException("Неизвестный тип данных")
//        }
//    }

//    fun createResultIntent(items: LibraryItems): Intent {
//        return Intent().apply {
//            putExtra(ITEM_TYPE, when (items) {
//                is Books -> "Book"
//                is Newspapers -> "Newspaper"
//                is Discs -> "Disc"
//                else -> 0
//            })
//            putExtra(ITEM_ID, items.id)
//            putExtra(ITEM_NAME, items.name)
//            putExtra(IS_AVAILABLE, items.isAvailable)
//            putExtra(ITEM_IMAGE, items.imageId)
//
//            when (items) {
//                is Books -> {
//                    putExtra(BOOK_PAGES, items.pages)
//                    putExtra(BOOK_AUTHOR, items.author)
//                }
//                is Newspapers -> {
//                    putExtra(NEWSPAPER_NUMBER, items.number)
//                    putExtra(NEWSPAPER_MONTH, items.month)
//                }
//                is Discs -> {
//                    putExtra(DISC_TYPE, items.type)
//                }
//            }
//        }
//    }

//    companion object {
//        const val ITEM_TYPE = "itemType"
//        const val ITEM_IMAGE = "im"
//        const val ITEM_ID = "id"
//        const val IS_AVAILABLE = "isAvailable"
//        const val ITEM_NAME = "name"
//        const val BOOK_PAGES = "countPage"
//        const val BOOK_AUTHOR = "author"
//        const val DISC_TYPE = "type"
//        const val NEWSPAPER_NUMBER = "number"
//        const val NEWSPAPER_MONTH = "month"
//    }
}