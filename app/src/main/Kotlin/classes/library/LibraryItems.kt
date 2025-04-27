package classes.library

import androidx.room.Entity

@Entity(tableName = "library_items")
abstract class LibraryItems(
    open val id: Int,
    open val name: String,
    open var isAvailable: Boolean,
    open var imageId: Int
) {
    abstract fun getShortInfo() : String
    abstract fun getAllInfo() : String
}