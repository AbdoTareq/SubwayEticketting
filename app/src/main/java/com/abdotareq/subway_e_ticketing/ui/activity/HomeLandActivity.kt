package com.abdotareq.subway_e_ticketing.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.dto.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home_land.*
import timber.log.Timber

/**
 * Class responsible for the main home opening activity that host all the app fragments & contents
 * */

class HomeLandActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_land)

        // this for icon background tint to be empty(just lines make image)
        bottom_nav_view.itemIconTintList = null

        // Set up NavHostFragment to navigate through bottom navigation
        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        // Set up NavigationController
        val navController = host.navController
        setupBottomNavMenu(navController)


    }

    // method to navigate through bottom navigation view
    // it navigates to the right item through id in every item in bottom_nav_menu
    private fun setupBottomNavMenu(navController: NavController) {
        // TODO STEP 9.3 - Use NavigationUI to set up Bottom Nav
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }

}
