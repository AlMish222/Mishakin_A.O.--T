package classes

import classes.library.Books
import classes.library.Discs
import classes.library.Newspapers
import com.example.dz_1.R
import papka.ReadInLibrary

class DigitizationCabinet <in T : ReadInLibrary> {

    fun scanning(item: T): Discs {
        return when (item) {
            is Books -> {
                println("Оцифрована книга: '${item.name}'")
                Discs(
                    id = item.id + 10212,
                    name = item.name,
                    isAvailable = true,
                    type = "CD",
                    imageId = R.drawable.disc1
                )
            }

            is Newspapers -> {
                println("Оцифрована газета: '${item.name}'")
                Discs(
                    id = item.id + 10323,
                    name = item.name,
                    isAvailable = true,
                    type = "CD",
                    imageId = R.drawable.disc1
                )
            }
            else -> throw IllegalArgumentException("Неподдерживаемый тип для оцифровки")
        }
    }
}