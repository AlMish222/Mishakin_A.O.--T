package firstDZ

import domain.model.LibraryItems

class Manager {
    fun buy(shop: Shop<LibraryItems>): LibraryItems {
        println("Менеджер покупает предмет")
        return shop.toSell()
    }
}