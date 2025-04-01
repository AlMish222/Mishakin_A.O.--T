package classes.library

import papka.ReadInLibrary
import papka.ReturnItem


enum class Month(val MonthName: String) {
    JANUARY("Январский"),
    FEBRUARY("Февральский"),
    MARCH("Мартовский"),
    APRIL("Апрельский"),
    MAY("Майский"),
    JUNE("Июньский"),
    JULY("Июльский"),
    AUGUST("Августовский"),
    SEPTEMBER("Сентяборьский"),
    OCTOBER("Октяборьский"),
    NOVEMBER("Ноябрьский"),
    DECEMBER("Декабрьский");

    override fun toString() =MonthName
}

data class Newspapers(
    override val id: Int,
    override val name: String,
    override var isAvailable: Boolean,
    val number: Int,
    val month: Month,
    override var imageId: Int
) : LibraryItems(id, name, isAvailable, imageId), ReadInLibrary, ReturnItem {

    override fun getShortInfo(): String {
        return "'$name' доступна: ${if (isAvailable) "Да" else "Нет"}\n"
    }
    override fun getAllInfo(): String {
        //return "$month выпуск $number газуты '$name' с id: $id доступен: ${if (isAvailable) "Да" else "Нет"}\n"
        return "газета '$name' $month выпуска под номером: $number с id: $id доступна: ${if (isAvailable) "Да" else "Нет"}\n"

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