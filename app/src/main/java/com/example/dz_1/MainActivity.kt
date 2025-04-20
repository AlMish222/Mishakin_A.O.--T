package com.example.dz_1

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import classes.library.LibraryItems
import com.example.dz_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    private val viewModel: MainViewModel by viewModels()
    private var isPortrait = true
    private lateinit var backCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(binding.root)


        setFragment()
        observeViewModel()

        backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.makeInformationInvisible()
            }
        }
        onBackPressedDispatcher.addCallback(this, backCallback)
        isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    private fun setFragment() {
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ListOfItemsFragment()).commit()
        }
    }

    private fun observeViewModel() {
        viewModel.libraryItemsState.observe(this) { state ->
            when (state) {
                is MainViewModel.LibraryItemsState.SelectedItem -> {
                    state.item?.let { showDetailsFragment(it) }
                }
                else -> Unit
            }
        }

//        viewModel.informationFragmentVisibility.observe(this) { visibility ->
//            if (visibility == false) {
//                if (isPortrait) {
//                    if (supportFragmentManager.backStackEntryCount > 0) {
//                        supportFragmentManager.popBackStack()
//                    } else {
//                        finish()
//                    }
//                } else {
//                    if (binding.informationFragmentContainer?.isVisible == true) {
//                        binding.informationFragmentContainer!!.visibility = View.INVISIBLE
//                    } else {
//                        finish()
//                    }
//                }
//            } else {
//                if (binding.informationFragmentContainer?.isVisible == true) {
//                    binding.informationFragmentContainer!!.visibility = View.VISIBLE
//                }
//            }
//        }

        viewModel.uiState.observe(this) { state ->
            when (state) {
                is MainViewModel.UIState.InformationFragmentVisibility -> {
                    if (state.isVisible) {
                        if (binding.informationFragmentContainer?.isVisible == true) {
                            binding.informationFragmentContainer!!.visibility = View.VISIBLE
                        }
                    } else {
                        if (isPortrait) {
                            if (supportFragmentManager.backStackEntryCount > 0) {
                                supportFragmentManager.popBackStack()
                            } else {
                                finish()
                            }
                        } else {
                            if (binding.informationFragmentContainer?.isVisible == true) {
                                binding.informationFragmentContainer!!.visibility = View.INVISIBLE
                            } else {
                                finish()
                            }
                        }
                    }
                }
                else -> {
                    Unit
                }
            }
        }

        viewModel.uiState.observe(this) { state ->
            when (state) {
                is MainViewModel.UIState.AddNewItem -> {
                    if (state.isAdding) {
                        addNewItemFragment()
                    } else {
                        supportFragmentManager.popBackStack()
                    }
                }
                else -> Unit
            }
        }
    }

    private fun showDetailsFragment(items: LibraryItems) {
        val fragment = DetailedInformationFragment.newInstance(items, false, null)

        if (isPortrait) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null).commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.information_fragment_container, fragment).commit()
        }
    }

    private fun addNewItemFragment() {
        val addItemType = (viewModel.uiState.value as? MainViewModel.UIState.AddItemType)?.type

        if (isPortrait) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container,
                    DetailedInformationFragment.newInstance(null, true, addItemType))
                .addToBackStack(null).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(
                R.id.information_fragment_container,
                DetailedInformationFragment.newInstance(null, true, addItemType)).commit()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val isNewPortrait = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT

        if (isNewPortrait != isPortrait) {
            isPortrait = isNewPortrait
            setFragment()

            when {
                (viewModel.libraryItemsState.value as? MainViewModel.LibraryItemsState
                    .SelectedItem)?.item != null -> {
                        val selectedItem = (viewModel.libraryItemsState.value as MainViewModel
                            .LibraryItemsState.SelectedItem).item
                        if (selectedItem != null) { showDetailsFragment(selectedItem) }
                    }
                (viewModel.uiState.value as? MainViewModel.UIState.AddNewItem)?.isAdding == true
                    -> { addNewItemFragment() }
            }
        }
    }
}
