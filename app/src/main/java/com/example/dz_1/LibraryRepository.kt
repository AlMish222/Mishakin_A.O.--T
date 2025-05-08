package com.example.dz_1

import android.util.Log
import classes.library.Books
import classes.library.Discs
import classes.library.LibraryItems
import classes.library.Newspapers
import interfaces.GoogleBooksApi
import interfaces.LibraryDao
import retrofit2.http.Query

class LibraryRepository(
    private val dao: LibraryDao,
    private val settingsManager: SettingsManager,
    private val googleBooksApi: GoogleBooksApi
) {

    private var currentOffset = 0
    private val pageSize = 16

    suspend fun loadAllItems(): List<LibraryItems> {
        return dao.getAll().map { it.toDomainItem() }
    }

    suspend fun deleteItem(id: Int) {
        try {
            dao.deleteById(id)
        } catch (e: Exception) {
            Log.e("LibraryRepository", "Error deleting item: ${e.message}")
            throw Exception("Ошибка удаления: ${e.message}")
        }
    }

    suspend fun insertItem(items: LibraryItems) {
        when(items) {
            is Books -> dao.insertItem(items.toUniversalItem())
            is Newspapers -> dao.insertItem(items.toUniversalItem())
            is Discs -> dao.insertItem(items.toUniversalItem())
        }
    }

    suspend fun loadInitialPage(): List<LibraryItems> {
        currentOffset = 0
        val sortType = settingsManager.getSortType()
        val items = when (sortType) {
            SortType.BY_NAME -> dao.getAllSortedByName(pageSize, currentOffset)
            SortType.BY_ID -> dao.getAllSortedById(pageSize, currentOffset)
        }
        return items.map { it.toDomainItem() }
    }

    suspend fun loadNextPage(): List<LibraryItems> {
        val sortType = settingsManager.getSortType()
        currentOffset += pageSize / 2
        val items = when (sortType) {
            SortType.BY_NAME -> dao.getAllSortedByName(pageSize / 2, currentOffset)
            SortType.BY_ID -> dao.getAllSortedById(pageSize / 2, currentOffset)
        }
        return items.map { it.toDomainItem() }
    }

    suspend fun loadPreviousPage(): List<LibraryItems> {
        val sortType = settingsManager.getSortType()
        currentOffset = (currentOffset - pageSize / 2).coerceAtLeast(0)
        val items = when (sortType) {
            SortType.BY_NAME -> dao.getAllSortedByName(pageSize / 2, currentOffset)
            SortType.BY_ID -> dao.getAllSortedById(pageSize / 2, currentOffset)
        }
        return items.map { it.toDomainItem() }
    }

    suspend fun refreshPage(): List<LibraryItems> {
        currentOffset = 0
        return loadInitialPage()
    }

    fun saveSortType(sortType: SortType) {
        settingsManager.saveSortType(sortType)
    }

    fun getSortType(): SortType {
        return settingsManager.getSortType()
    }

    suspend fun searchBooks(query: String): List<LibraryItems> {
        return try {
            Log.d("LibraryRepository", "Searching books with query: $query")

            val response = googleBooksApi.searchBooks(
                query = query,
                apiKey = RetrofitInstance.apiKey,
                maxResults = 20
            )

            if (response.isSuccessful) {
                Log.d("LibraryRepository", "Search successful, books found: ${response.body()?.items?.size}")

                response.body()?.items?.mapNotNull { googleBookItem ->
                    val volume = googleBookItem.volumeInfo
                    if (volume.title != null && volume.authors != null && volume.authors.isNotEmpty()) {
                        Books(
                            id = 0,
                            name = volume.title,
                            isAvailable = true,
                            imageId = 0,
                            author = volume.authors.joinToString(", ") ?: "Неизвестен",
                            pages = volume.pageCount ?: 0
                        )
                    } else {
                        null
                    }
                } ?: emptyList()
            } else {
                Log.e("LibraryRepository", "Error: ${response.code()} - ${response.message()}")

                emptyList()
            }
        } catch (e: Exception) {
            Log.e("LibraryRepository", "Error during book search: ${e.message}")

            emptyList()
        }
    }
}