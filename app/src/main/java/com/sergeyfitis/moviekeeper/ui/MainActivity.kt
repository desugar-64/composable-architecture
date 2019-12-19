package com.sergeyfitis.moviekeeper.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sergeyfitis.moviekeeper.MovieApp
import com.sergeyfitis.moviekeeper.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = MovieApp.appFragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
