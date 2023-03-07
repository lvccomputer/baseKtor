package android.ncdev.basektornetwork

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.ncdev.core_db.realm.RealmMigrations
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration
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
        initRealm()
        Firebase.crashlytics.setUserId(Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))
    }

    private fun initRealm() {
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            .name("app.realm")
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .schemaVersion(RealmMigrations.CURRENT_VERSION.toLong())
            .migration(RealmMigrations())
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }

    //<editor-fold desc="Handler rotate & start activity">
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        runCatching {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
        }
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {
        currentActivity = WeakReference(activity as? AppCompatActivity)
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
    //</editor-fold>
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