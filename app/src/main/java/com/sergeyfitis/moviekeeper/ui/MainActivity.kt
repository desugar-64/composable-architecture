package com.sergeyfitis.moviekeeper.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sergeyfitis.moviekeeper.MovieApp
import com.sergeyfitis.moviekeeper.R
import com.sergeyfitis.moviekeeper.ca.action.AppAction
import com.syaremych.composable_architecture.store.view

class MainActivity : AppCompatActivity() {

    val mainNavHost: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.main_nav_host)!!

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = MovieApp.appFragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainNavBar = findViewById<BottomNavigationView>(R.id.main_bottom_bar)
        mainNavBar.setupWithNavController(mainNavHost.findNavController())

        (application as MovieApp).appStore.view.send(AppAction.Genres.Load)
    }
}
