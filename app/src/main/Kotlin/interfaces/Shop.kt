package interfaces

import classes.LibraryItems

interface Shop <out T : LibraryItems>{
    fun toSell(): T
}