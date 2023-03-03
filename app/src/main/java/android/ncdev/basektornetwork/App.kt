package android.ncdev.basektornetwork

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.HiltAndroidApp
import java.lang.ref.WeakReference

@HiltAndroidApp
class App : Application(), Application.ActivityLifecycleCallbacks {

    var currentActivity: WeakReference<AppCompatActivity>? = null

    companion object {
        private lateinit var app: App
        fun get(): App {
            return app
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        runCatching {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
        }
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = WeakReference(activity as? AppCompatActivity)
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}

fun getAppContext(): App {
    return App.get()
}

fun getCurrentActivity(): AppCompatActivity? {
    return getAppContext().currentActivity?.get()
}

fun getCurrentContext(): Context {
    return getCurrentActivity() ?: getAppContext()
}