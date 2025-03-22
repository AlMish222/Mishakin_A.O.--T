package Classes

import Interfaces.Shop

class NewspaperStall : Shop {
    override fun toSell(): Newspapers {
        println("Купил газету 'Труд'")
        return Newspapers(id = 833, name = "Труд", isAvailable = true, number = 1235, month = "Сентяборьский")
    }
}