package com.example.dz_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dz_1.databinding.FragmentListBinding

class ListOfItemsFragment : Fragment(R.layout.fragment_list) {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: ItemsAdapter
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setAddButton()
        setObserver()
    }

    private fun setRecyclerView() {

        adapter = ItemsAdapter { item ->
            viewModel.selectItem(item)
        }

        binding.apply {
            rcView.layoutManager = GridLayoutManager(context, 1)
            rcView.adapter = adapter
        }

        viewModel.libraryItemsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MainViewModel.LibraryItemsState.ScrollPosition -> {
                    (binding.rcView.layoutManager as GridLayoutManager)
                        .scrollToPositionWithOffset(state.position, 0)
                }
                else -> Unit
            }
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
                        viewModel.selectNewItemType(MainViewModel.Element.Book)
                        viewModel.startAddNewItem()
                        true
                    }
                    R.id.newspapers -> {
                        viewModel.selectNewItemType(MainViewModel.Element.Newspaper)
                        viewModel.startAddNewItem()
                        true
                    }
                    R.id.discs -> {
                        viewModel.selectNewItemType(MainViewModel.Element.Disc)
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
        viewModel.libraryItemsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MainViewModel.LibraryItemsState.Items -> adapter.submitList(state.list)
                else -> Unit
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