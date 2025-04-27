package com.example.dz_1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import classes.library.LibraryItems
import createLibraryItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel(
    private val repository: LibraryRepository
) : ViewModel() {
//
    private val _libraryItems = MutableStateFlow<List<LibraryItems>>(emptyList())
    val libraryItems: StateFlow<List<LibraryItems>> = _libraryItems
//
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
        viewModelScope.launch {
            val items = repository.loadAllItems()
            if (items.isEmpty()) {
                createLibraryItems().forEach {
                    repository.insertItem(it)
                }
            }
            loadItems()
        }
    }

    fun loadItems() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                delay(Random.nextLong(200, 2500))
                _libraryItems.value = repository.loadAllItems()

            } catch (e: Exception) {
                _error.value = e.message ?: "Произошла какая-то ошибка"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteItem(position: Int) {
        viewModelScope.launch {
            try {
                val itemToDelete = _libraryItems.value[position]
                repository.deleteItem(itemToDelete.id)

                _libraryItems.value = repository.loadAllItems()

            } catch (e: Exception) {
                _error.value = "Не удалось удалить элемент"
            }
        }
    }

    fun addItem(items: LibraryItems) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                delay(Random.nextLong(100, 600))

                if (Random.nextInt(4) == 0) {
                    throw Exception("Ошибка сохранения данных")
                }

                repository.insertItem(items)
                _libraryItems.value = repository.loadAllItems()
                _scrollPosition.value = _libraryItems.value.size - 1

            } catch (e: Exception) {
                _error.value = "Ошибка добавления"
            } finally {
                _isLoading.value = false
                _isAddNewItem.value = false
            }
        }
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

class MainViewModelFactory(private val repository: LibraryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Неизвестный ViewModel class")
    }
}