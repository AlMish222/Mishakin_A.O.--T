import classes.LibraryItems
import classes.Books
import classes.Newspapers
import classes.Discs

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