package presentation.view

import presentation.viewmodel.DetailedInformationFragment
import presentation.viewmodel.ListOfItemsFragment
import presentation.viewmodel.MainViewModel
import presentation.viewmodel.MainViewModelFactory
import RetrofitInstance
import SettingsManager
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import domain.model.LibraryItems
import com.example.dz_1.R
import com.example.dz_1.databinding.ActivityMainBinding
import data.local.Library_DB
import data.repository.LibraryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            LibraryRepository(
                Library_DB.getDb(this).getDao(),
                SettingsManager(this),
                RetrofitInstance.googleBooksApi
            )
        )
    }

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
            viewModel.isLoading.collect { isLoading ->
                if (isLoading) {
                    binding.shimmerContainer.visibility = View.VISIBLE
                    binding.shimmerContainer.startShimmer()

                    delay(2000)

                    binding.fragmentContainer.visibility = View.GONE
                    binding.emptyText?.visibility = View.GONE

                } else {
                    delay(1200)

                    binding.shimmerContainer.stopShimmer()
                    binding.shimmerContainer.visibility = View.GONE

                    val items = viewModel.libraryItems.value

                    if (items.isEmpty()) {
                        binding.emptyText?.visibility = View.VISIBLE
                        binding.fragmentContainer.visibility = View.GONE
                    } else {
                        binding.emptyText?.visibility = View.GONE
                        binding.fragmentContainer.visibility = View.VISIBLE
                    }
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
                handleInformationFragmentInvisible()
            } else {
                binding.informationFragmentContainer?.visibility = View.VISIBLE
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

        lifecycleScope.launch {
            viewModel.libraryItems.collect { items ->
                Log.d("presentation.view.MainActivity", "Получено элементов из базы: ${items.size}")
            }
        }
    }

    private fun handleInformationFragmentInvisible() {
        if (isPortrait) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        } else {
            if (binding.informationFragmentContainer?.isVisible == true) {
                binding.informationFragmentContainer?.visibility = View.INVISIBLE
            } else {
                finish()
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
                .replace(
                    R.id.fragment_container,
                    DetailedInformationFragment.newInstance(null, true, viewModel.addItemType.value)
                )
                .addToBackStack(null).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(
                R.id.information_fragment_container, DetailedInformationFragment.newInstance(
                    null,
                    true, viewModel.addItemType.value
                )
            ).commit()
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
