import classes.LibraryItems
import classes.Books
import classes.Newspapers
import classes.Discs
import interfaces.TakeHome
import interfaces.ReadInLibrary
import interfaces.ReturnItem

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