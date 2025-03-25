package classes

import interfaces.ReadInLibrary

class DigitizationCabinet <in T : ReadInLibrary> {

    fun scanning(item: T): Discs {
        return when (item) {
            is Books -> {
                println("Оцифрована книга: '${item.name}'")
                Discs(
                    id = item.id + 10212,
                    name = item.name,
                    isAvailable = true,
                    type = "CD"
                )
            }

            is Newspapers -> {
                println("Оцифрована газета: '${item.name}'")
                Discs(
                    id = item.id + 10323,
                    name = item.name,
                    isAvailable = true,
                    type = "CD"
                )
            }
            else -> throw IllegalArgumentException("Неподдерживаемый тип для оцифровки")
        }
    }
}