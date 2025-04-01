package papka

import classes.library.LibraryItems

interface Shop <out T : LibraryItems>{
    fun toSell(): T
}