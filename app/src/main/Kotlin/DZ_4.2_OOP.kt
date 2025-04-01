import classes.library.LibraryItems
import classes.library.Books
import classes.library.Newspapers
import classes.library.Discs
import classes.BookStore
import classes.DiscsStore
import classes.NewspaperStall
import classes.Manager
import classes.DigitizationCabinet
import classes.library.Month
import papka.TakeHome
import papka.ReadInLibrary
import papka.ReturnItem

fun main() {

    val libraryItems = createLibraryItems()
    val manager = Manager()
    val digCab = DigitizationCabinet<ReadInLibrary>()

    while (true) {

        printFirstMenu()
        when(readLine()?.toIntOrNull()) {
            1 -> showItems(libraryItems.filterTypes<Books>())
            2 -> showItems(libraryItems.filterTypes<Newspapers>())
            3 -> showItems(libraryItems.filterTypes<Discs>())
            4 -> shoppingAtTheLibrary(manager)
            5 -> manageDigCab(digCab)
            6 -> demonstrateFilterTypes(libraryItems)

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
        println("0. Вернуться в главное меню\n")

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
            1 -> {
                if(item is TakeHome) {
                    item.takeHome()
                } else {
                    println("Этот объект нельзя взять домой\n")
                }
            }
            2 -> {
                if (item is ReadInLibrary) {
                    item.readInLibrary()
                } else {
                    println("Этот объект нельзя читать в читальном зале\n")
                }
            }
            3 -> println(item.getAllInfo())
            4 -> {
                if (item is ReturnItem) {
                    item.returnItem()
                } else {
                    println("Этот объект нельзя вернуть\n")
                }
            }

            5 -> return true
            0 -> break
            else -> println("Неверный выбор, попробуйте снова\n")
        }
    }
    return false
}

fun shoppingAtTheLibrary(manager: Manager) {
    while (true){

        printShopMenu()
        when(readLine()?.toIntOrNull()){
            1 -> {
                val book = manager.buy(BookStore())
                println("Менеджером была куплена: ${book.getAllInfo()}")
            }
            2 -> {
                val discs = manager.buy(DiscsStore())
                println("Менеджером был куплен: ${discs.getAllInfo()}")
            }
            3 -> {
                val newspapers = manager.buy(NewspaperStall())
                println("Менеджером была куплена: ${newspapers.getAllInfo()}")
            }

            0 -> return
            else -> println("Неверный ввод, попробуйте снова\n")
        }
    }
}

fun manageDigCab(digCab : DigitizationCabinet<ReadInLibrary>) {
    while (true){

        printDigCabMenu()
        when(readLine()?.toIntOrNull()){
            1 -> {
                val book = Books(id = 231, name = "Дандадан", isAvailable = true, author = "Ториро Сакомото", pages = 11111, imageId = 1)
                val digDiscs = digCab.scanning(book)

                println("Оцифрованный объект: ${digDiscs.getAllInfo()}")
            }
            2 -> {
                val newspapers = Newspapers(id = 2331, name = "ГромаСтак", isAvailable = true, number = 2222, month = Month.NOVEMBER, imageId = 2)
                val digDiscs = digCab.scanning(newspapers)

                println("Оцифрованный объект: ${digDiscs.getAllInfo()}")
            }

            0 -> return
            else -> println("Неверный выбор, попробуйте снова\n")
        }
    }
}
