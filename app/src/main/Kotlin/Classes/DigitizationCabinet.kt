package Classes

class DigitizationCabinet <in T : LibraryItems, out D : Discs>{

    fun scanning(item: T): D {
        return when (item) {
            is Books -> {
                println("Оцифрована книга: '${item.name}'")
                Discs(
                    id = item.id + 10212,
                    name = item.name,
                    isAvailable = true,
                    type = "CD"
                ) as D
            }

            is Newspapers -> {
                println("Оцифрована газета: '${item.name}'")
                Discs(
                    id = item.id + 10323,
                    name = item.name,
                    isAvailable = true,
                    type = "CD"
                ) as D
            }
            else -> throw IllegalArgumentException("Неподдерживаемый тип для оцифровки")
        }
    }
}