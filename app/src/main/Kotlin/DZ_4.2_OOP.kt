abstract class LibraryItems(
    open val id: Int,
    open val name: String,
    open var isAvailable: Boolean
) {
    abstract fun getShortInfo() : String
    abstract fun getAllInfo() : String
}

data class Books(
    override val id: Int,
    override val name: String,
    override var isAvailable: Boolean,
    val author: String,
    val pages: Int
) : LibraryItems(id, name, isAvailable) {

    override fun getShortInfo(): String {
        return "'$name' доступна: ${if (isAvailable) "Да" else "Нет"}\n"
    }
    override fun getAllInfo(): String {
        return "книга: '$name'($pages стр.)автора: $author с id: $id доступна: ${if (isAvailable) "Да" else "Нет"}\n"
    }
}

data class Newspapers(
    override val id: Int,
    override val name: String,
    override var isAvailable: Boolean,
    val number: Int
) : LibraryItems(id, name, isAvailable) {

    override fun getShortInfo(): String {
        return "'$name' доступна: ${if (isAvailable) "Да" else "Нет"}\n"
    }
    override fun getAllInfo(): String {
        return "выпуск $number газуты '$name' с id: $id доступен: ${if (isAvailable) "Да" else "Нет"}\n"
    }
}

data class Discs(
    override val id: Int,
    override val name: String,
    override var isAvailable: Boolean,
    val type: String
) : LibraryItems(id, name, isAvailable) {

    override fun getShortInfo(): String {
        return "'$name' доступна: ${if (isAvailable) "Да" else "Нет"}\n"
    }
    override fun getAllInfo(): String {
        return "$type '$name' доступен: ${if (isAvailable) "Да" else "Нет"}\n"
    }
}

fun createLibraryItems() : List<LibraryItems> {
    return listOf(
        Books(id = 1, name = "Маугли", isAvailable = true, author = "Джозеф Киплинг", pages = 202),
        Books(id = 2, name = "1984", isAvailable = false, author = "Джордж Оруэлл", pages = 328),
        Newspapers(id = 3, name = "Сельская жизнь", isAvailable = false, number = 794),
        Books(id = 4, name = "Чистилище", isAvailable = true, author = "Сергей Тармашев", pages = 413),
        Books(id = 5, name = "Епифань", isAvailable = true, author = "Джордж Оруэлл", pages = 477),
        Discs(id = 6, name = "The Dark Side of the Moon", isAvailable = true, type = "CD"),
        Discs(id = 7, name = "Inception", isAvailable = false, type = "DVD"),
        Newspapers(id = 8, name = "Комсамольская правда", isAvailable = true, number = 1015)
    )
}

fun printFirstMenu(){
    val text = """
        Привет, впиши цифру соответствующую пункту меню:
        
        1. Показать книги
        2. Показать газеты
        3. Показать диски
        
        0. Выход
    """.trimIndent()
    println(text)
}

fun printSecondMenu(){
    val text1 = """
        Выберите действие:
        
        1. Взять домой
        2. Читать в читальном зале
        3. Показать подробную информацию
        4. Вернуть
        
        5. Вернуться в главное меню        
        0. Вернуться к краткой информации
    """.trimIndent()
    println(text1)
}

fun main() {

    val libraryItems = createLibraryItems()
    while (true) {

        printFirstMenu()
        when(readLine()?.toIntOrNull()) {
            1 -> showItems(libraryItems.filterIsInstance<Books>())
            2 -> showItems(libraryItems.filterIsInstance<Newspapers>())
            3 -> showItems(libraryItems.filterIsInstance<Discs>())

            0 -> return
            else -> println("Неверный ввод, попробуйте снова\n")
        }
    }
}

fun showItems(items: List<LibraryItems>) {
    while (true) {
        println("Список объектов:\n")
        items.forEachIndexed{ index, item ->
            println("${index + 1}. ${item.getShortInfo()}")
        }
        println("0. Вернуться к выбору прдмета\n")

        val choice = readLine()?.trim()?.toIntOrNull()
        when(choice) {
            0 -> return

            in 1..items.size -> {
                val selectedItem = items[choice!! - 1]
                val shouldReturnToMainMenu = handleItemActions(selectedItem)
                if (shouldReturnToMainMenu){
                    return
                }
            }
            else -> {
                println("Неверный выбор, попробуйте снова\n")
            }
        }
    }
}

fun handleItemActions(item: LibraryItems): Boolean{
    while (true) {

        printSecondMenu()
        when (readLine()?.trim()?.toIntOrNull()) {
            1 -> takeHome(item)
            2 -> readInLibrary(item)
            3 -> println(item.getAllInfo())
            4 -> returnItem(item)

            5 -> return true
            0 -> break
            else -> println("Неверный выбор, попробуйте снова\n")
        }
    }
    return false
}

fun takeHome(item: LibraryItems) {
    when(item) {
        is Books, is Discs -> {
            if (item.isAvailable) {
                item.isAvailable = false
                println("${item::class.simpleName} ID: ${item.id} '${item.name}' взяли домой\n")
            } else {
                println("Этот объект уже недоступен\n")
            }
        }
        else -> println("Этот объект нельзя взять домой\n")
    }
}

fun readInLibrary(item: LibraryItems) {
    when (item) {
        is Books, is Newspapers -> {
            if(item.isAvailable){
                item.isAvailable = false
                println("${item::class.simpleName} ID: ${item.id} '${item.name}' взяли в читальный зал\n")
            } else {
                println("Этот объект уже недоступен\n")
            }
        }
        else -> println("Этот объект нельзя читать в читальном зале\n")
    }
}

fun returnItem(item: LibraryItems) {
    if (!item.isAvailable) {
        item.isAvailable = true
        println("${item::class.simpleName} ID: ${item.id} '${item.name}' вернули\n")
    } else {
        println("Этот объект уже доступен\n")
    }
}


