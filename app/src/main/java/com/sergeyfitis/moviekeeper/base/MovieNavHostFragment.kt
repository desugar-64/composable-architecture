package com.sergeyfitis.moviekeeper.base

import android.os.Bundle
import androidx.annotation.Keep
import androidx.navigation.fragment.NavHostFragment

@Keep
class MovieNavHostFragment(
    private val fragmentFactory: AppFragmentFactory
) : NavHostFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
    }
}