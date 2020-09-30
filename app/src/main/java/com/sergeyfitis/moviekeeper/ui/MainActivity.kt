package com.sergeyfitis.moviekeeper.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sergeyfitis.moviekeeper.MovieApp
import com.sergeyfitis.moviekeeper.R
import com.sergeyfitis.moviekeeper.data.models.MoviesResponse
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.effects.loadNowPlayingEffect
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.effects.loadTopRatedEffect
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.effects.loadUpcomingEffect
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.effects.loadUpcomingEffect2
import com.syaremych.composable_architecture.store.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MainActivity : AppCompatActivity() {

    val mainNavHost: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.main_nav_host)!!

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = MovieApp.appFragmentFactory
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        val mainNavBar = findViewById<BottomNavigationView>(R.id.main_bottom_bar)
//        mainNavBar.setupWithNavController(mainNavHost.findNavController())

        lifecycle.coroutineScope.launchWhenResumed {
//            Effect.none<Int>()
//                .onCompletion { Log.d("MainActivity", "none completed") }
//                .collect { i -> Log.d("MainActivity", "i=$i") }
//            flowOf(2)
//                .onCompletion { Log.d("MainActivity", "flowOf completed") }
//                .collect { i -> Log.d("MainActivity", "i=$i") }
            val mutex = Mutex()
            Effect.merge(
                loadUpcomingEffect2 { MoviesResponse(emptyList()) },
                loadUpcomingEffect2 { MoviesResponse(emptyList()) },
                loadUpcomingEffect2 { MoviesResponse(emptyList()) },
                loadUpcomingEffect2 { MoviesResponse(emptyList()) },
                loadUpcomingEffect2 { MoviesResponse(emptyList()) },
                loadUpcomingEffect2 { MoviesResponse(emptyList()) },
                loadUpcomingEffect2 { MoviesResponse(emptyList()) },
                loadUpcomingEffect2 { MoviesResponse(emptyList()) },
                loadUpcomingEffect2 { MoviesResponse(emptyList()) },
            )
                .catch { Log.e("MainActivity", it.localizedMessage); it.printStackTrace() }
                .collect {
                    mutex.withLock {
                        Log.d("MainActivity", "action=$it")
                    }
                }
        }
    }
}
