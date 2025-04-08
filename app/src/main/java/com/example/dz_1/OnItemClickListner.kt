package com.example.dz_1

import classes.library.LibraryItems

interface OnItemClickListener {
    fun onItemClick(item: LibraryItems, position: Int)
}