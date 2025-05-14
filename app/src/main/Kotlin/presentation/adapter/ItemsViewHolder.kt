package presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import domain.model.Books
import domain.model.LibraryItems
import com.example.dz_1.R
import com.example.dz_1.databinding.LibraryItemsBinding

class ItemsViewHolder(
    private val binding: LibraryItemsBinding,
): RecyclerView.ViewHolder(binding.root) {

    fun bind(libraryItems: LibraryItems) = with(binding) {
        im.setImageResource(libraryItems.imageId)
        tvTtile.text = itemView.context.getString(R.string.titleName, libraryItems.name)
        itemId.text = itemView.context.getString(R.string.prItemId, libraryItems.id)

        if (libraryItems is Books) {
            tvAuthor.text = itemView.context.getString(R.string.book_author, libraryItems.author)
            tvPages.text = itemView.context.getString(R.string.count_page, libraryItems.pages)
        } else {
            tvAuthor.text = ""
            tvPages.text = ""
        }
    }
}