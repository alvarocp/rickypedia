package es.i12capea.rickypedia.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickypedia.R
import es.i12capea.rickypedia.presentation.common.makeStatusBarTransparent
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navController: NavController by lazy { findNavController(R.id.fragment_nav_host) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeStatusBarTransparent()

        bottom_navigation.setupWithNavController(navController)

        bottom_navigation.setOnNavigationItemReselectedListener {

        }
        bottom_navigation.itemIconTintList = null;

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.characterListFragment -> {
                    bottom_navigation.visibility = View.VISIBLE
                }
                R.id.locationListFragment -> {
                    bottom_navigation.visibility = View.VISIBLE

                }
                R.id.episodeListFragment -> {
                    bottom_navigation.visibility = View.VISIBLE
                }

                else -> bottom_navigation.visibility = View.GONE
            }
        }
    }
    
}
