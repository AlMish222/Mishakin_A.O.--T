package classes

import interfaces.Shop

class NewspaperStall : Shop<Newspapers> {
    override fun toSell(): Newspapers {
        println("Купил газету 'Труд'")
        return Newspapers(id = 833, name = "Труд", isAvailable = true, number = 1235, month = Month.SEPTEMBER)
    }
}