package com.example.dz_1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import classes.library.LibraryItems
import createLibraryItems

class MainViewModel : ViewModel() {
    private val _libraryItems = MutableLiveData<List<LibraryItems>>(createLibraryItems())
    val libraryItems: LiveData<List<LibraryItems>> get() = _libraryItems

    fun deleteItem(position: Int) {
        val currentList = _libraryItems.value?.toMutableList()
        currentList?.removeAt(position)
        _libraryItems.value = currentList ?: emptyList()
    }

    fun addItem(items: LibraryItems) {
        val currentList = _libraryItems.value?.toMutableList() ?: mutableListOf()
        currentList.add(items)
        _libraryItems.value = currentList
    }
}