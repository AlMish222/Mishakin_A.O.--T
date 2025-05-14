package firstDZ

import domain.model.LibraryItems

interface Shop <out T : LibraryItems>{
    fun toSell(): T
}