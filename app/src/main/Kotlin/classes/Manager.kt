package classes

import classes.library.LibraryItems
import papka.Shop

class Manager {
    fun buy(shop: Shop<LibraryItems>): LibraryItems {
        println("Менеджер покупает предмет")
        return shop.toSell()
    }
}