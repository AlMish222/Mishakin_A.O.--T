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
        Newspapers(id = 3, name = "Сельская жизнь", isAvailable = false, number = 794, month = Month.JANUARY.toString(), imageId = R.drawable.newspaper1),
        Books(id = 4, name = "Чистилище", isAvailable = true, author = "Сергей Тармашев", pages = 413, imageId = R.drawable.book1),
        Books(id = 5, name = "Епифань", isAvailable = true, author = "Н.И. Никонов", pages = 477, imageId = R.drawable.book1),
        Discs(id = 6, name = "The Dark Side of the Moon", isAvailable = true, type = "CD", imageId = R.drawable.disc1),
        Discs(id = 7, name = "Inception", isAvailable = false, type = "DVD", imageId = R.drawable.disc1),
        Newspapers(id = 8, name = "Комсомольская правда", isAvailable = true, number = 1015,
            month = Month.MARCH.toString(), imageId = R.drawable.newspaper1),
        Discs(id = 9, name = "Наруто", isAvailable = false, type = "CD", imageId = R.drawable.disc1),
        Newspapers(id = 10, name = "Комсомольская ложь", isAvailable = true, number = 1111,
            month = Month.NOVEMBER.toString(), imageId = R.drawable.newspaper1),
        Newspapers(id = 11, name = "Космонавты", isAvailable = true, number = 115,
            month = Month.JULY.toString(), imageId = R.drawable.newspaper1),
        Newspapers(id = 12, name = "Space exploration", isAvailable = true, number = 15,
            month = Month.MARCH.toString(), imageId = R.drawable.newspaper1),
        Newspapers(id = 13, name = "МГТУ", isAvailable = false, number = 1,
            month = Month.SEPTEMBER.toString(), imageId = R.drawable.newspaper1),
        Newspapers(id = 14, name = "Боль ИУ", isAvailable = false, number = 5,
            month = Month.MARCH.toString(), imageId = R.drawable.newspaper1),
        Newspapers(id = 15, name = "Страдания ФН", isAvailable = true, number = 10,
            month = Month.SEPTEMBER.toString(), imageId = R.drawable.newspaper1),
        Newspapers(id = 16, name = "Жизнь за счёт воды", isAvailable = false, number = 22,
            month = Month.AUGUST.toString(), imageId = R.drawable.newspaper1),
        Newspapers(id = 17, name = "Нехватка кислорода", isAvailable = true, number = 2,
            month = Month.APRIL.toString(), imageId = R.drawable.newspaper1),
        Newspapers(id = 18, name = "НАША ИГРА!!!", isAvailable = true, number = 3,
            month = Month.APRIL.toString(), imageId = R.drawable.newspaper1),
        Newspapers(id = 19, name = "Большой брат всё видит", isAvailable = false, number = 3333,
            month = Month.MAY.toString(), imageId = R.drawable.newspaper1),
        Newspapers(id = 20, name = "Жизнь без проблем", isAvailable = true, number = 4312,
            month = Month.OCTOBER.toString(), imageId = R.drawable.newspaper1),
        Newspapers(id = 21, name = "Как стать тупым", isAvailable = false, number = 654321,
            month = Month.DECEMBER.toString(), imageId = R.drawable.newspaper1),

        Books(id = 22, name = "Град обречённых", isAvailable = true, author = "Трампикус",
            pages = 47, imageId = R.drawable.book1),
        Books(id = 23, name = "Уэльс. Гольф. Мадрид.", isAvailable = false, author = "Г. Бэйл",
            pages = 77, imageId = R.drawable.book1),
        Books(id = 24, name = "Требл", isAvailable = false, author = "Х. Гвардиола",
            pages = 47777, imageId = R.drawable.book1),
        Books(id = 25, name = "ЦСКА. Клуб сердца!", isAvailable = false, author = "А.О. Мишакин",
            pages = 4757, imageId = R.drawable.book1),
        Books(id = 26, name = "Там где родился Пумба", isAvailable = true, author = "Акуна Мотата",
            pages = 47227, imageId = R.drawable.book1),
        Books(id = 27, name = "Я вернусь", isAvailable = false, author = "Терминатор",
            pages = 22, imageId = R.drawable.book1),
        Books(id = 28, name = "Как стать самым ненавистным героем аниме", isAvailable = true,
            author = "Эрен Егер", pages = 567, imageId = R.drawable.book1),

        Discs(id = 29, name = "Как казаться всем слабым, но побеждать с одного удара",
            isAvailable = true, type = "CD", imageId = R.drawable.disc1),
        Discs(id = 30, name = "Мастер меча",
            isAvailable = true, type = "DVD", imageId = R.drawable.disc1),
        Discs(id = 31, name = "Рудеус, жизнь после предательства",
            isAvailable = true, type = "CD", imageId = R.drawable.disc1),
        Discs(id = 32, name = "Как перестать быть, Годжо Саторо",
            isAvailable = false, type = "DVD", imageId = R.drawable.disc1),
        Discs(id = 33, name = "Золотое божество",
            isAvailable = false, type = "DVD", imageId = R.drawable.disc1),
        Discs(id = 34, name = "Повар брец Сома, как смотреть аниме и всегда хотеть есть",
            isAvailable = true, type = "DVD", imageId = R.drawable.disc1),
        Discs(id = 35, name = "DOTA не только для задрота",
            isAvailable = false, type = "DVD", imageId = R.drawable.disc1),
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

