package Classes

import Interfaces.Shop

class BookStore : Shop {
    override fun toSell(): Books {
        println("Купил книгу 'Метро 2033'")
        return Books(id = 1122, name = "Метро 2033", isAvailable = true, author = "Дмитрий Глуховский", pages = 407)
    }
}