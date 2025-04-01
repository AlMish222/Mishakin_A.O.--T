package papka

import classes.library.LibraryItems

interface OnItemClickListener {
    fun onItemClick(item: LibraryItems, position: Int)
}