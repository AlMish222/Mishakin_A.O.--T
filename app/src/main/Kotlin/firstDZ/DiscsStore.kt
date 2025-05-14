package firstDZ

import domain.model.Discs
import com.example.dz_1.R

class DiscsStore : Shop<Discs> {
    override fun toSell(): Discs {
        println("Купил диск 'Крёстный отец'")
        return Discs(id = 6543,
            name = "Крёстный отец",
            isAvailable = true,
            type = "DVD",
            imageId = R.drawable.disc1
        )
    }
}