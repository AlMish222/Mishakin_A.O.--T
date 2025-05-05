package com.example.dz_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dz_1.databinding.FragmentListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListOfItemsFragment : Fragment(R.layout.fragment_list) {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: ItemsAdapter
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setObserver()
        setAddButton()
        setErrorHandler()
    }

    private fun setRecyclerView() {

        adapter = ItemsAdapter { item ->
            viewModel.selectItem(item)
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
                adapter.submitList(items)
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
                            if (viewModel.error.value == "Ошибка загрузки данных") {
                                viewModel.loadInitialPage()
                            }
                        }
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val position = (binding.rcView.layoutManager as GridLayoutManager)
            .findFirstVisibleItemPosition()
        viewModel.saveScrollPosition(position)
    }
}