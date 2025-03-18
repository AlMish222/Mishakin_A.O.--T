package classes

import interfaces.ReadInLibrary
import interfaces.ReturnItem

data class Newspapers(
    override val id: Int,
    override val name: String,
    override var isAvailable: Boolean,
    val number: Int
) : LibraryItems(id, name, isAvailable), ReadInLibrary, ReturnItem {

    override fun getShortInfo(): String {
        return "'$name' доступна: ${if (isAvailable) "Да" else "Нет"}\n"
    }
    override fun getAllInfo(): String {
        return "выпуск $number газуты '$name' с id: $id доступен: ${if (isAvailable) "Да" else "Нет"}\n"
    }

    override fun readInLibrary() {
        if (isAvailable) {
            isAvailable = false
            println("Газета ID: $id '$name' взяли в читальный зал\n")
        } else {
            println("Этот объект уже недоступен\n")
        }
    }

    override fun returnItem() {
        if (!isAvailable) {
            isAvailable = true
            println("Газета ID: $id '$name' вернули\n")
        } else {
            println("Этот объект уже доступен\n")
        }
    }
}