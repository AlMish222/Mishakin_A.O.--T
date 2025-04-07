package classes.library

import papka.TakeHome
import papka.ReturnItem

data class Discs(
    override val id: Int,
    override val name: String,
    override var isAvailable: Boolean,
    val type: String,
    override var imageId: Int
) : LibraryItems(id, name, isAvailable, imageId), TakeHome, ReturnItem {

    override fun getShortInfo(): String {
        return "'$name' доступна: ${if (isAvailable) "Да" else "Нет"}\n"
    }
    override fun getAllInfo(): String {
        return "$type диск с названием: '$name' доступен: ${if (isAvailable) "Да" else "Нет"}\n"
    }

    override fun takeHome() {
        if (isAvailable) {
            isAvailable = false
            println("Диск ID: $id '$name' взяли домой\n")
        } else {
            println("Этот объект уже недоступен\n")
        }
    }

    override fun returnItem() {
        if (!isAvailable) {
            isAvailable = true
            println("Диск ID: $id '$name' вернули\n")
        } else {
            println("Этот объект уже доступен\n")
        }
    }
}