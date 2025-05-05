package com.example.dz_1

import classes.library.Books
import classes.library.Discs
import classes.library.LibraryItems
import classes.library.Newspapers
import interfaces.LibraryDao

class LibraryRepository(
    private val dao: LibraryDao,
    private val settingsManager: SettingsManager
) {

    private var currentOffset = 0
    private val pageSize = 16

    suspend fun saveBooks(books: Books) {
        val items = books.toUniversalItem()
        dao.insertItem(items)
    }

    suspend fun saveNewspapers(newspapers: Newspapers) {
        val items = newspapers.toUniversalItem()
        dao.insertItem(items)
    }

    suspend fun saveDiscs(discs: Discs) {
        val items = discs.toUniversalItem()
        dao.insertItem(items)
    }

    suspend fun loadAllItems(): List<LibraryItems> {
        return dao.getAll().map { it.toDomainItem() }
    }

    suspend fun deleteItem(id: Int) {
        try {
            dao.deleteById(id)
        } catch (e: Exception) {
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
}