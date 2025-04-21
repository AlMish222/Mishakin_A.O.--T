package com.example.dz_1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import classes.library.LibraryItems
import createLibraryItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val _libraryItems = MutableStateFlow<List<LibraryItems>>(emptyList())
    val libraryItems: StateFlow<List<LibraryItems>> = _libraryItems

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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
        _libraryItems.value = createLibraryItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                delay(Random.nextLong(200, 2500))

                if (Random.nextInt(4) == 0) {
                    throw Exception("Ошибка загрузки")
                }

                _libraryItems.value = createLibraryItems()
            } catch (e: Exception) {
                _error.value = e.message ?: "Произошла какая-то ошибка"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteItem(position: Int) {
        val currentList = _libraryItems.value?.toMutableList()
        currentList?.removeAt(position)
        _libraryItems.value = currentList ?: emptyList()
    }

    fun addItem(items: LibraryItems) {
        val currentList = _libraryItems.value.toMutableList()
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                delay(Random.nextLong(100, 600))

                if (Random.nextInt(4) == 0) {
                    throw Exception("Ошибка сохранения данных")
                }
                currentList.add(items)
                _libraryItems.value = currentList
            } catch (e: Exception) {
                _error.value = e.message ?: "Произошла какая-то ошибка"
            } finally {
                _isLoading.value = false
                _isAddNewItem.value = false
            }
        }
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
}