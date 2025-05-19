package firstDZ

import domain.model.Month
import domain.model.Newspapers
import com.example.dz_1.R

class NewspaperStall : Shop<Newspapers> {
    override fun toSell(): Newspapers {
        println("Купил газету 'Труд'")
        return Newspapers(
            id = 833,
            name = "Труд",
            isAvailable = true,
            number = 1235,
            month = Month.SEPTEMBER.toString(),
            imageId = R.drawable.newspaper1
        )
    }
}