package com.sergeyfitis.moviekeeper.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.sergeyfitis.moviekeeper.ui.MainActivity

class AppActivityLifecycleCallbacks(
    private val onMainActivityCreated: (MainActivity) -> Unit,
    private val onMainActivityDestroyed: (MainActivity) -> Unit
) : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity is MainActivity) {
            onMainActivityDestroyed.invoke(activity)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is MainActivity) {
            onMainActivityCreated.invoke(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {

    }
}