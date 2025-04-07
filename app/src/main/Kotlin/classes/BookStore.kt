package classes

import classes.library.Books
import com.example.dz_1.R
import papka.Shop

class BookStore : Shop<Books> {
    override fun toSell(): Books {
        println("Купил книгу 'Метро 2033'")
        return Books(
            id = 1122,
            name = "Метро 2033",
            isAvailable = true,
            author = "Дмитрий Глуховский",
            pages = 407,
            imageId = R.drawable.book1
        )
    }
}