package com.example.dz_1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import classes.library.LibraryItems
import createLibraryItems

class MainViewModel : ViewModel() {

    sealed class LibraryItemsState{
        data class Items(val list: List<LibraryItems>): LibraryItemsState()
        data class SelectedItem(val item: LibraryItems?): LibraryItemsState()
        data class ScrollPosition(val position: Int): LibraryItemsState()
    }

    sealed class UIState{
        data class AddNewItem(val isAdding: Boolean): UIState()
        data class AddItemType(val type: String?): UIState()
        data class CloseFragment(val shouldClose: Boolean): UIState()
        data class InformationFragmentVisibility(val isVisible: Boolean): UIState()
    }

    private val _libraryItemsState = MutableLiveData<LibraryItemsState>()
    val libraryItemsState: LiveData<LibraryItemsState> = _libraryItemsState

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState> = _uiState


    init {
        Log.d("ViewModel", "Initializing ViewModel")
        _libraryItemsState.value = LibraryItemsState.Items(createLibraryItems()).also {
            Log.d("ViewModel", "Initial items: ${it.list.size}")
        }
        _uiState.value = UIState.InformationFragmentVisibility(false)
    }


    fun deleteItem(position: Int) {
        val currentList = (_libraryItemsState.value as? LibraryItemsState.Items)?.list?.toMutableList()
            ?: mutableListOf()
        currentList.removeAt(position)
        _libraryItemsState.value = LibraryItemsState.Items(currentList)
    }

    fun addItem(items: LibraryItems) {
        val currentList = (_libraryItemsState.value as LibraryItemsState.Items)?.list?.toMutableList()
            ?: mutableListOf()
        currentList.add(items)
        _libraryItemsState.value = LibraryItemsState.Items(currentList)
        _uiState.value = UIState.AddNewItem(false)
        _libraryItemsState.value = LibraryItemsState.ScrollPosition(currentList.size - 1)
    }

    fun makeInformationInvisible() {
        _uiState.value = UIState.InformationFragmentVisibility(false)
    }

    fun saveScrollPosition(position: Int) {
        _libraryItemsState.value = LibraryItemsState.ScrollPosition(position)
    }

    fun closeFragment() {
        _uiState.value = UIState.CloseFragment(true)
    }

    fun selectItem(items: LibraryItems) {
        _libraryItemsState.value = LibraryItemsState.SelectedItem(items)
        _uiState.value = UIState.AddNewItem(false)
        _uiState.value = UIState.InformationFragmentVisibility(true)
    }

    enum class Element(private val elementName: String){
        Book("Book"),
        Newspaper("Newspaper"),
        Disc("Disc");

        override fun toString() = elementName
    }

    fun selectNewItemType(element: Element){
        when (element) {
            Element.Book -> _uiState.value = UIState.AddItemType("Book")
            Element.Newspaper -> _uiState.value = UIState.AddItemType("Newspaper")
            Element.Disc -> _uiState.value = UIState.AddItemType("Disc")
        }
    }

    fun startAddNewItem() {
        _libraryItemsState.value = LibraryItemsState.SelectedItem(null)
        _uiState.value = UIState.AddNewItem(true)
        _uiState.value = UIState.InformationFragmentVisibility(true)
    }
}