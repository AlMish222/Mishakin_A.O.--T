package com.example.dz_1

import androidx.room.Entity
import androidx.room.PrimaryKey
import classes.library.Books
import classes.library.Discs
import classes.library.LibraryItems
import classes.library.Newspapers


@Entity(tableName = "library_items")
data class UniversalItemClass_BD(
    @PrimaryKey val id: Int,

    val name: String,
    val isAvailable: Boolean,
    val imageId: Int,
    val itemType: TypeOfElement,

    val author: String? = null,
    val pages: Int? = null,

    val number: Int? = null,
    val month: String? = null,

    val discType: String? = null
)

enum class TypeOfElement {
    BOOK,
    NEWSPAPER,
    DISC
}

fun Books.toUniversalItem(): UniversalItemClass_BD {
    return UniversalItemClass_BD(
        id = this.id,
        name = this.name,
        isAvailable = this.isAvailable,
        imageId = this.imageId,
        itemType = TypeOfElement.BOOK,
        author = this.author,
        pages = this.pages,
        number = null,
        month = null,
        discType = null
    )
}

fun Newspapers.toUniversalItem(): UniversalItemClass_BD {
    return UniversalItemClass_BD(
        id = this.id,
        name = this.name,
        isAvailable = this.isAvailable,
        imageId = this.imageId,
        itemType = TypeOfElement.NEWSPAPER,
        number = this.number,
        month = this.month,
        author = null,
        pages = null,
        discType = null
    )
}

fun Discs.toUniversalItem(): UniversalItemClass_BD {
    return UniversalItemClass_BD(
        id = this.id,
        name = this.name,
        isAvailable = this.isAvailable,
        imageId = this.imageId,
        itemType = TypeOfElement.DISC,
        discType = this.type,
        author = null,
        pages = null,
        number = null,
        month = null
    )
}

fun UniversalItemClass_BD.toDomainItem(): LibraryItems = when(itemType) {
    TypeOfElement.BOOK -> Books(
        id = id,
        name = name,
        isAvailable = isAvailable,
        imageId = imageId,
        author = author ?: "",
        pages = pages ?: 0
    )

    TypeOfElement.NEWSPAPER -> Newspapers(
        id = id,
        name = name,
        isAvailable = isAvailable,
        imageId = imageId,
        number = number ?: 0,
        month = month ?: ""
    )

    TypeOfElement.DISC -> Discs(
        id = id,
        name = name,
        isAvailable = isAvailable,
        imageId = imageId,
        type = discType ?: ""
    )
}

