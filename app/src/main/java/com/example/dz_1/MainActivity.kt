package com.example.dz_1

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import classes.library.LibraryItems
import com.example.dz_1.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        setShimmer()

        backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.makeInformationInvisible()
            }
        }
        onBackPressedDispatcher.addCallback(this, backCallback)
        isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    private fun setFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, ListOfItemsFragment()).commit()
    }

    private fun setShimmer() {
        lifecycleScope.launch {
            viewModel.isLoading.collect {
                if (viewModel.isLoading.value) {
                    binding.shimmerContainer.visibility = View.VISIBLE
                    binding.shimmerContainer.startShimmer()
                    delay(3000)
                } else {
                    binding.shimmerContainer.stopShimmer()
                    binding.shimmerContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.selectItem.observe(this) { item ->
            item?.let { showDetailsFragment(it) }
        }

        viewModel.informationFragmentVisibility.observe(this) { visibility ->
            if (!visibility) {
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
            } else {
                if (binding.informationFragmentContainer?.isVisible == true) {
                    binding.informationFragmentContainer!!.visibility = View.VISIBLE
                }
            }
        }

        viewModel.isAddNewItem.observe(this) { isAdd ->
            if (isAdd) {
                addNewItemFragment()
            }
        }

        viewModel.isAddNewItem.observe(this) { closeFragment ->
            if (closeFragment) {
                supportFragmentManager.popBackStack()
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
        if (isPortrait) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container,
                    DetailedInformationFragment.newInstance(null, true, viewModel.addItemType.value))
                .addToBackStack(null).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id
                .information_fragment_container, DetailedInformationFragment.newInstance(null,
                true, viewModel.addItemType.value)).commit()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val isNewPortrait = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT

        if (isNewPortrait != isPortrait) {
            isPortrait = isNewPortrait
            lifecycleScope.launch {
                setFragment()
            }
            viewModel.selectItem.value?.let {
                showDetailsFragment(it)
            } ?: viewModel.isAddNewItem.value?.takeIf { it }?.let {
                addNewItemFragment()
            }
        }
    }
}
