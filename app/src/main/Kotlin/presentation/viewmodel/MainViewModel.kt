package presentation.viewmodel

import RetrofitInstance
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import domain.model.LibraryItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import GoogleBookItem
import domain.model.Books
import data.local.createLibraryItems
import data.repository.LibraryRepository
import retrofit2.HttpException
import java.io.IOException

class MainViewModel(
    private val repository: LibraryRepository
) : ViewModel() {

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

    private val _googleBooks = MutableStateFlow<List<Books>>(emptyList())
    val googleBooks: StateFlow<List<Books>> = _googleBooks

    private var currentOffset = 0
    private val pageSize = 16


    init {
        viewModelScope.launch {
            initializePage()
        }
    }

    fun loadBooks(query: String? = null) {
        viewModelScope.launch {
            _libraryItems.value = repository.loadAllItems()

            if (!query.isNullOrEmpty()) {
                loadGoogleBooks("", query)
            }
        }
    }

    private suspend fun loadGoogleBooks(query: String, query1: String) {
        try {
            val booksFromGoogle = repository.searchBooks(query)
            _googleBooks.value = booksFromGoogle as List<Books>
            _libraryItems.value += booksFromGoogle
        } catch (e: Exception) {
            _error.value = "Ошибка загрузки книг из Google Books"
        }
    }

    private suspend fun initializePage() {
        _isLoading.value = true
        try {
            val items = repository.loadAllItems()
            if (items.isEmpty()) {
                createLibraryItems().forEach{
                    repository.insertItem(it)
                }
            }
            loadInitialPage()
        } catch (e: Exception) {
            _error.value = e.message ?: "Ошибка инициализации базы"
        } finally {
            _isLoading.value = false
        }
    }

    fun loadInitialPage() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                delay(Random.nextLong(200, 2500))
                _libraryItems.value = repository.loadInitialPage()
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка загрузки данных"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                delay(500)
                val newItems = repository.loadNextPage()
                _libraryItems.value += newItems
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка загрузки следующей страницы"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadPreviousPage() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                delay(500)
                val newItems = repository.loadPreviousPage()
                _libraryItems.value = newItems
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка загрузки предыдущей страницы"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _libraryItems.value = repository.refreshPage()
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка обновления списка"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteItem(position: Int) {
        viewModelScope.launch {
            try {
                val itemToDelete = _libraryItems.value.getOrNull(position)
                itemToDelete?.let {
                    repository.deleteItem(it.id)
                    refreshItems()
                }
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

    fun searchGoogleBooks(author: String, title: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val query = buildGoogleBooksQuery(title, author)
                val response = RetrofitInstance.googleBooksApi.searchBooks(
                    query =query,
                    apiKey = RetrofitInstance.apiKey,
                    maxResults = 20
                )

                if (response.isSuccessful) {
                    val items = response.body()?.items ?: emptyList()
                    _googleBooks.value = items.map { it.toBooks() }
                } else {
                    _error.value = "Ошибка ответа от Google Books API"
                    _googleBooks.value = emptyList()
                }
            } catch (e: IOException) {
                _error.value = "Ошибка сети"
                _googleBooks.value = emptyList()
            } catch (e: HttpException) {
                _error.value = "HTTP ошибка: ${e.code()}"
                _googleBooks.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun buildGoogleBooksQuery(title: String, author: String): String {
        val titleQuery = if (title.isNotBlank()) "intitle:$title" else ""
        val authorQuery = if (author.isNotBlank()) "inauthor:$author" else ""
        return listOf(titleQuery, authorQuery).filter { it.isNotBlank() }.joinToString("+")
    }

    private fun GoogleBookItem.toBooks(): Books {
        return Books(
            id = this.id.hashCode(),
            name = this.volumeInfo.title ?: "Без названия",
            isAvailable = true,
            imageId = 0,
            author = this.volumeInfo.authors?.joinToString(", ") ?: "Автор не указан",
            pages = this.volumeInfo.pageCount ?: 0,
        )
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