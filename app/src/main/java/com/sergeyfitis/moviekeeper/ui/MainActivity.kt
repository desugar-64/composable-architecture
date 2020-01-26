package com.sergeyfitis.moviekeeper.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.sergeyfitis.moviekeeper.MovieApp
import com.sergeyfitis.moviekeeper.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = MovieApp.appFragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_bottom_bar.setupWithNavController(main_nav_host.findNavController())
    }
}
