package com.example.dz_1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import classes.library.Books
import classes.library.Discs
import classes.library.LibraryItems
import classes.library.Newspapers
import com.example.dz_1.databinding.FragmentDetailsBinding


class DetailedInformationFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentDetailsBinding
    private var isNew = true
    private var items: LibraryItems? = null
    private lateinit var argItemType: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            isNew = it.getBoolean(ARG_ITEMS_IS_NEW)
            binding.tvExtra2.isGone = it.getString(ITEM_TYPE) == "Disc"
            if (!isNew) {
                items = createExistItem()
            }
        }

        if (!isNew) {
            showItems()
        } else {
            createNewItem()
        }
    }

    private fun createExistItem(): LibraryItems? {
        arguments?.let {
            val itemId = it.getInt(ITEM_ID)
            val itemName = it.getString(ITEM_NAME)!!
            val itemIsAvailable = it.getBoolean(IS_AVAILABLE)
            val im = it.getInt(ITEM_IMAGE)
            return when (it.getString(ITEM_TYPE)) {
                "Book" -> Books(
                    itemId,
                    itemName,
                    itemIsAvailable,
                    im,
                    it.getString(BOOK_AUTHOR)!!,
                    it.getInt(BOOK_PAGES)
                )
                "Newspaper" -> Newspapers(
                    itemId,
                    itemName,
                    itemIsAvailable,
                    im,
                    it.getInt(NEWSPAPER_NUMBER),
                    it.getString(NEWSPAPER_MONTH)!!
                )
                "Disc" -> Discs(
                    itemId,
                    itemName,
                    itemIsAvailable,
                    im,
                    it.getString(DISC_TYPE)!!
                )
                else -> throw IllegalArgumentException("Неизвестный тип данных")
            }
        }
        return null
    }

    private fun createNewItem() {
        argItemType = arguments?.getString(ITEM_TYPE)!!
        with(binding) {
            idEditText.hint = ITEM_ID
            isAvailableText.hint = IS_AVAILABLE
            nameEditText.hint = ITEM_NAME
            when (argItemType) {
                "Book" -> {
                    usersItem.text = BOOK_USERS_ITEM
                    im.setImageResource(R.drawable.book1)
                    tvExtra1.hint = BOOK_PAGES
                    tvExtra2.hint = BOOK_AUTHOR
                }
                "Newspaper" -> {
                    usersItem.text = NEWSPAPER_USERS_ITEM
                    im.setImageResource(R.drawable.newspaper1)
                    tvExtra1.hint = NEWSPAPER_NUMBER
                    tvExtra2.hint = NEWSPAPER_MONTH
                }
                "Disc" -> {
                    usersItem.text = DISC_USERS_ITEM
                    im.setImageResource(R.drawable.disc1)
                    tvExtra1.hint = DISC_TYPE
                }
            }
            saveButton.setOnClickListener {
                val resultItem = when (argItemType) {
                    "Book" -> Books(
                        idEditText.text.toString().toIntOrNull() ?: INDEX_DEFAULT_VALUE,
                        nameEditText.text.toString(),
                        isAvailableText.text.toString().toBoolean(),
                        R.drawable.book1,
                        tvExtra1.text.toString(),
                        tvExtra2.text.toString().toIntOrNull() ?: INDEX_DEFAULT_VALUE
                    )

                    "Newspaper" -> Newspapers(
                        idEditText.text.toString().toIntOrNull() ?: INDEX_DEFAULT_VALUE,
                        nameEditText.text.toString(),
                        isAvailableText.text.toString().toBoolean(),
                        R.drawable.newspaper1,
                        tvExtra1.text.toString().toIntOrNull() ?: INDEX_DEFAULT_VALUE,
                        tvExtra2.text.toString()
                    )

                    "Disc" -> Discs(
                        idEditText.text.toString().toIntOrNull() ?: INDEX_DEFAULT_VALUE,
                        nameEditText.text.toString(),
                        isAvailableText.text.toString().toBoolean(),
                        R.drawable.disc1,
                        tvExtra1.text.toString()
                    )
                    else -> throw IllegalArgumentException("Неизвестный тип данных")
                }
                if (argItemType != "Disc") {
                    if (isNotDiscInfEmpty()) {
                        confirmationForSaveCancel()
                    } else {
                        viewModel.addItem(resultItem)
                        viewModel.closeFragment()
                    }
                } else {
                    if (isDiscInfEmpty()) {
                        confirmationForSaveCancel()
                    } else {
                        viewModel.addItem(resultItem)
                        viewModel.closeFragment()
                    }
                }
            }
        }
    }

    private fun isNotDiscInfEmpty(): Boolean {
        with(binding) {
            return (idEditText.text.isEmpty() || nameEditText.text.isEmpty() ||
                    isAvailableText.text.isEmpty() || tvExtra1.text.isEmpty() ||
                    tvExtra2.text.isEmpty())
        }
    }

    private fun isDiscInfEmpty(): Boolean {
        with(binding) {
            return idEditText.text.isEmpty() || nameEditText.text.isEmpty() ||
                    isAvailableText.text.isEmpty() || tvExtra1.text.isEmpty()
        }
    }

    private fun confirmationForSaveCancel() {
        AlertDialog.Builder(requireContext()).setTitle("Отменить создание?")
            .setMessage("Вы заполнили не все поля, Вы уверены, что хотите отменить создание нового элемента?")
            .setPositiveButton("Да") { _, _ ->
                viewModel.makeInformationInvisible()
            }
            .setNeutralButton("Нет") { _, _ ->
                createNewItem()
            }
            .show()
    }

    @SuppressLint("SetTextI18n")
    private fun showItems() {
        with(binding) {
            setGeneralDescription()
            when (items) {
                is Books -> {
                    tvExtra1.setText("Кол-во страниц: ${(items as Books).pages}")
                    tvExtra2.setText("Автор: ${(items as Books).author}")
                }
                is Newspapers -> {
                    tvExtra1.setText("Номер издания: ${(items as Newspapers).number}")
                    tvExtra2.setText("Месяц издания: ${(items as Newspapers).month}")
                }
                is Discs -> {
                    tvExtra1.setText("Тип диска: ${(items as Discs).type}")
                    tvExtra2.isGone = true
                }
            }
            setUnavailable()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setGeneralDescription() {
        with(binding) {
            idEditText.setText("ID: ${items?.id}")
            nameEditText.setText("Название: ${items?.name}")
            usersItem.text = items?.name
            im.setImageResource(items?.imageId ?: 0)
            isAvailableText.setText("Доступность: ${items?.isAvailable}")
        }
    }

    private fun setUnavailable() {
        with(binding) {
            nameEditText.isFocusable = false
            idEditText.isFocusable = false
            isAvailableText.isFocusable = false
            tvExtra1.isFocusable = false
            tvExtra2.isFocusable = false
            saveButton.isGone = true
        }
    }

    companion object {
        private const val ARG_ITEMS_IS_NEW = "isNew"
        const val BOOK_USERS_ITEM = "Ваша книга"
        const val NEWSPAPER_USERS_ITEM = "Ваша газета"
        const val DISC_USERS_ITEM = "Ваш диск"
        const val ITEM_TYPE = "itemType"
        const val ITEM_IMAGE = "im"

        const val ITEM_ID = "Id"
        const val IS_AVAILABLE = "Доступность: \"true/false\""
        const val ITEM_NAME = "Название"

        const val BOOK_PAGES = "Кол-во страниц"
        const val BOOK_AUTHOR = "Автор"

        const val DISC_TYPE = "Тип диска"

        const val NEWSPAPER_NUMBER = "Номер издания"
        const val NEWSPAPER_MONTH = "Месяц издания"

        const val INDEX_DEFAULT_VALUE = -1

        fun newInstance(
            items: LibraryItems?,
            isNew: Boolean,
            itemType: String?
        ) = DetailedInformationFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_ITEMS_IS_NEW, isNew)
                    if (!isNew) {
                        putInt(ITEM_IMAGE, items?.imageId ?: 0)
                        putInt(ITEM_ID, items?.id ?: 0)
                        putString(ITEM_NAME, items?.name ?: "")
                        putBoolean(IS_AVAILABLE, items?.isAvailable ?: true)
                        when (items) {
                            is Books -> {
                                putString(ITEM_TYPE, "Book")
                                putInt(BOOK_PAGES, items.pages)
                                putString(BOOK_AUTHOR, items.author)
                            }
                            is Newspapers -> {
                                putString(ITEM_TYPE, "Newspaper")
                                putInt(NEWSPAPER_NUMBER, items.number)
                                putString(NEWSPAPER_MONTH, items.month)
                            }
                            is Discs -> {
                                putString(ITEM_TYPE, "Disc")
                                putString(DISC_TYPE, items.type)
                            }
                        }
                    } else {
                        putString(ITEM_TYPE, itemType)
                    }
                }
            }
    }
}