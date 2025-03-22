package Classes

abstract class LibraryItems(
    open val id: Int,
    open val name: String,
    open var isAvailable: Boolean
) {
    abstract fun getShortInfo() : String
    abstract fun getAllInfo() : String
}