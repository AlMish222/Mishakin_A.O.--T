package classes

import interfaces.TakeHome
import interfaces.ReadInLibrary
import interfaces.ReturnItem

data class Books(
    override val id: Int,
    override val name: String,
    override var isAvailable: Boolean,
    val author: String,
    val pages: Int
) : LibraryItems(id, name, isAvailable), TakeHome, ReadInLibrary, ReturnItem  {

    override fun getShortInfo(): String {
        return "'$name' доступна: ${if (isAvailable) "Да" else "Нет"}\n"
    }
    override fun getAllInfo(): String {
        return "книга: '$name'($pages стр.)автора: $author с id: $id доступна: ${if (isAvailable) "Да" else "Нет"}\n"
    }

    override fun takeHome() {
        if (isAvailable) {
            isAvailable = false
            println("Книга ID: $id '$name' взяли домой\n")
        } else {
            println("Этот объект уже недоступен\n")
        }
    }

    override fun readInLibrary() {
        if(isAvailable){
            isAvailable = false
            println("Книга ID: $id '$name' взяли в читальный зал\n")
        } else {
            println("Этот объект уже недоступен\n")
        }
    }

    override fun returnItem() {
        if (!isAvailable) {
            isAvailable = true
            println("Книга ID: $id '$name' вернули\n" )
        }else {
            println("Этот объект уже недоступен\n")
        }
    }
}