package dev.passwordless.sampleapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import dev.passwordless.sampleapp.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content)
        val anonymousFragments = arrayOf(R.id.registration_fragment, R.id.login_fragment)

        if (anonymousFragments.contains(navController.previousBackStackEntry!!.destination.id) &&
            anonymousFragments.contains(navController.currentBackStackEntry!!.destination.id)
        ) {
            navController.navigateUp()
            return true
        } else if (!anonymousFragments.contains(navController.previousBackStackEntry!!.destination.id) &&
            !anonymousFragments.contains(navController.currentBackStackEntry!!.destination.id)
        ) {
            navController.navigateUp()
            return true
        } else {
            return true
        }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val navController = findNavController(R.id.nav_host_fragment_content)
            val anonymousFragments = arrayOf(R.id.registration_fragment, R.id.login_fragment)

            // Check if current and previous fragments are anonymous
            if (anonymousFragments.contains(navController.currentDestination!!.id)) {
                // Handle back press for anonymous fragments (e.g., display confirmation dialog)
                // You can also use `super.onBackPressed()` here if needed
            } else {
                // Allow default back navigation for other fragments
                navController.navigateUp()
            }
        }
    }

    override fun onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().clear().commit()
        backPressedCallback.remove()
        super.onDestroy()
    }
}
