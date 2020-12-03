package es.i12capea.rickypedia.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickypedia.R
import es.i12capea.rickypedia.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navController: NavController by lazy { this.findNavController(R.id.fragment_nav_host) }

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.bottomNavigation.setupWithNavController(this.navController)

        binding.bottomNavigation.setOnNavigationItemReselectedListener {

        }
        binding.bottomNavigation.itemIconTintList = null;

        this.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.characterListFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
                R.id.locationListFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE

                }
                R.id.episodeListFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }

                else -> binding.bottomNavigation.visibility = View.GONE
            }
        }
    }
    
}
