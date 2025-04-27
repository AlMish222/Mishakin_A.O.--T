package com.example.dz_1

import classes.library.Books
import classes.library.Discs
import classes.library.LibraryItems
import classes.library.Newspapers
import interfaces.LibraryDao

class LibraryRepository(private val dao: LibraryDao) {

    suspend fun saveBooks(books: Books) {
        val items = UniversalItemClass_BD(
            id = books.id,
            name = books.name,
            isAvailable = books.isAvailable,
            imageId = books.imageId,
            itemType = EnumClass_BD.BOOK,
            author = books.author,
            pages = books.pages,
        )
        dao.insertItem(items)
    }

    suspend fun saveNewspapers(newspapers: Newspapers) {
        val items = UniversalItemClass_BD(
            id = newspapers.id,
            name = newspapers.name,
            isAvailable = newspapers.isAvailable,
            imageId = newspapers.imageId,
            itemType = EnumClass_BD.NEWSPAPER,
            number = newspapers.number,
            month = newspapers.month
        )
        dao.insertItem(items)
    }

    suspend fun saveDiscs(discs: Discs) {
        val items = UniversalItemClass_BD(
            id = discs.id,
            name = discs.name,
            isAvailable = discs.isAvailable,
            imageId = discs.imageId,
            itemType = EnumClass_BD.DISC,
            discType = discs.type
        )
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
}