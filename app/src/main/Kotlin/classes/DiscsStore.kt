package classes

import interfaces.Shop

class DiscsStore : Shop<Discs> {
    override fun toSell(): Discs{
        println("Купил диск 'Крёстный отец'")
        return Discs(id = 6543, name = "Крёстный отец", isAvailable = true, type = "DVD")
    }
}