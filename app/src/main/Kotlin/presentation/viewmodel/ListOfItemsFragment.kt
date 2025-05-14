package presentation.viewmodel

import RetrofitInstance
import SettingsManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import domain.model.Books
import domain.model.LibraryItems
import com.example.dz_1.R
import com.example.dz_1.databinding.FragmentListBinding
import com.google.android.material.snackbar.Snackbar
import data.local.Library_DB
import data.repository.LibraryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import presentation.adapter.ItemsAdapter

class ListOfItemsFragment : Fragment(R.layout.fragment_list) {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: ItemsAdapter
    private lateinit var viewModel: MainViewModel

    private var isGoogleBooksMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        val repository = LibraryRepository(
            Library_DB.getDb(requireContext()).getDao(),
            SettingsManager(requireContext()),
            RetrofitInstance.googleBooksApi
        )
        viewModel = ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        viewModel.loadBooks()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setObserver()
        setAddButton()
        setErrorHandler()

        setupModeSwitchButtons()
        setupSearchForm()
    }

    private fun setRecyclerView() {

        adapter = ItemsAdapter { item ->
            val message = item.getAllInfo()
            showItemDialog(item, message)
        }

        binding.apply {
            rcView.layoutManager = GridLayoutManager(context, 1)
            rcView.adapter = adapter

            rcView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (lastVisibleItemPosition + 3 >= totalItemCount) {
                        viewModel.loadNextPage()
                    }
                }
            })
        }

        viewModel.scrollPosition.observe(viewLifecycleOwner) { position ->
            (binding.rcView.layoutManager as GridLayoutManager)
                .scrollToPositionWithOffset(position, 0)
        }
    }

    private fun setAddButton() {
        binding.addButton.setOnClickListener {
            val popup = PopupMenu(context, it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.popup_menu_item, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.books -> {
                        viewModel.selectNewItemTypeBook()
                        viewModel.startAddNewItem()
                        true
                    }
                    R.id.newspapers -> {
                        viewModel.selectNewItemTypeNewspaper()
                        viewModel.startAddNewItem()
                        true
                    }
                    R.id.discs -> {
                        viewModel.selectNewItemTypeDisc()
                        viewModel.startAddNewItem()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewModel.libraryItems.collect { items ->
                if (!isGoogleBooksMode) {
                    withContext(Dispatchers.Main) {
                        adapter.submitList(items)
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.googleBooks.collect { books ->
                if (isGoogleBooksMode) {
                    adapter.submitList(books)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setErrorHandler() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    AlertDialog.Builder(requireContext()).setTitle(viewModel.error.value)
                        .setMessage("Попробуй ещё раз")
                        .setPositiveButton("ОК") { _, _ ->
                            if (viewModel.error.value == LOAD_ERROR) {
                                viewModel.loadInitialPage()
                            }
                        }
                        .show()
                }
            }
        }
    }

    private fun showItemDialog(item: LibraryItems, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Информация о книге")
            .setMessage(message)
            .setPositiveButton("Сохранить в библиотеку") { _, _ ->
                val existingItem = viewModel.libraryItems.value.find {
                    it.name == item.name && it is Books
                }
                if (existingItem != null) {
                    val message1 = "Эта книга уже есть в библиотеке"
                    showSnackbar(message1)
                } else {
                    viewModel.addItem(item)
                    val message2 = "Книга добавлена в библиотеку"
                    showSnackbar(message2)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showSnackbar(mes: String) {
        Snackbar.make(binding.root, mes, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupModeSwitchButtons() {
        binding.btnLibrary.setOnClickListener{
            isGoogleBooksMode = false
            viewModel.loadInitialPage()
            binding.googleBooksSearchLayout.visibility = View.GONE
        }

        binding.btnGoogleBooks.setOnClickListener{
            isGoogleBooksMode = true
            adapter.submitList(emptyList())
            binding.googleBooksSearchLayout.visibility = View.VISIBLE
        }
    }

    private fun setupSearchForm() {
        binding.btnSearch.isEnabled = false

        binding.etTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateSearchButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etAuthor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateSearchButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnSearch.setOnClickListener {
            val author = binding.etAuthor.text.toString()
            val title = binding.etTitle.text.toString()
            performSearch(author, title)
        }
    }

    private fun updateSearchButtonState() {
        val authorText = binding.etAuthor.text.toString()
        val titleText = binding.etTitle.text.toString()

        binding.btnSearch.isEnabled = authorText.length >= 3 || titleText.length >= 3
    }

    private fun performSearch(author: String, title: String) {
        if (author.length < 3 && title.length < 3) {
            showSnackbar("Введите минимум 3 символа")
            return
        }

        viewModel.searchGoogleBooks(author, title)
    }

    private fun buildSearchQuery(title: String, author: String): String {
        return when {
            title.isNotEmpty() && author.isNotEmpty() -> "intitle:$title+inauthor:$author"
            title.isNotEmpty() -> "intitle:$title"
            author.isNotEmpty() -> "inauthor:$author"
            else -> ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val position = (binding.rcView.layoutManager as GridLayoutManager)
            .findFirstVisibleItemPosition()
        viewModel.saveScrollPosition(position)
    }

    companion object {
        const val LOAD_ERROR = "Ошибка загрузки данных"
    }
}