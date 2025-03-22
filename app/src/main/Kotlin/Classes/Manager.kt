package Classes

import Interfaces.Shop

class Manager {
    fun buy(shop: Shop): LibraryItems{
        println("Менеджер покупает предмет")
        return shop.toSell()
    }
}