package dev.elshaarawy.nearby.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import dev.elshaarawy.nearby.R
import dev.elshaarawy.nearby.databinding.ActivityAppBinding
import kotlinx.coroutines.MainScope
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class AppActivity : AppCompatActivity() {
    private val viewModel: AppViewModel by viewModel()
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityAppBinding>(
            this,
            R.layout.activity_app
        )
    }
    private val toolbar by lazy { binding.toolbar }
    private val navController by lazy { findNavController(R.id.appNavHost) }
    private val appBarConfiguration by lazy { AppBarConfiguration(topLevelDestination) }
    private val topLevelDestination by lazy {
        setOf(R.id.splashFragment, R.id.homeFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setBindingVariables()
        setSupportActionBar(toolbar)
        configureNavController()
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    private fun setBindingVariables() {
        binding.apply {
            viewModel = this@AppActivity.viewModel
            lifecycleOwner = this@AppActivity
        }
    }

    private fun configureNavController() {
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, des, _ ->
            viewModel.onDestinationChange(des.id)
        }
    }
}