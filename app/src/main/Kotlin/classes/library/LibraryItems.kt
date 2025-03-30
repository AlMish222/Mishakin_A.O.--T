package classes.library

abstract class LibraryItems(
    open val id: Int,
    open val name: String,
    open var isAvailable: Boolean,
    open var imageId: Int
) {
    abstract fun getShortInfo() : String
    abstract fun getAllInfo() : String
}