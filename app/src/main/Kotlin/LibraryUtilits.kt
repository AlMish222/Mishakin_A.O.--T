import classes.library.Books
import classes.library.Discs
import classes.library.LibraryItems
import classes.library.Month
import classes.library.Newspapers
import com.example.dz_1.R


fun createLibraryItems(): List<LibraryItems> {
    return listOf(
        Books(id = 1, name = "Маугли", isAvailable = true, author = "Джозеф Киплинг", pages = 202, imageId = R.drawable.book1),
        Books(id = 2, name = "1984", isAvailable = false, author = "Джордж Оруэлл", pages = 328, imageId = R.drawable.book1),
        Newspapers(id = 3, name = "Сельская жизнь", isAvailable = false, number = 794, month = Month.JANUARY, imageId = R.drawable.newspaper1),
        Books(id = 4, name = "Чистилище", isAvailable = true, author = "Сергей Тармашев", pages = 413, imageId = R.drawable.book1),
        Books(id = 5, name = "Епифань", isAvailable = true, author = "Н.И. Никонов", pages = 477, imageId = R.drawable.book1),
        Discs(id = 6, name = "The Dark Side of the Moon", isAvailable = true, type = "CD", imageId = R.drawable.disc1),
        Discs(id = 7, name = "Inception", isAvailable = false, type = "DVD", imageId = R.drawable.disc1),
        Newspapers(id = 8, name = "Комсамольская правда", isAvailable = true, number = 1015, month = Month.MARCH, imageId = R.drawable.newspaper1),
        Discs(id = 9, name = "Наруто", isAvailable = false, type = "CD", imageId = R.drawable.disc1)
    )
}

inline fun <reified T> List<*>.filterTypes() : List<T> {
    return this.filterIsInstance<T>()
}

fun demonstrateFilterTypes(libraryItems: List<LibraryItems>) {

    val books: List<Books> = libraryItems.filterTypes()
    println("Книги:\n")
    books.forEach { println(it.getAllInfo()) }

    val newspapers: List<Newspapers> = libraryItems.filterTypes()
    println("Газеты:\n")
    newspapers.forEach { println(it.getAllInfo()) }


    val discs: List<Discs> = libraryItems.filterTypes()
    println("Диски:\n")
    discs.forEach { println(it.getAllInfo()) }
}

fun printFirstMenu(){
    val text = """
        Привет, впиши цифру соответствующую пункту меню:
        
        1. Показать книги
        2. Показать газеты
        3. Показать диски
        4. Покупки новых объектов
        5. Кабинет оцифровки
        6. Общий список объектов нашей библиотеке 
        
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

fun printShopMenu(){
    val text2 = """
        Выберете магазин для покупок:
        
        1. Магазин книг
        2. Магазин дисков
        3. Газетный ларёк
        
        0. Вернуться в галвное меню
        
    """.trimIndent()
    println(text2)
}

fun printDigCabMenu() {
    val text3 = """
        Выберете тип объекта для оцифровки:
        
        1. Оцифровать книгу
        2. Оцифровать газету
        
        0. Вернуться в главное меню
        
    """.trimIndent()
    println(text3)
}

