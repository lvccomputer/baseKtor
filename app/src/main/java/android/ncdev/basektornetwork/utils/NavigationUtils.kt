package android.ncdev.basektornetwork.utils

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

fun <T> NavBackStackEntry.useSavedStateValue(key: String, handler: (T) -> Unit) {
    if (savedStateHandle.contains(key)) {
        savedStateHandle.get<T>(key)?.let { newValue ->
            handler(newValue)
        }
        savedStateHandle.remove<T>(key)
    }
}

// Used for transferring data from dialog.
fun Fragment.startSavedStateListener(fragmentId: Int, savedStateListener: NavBackStackEntry.() -> Unit) {
    val navBackStackEntry: NavBackStackEntry?
    try {
        navBackStackEntry = findNavController().getBackStackEntry(fragmentId)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // coroutine launch is added to wait for layout to be laid out.
                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                    savedStateListener(navBackStackEntry)
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)
        viewLifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }
            }
        })
    } catch (exception: Exception) {
        return
    }
}

fun <T> Fragment.setNavigationResult(key: String, value: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
}
fun <T> Fragment.setCurrentNavigationResult(key: String,value: T){
    findNavController().currentBackStackEntry?.savedStateHandle?.set(key, value)
}

fun <T> Fragment.getNavigationResultLiveData(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)


fun FragmentActivity?.getNavigationBackStackCount(): Int {
    return this?.supportFragmentManager?.primaryNavigationFragment?.childFragmentManager?.backStackEntryCount ?: 0
}

fun NavController.navigateSafe(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.let { navigate(direction) }
}

fun AppCompatActivity.addFragment(@IdRes containerID: Int, fragment: Fragment?) {
    fragment?.let {
        if (this.supportFragmentManager.findFragmentByTag(it.javaClass.simpleName) == null) {
            this.supportFragmentManager.beginTransaction()
                .addToBackStack(fragment.javaClass.simpleName)
                .add(containerID, fragment, fragment.javaClass.simpleName)
                .commitAllowingStateLoss()
        }
    }
}
fun FragmentActivity.addFragment(@IdRes containerID: Int, fragment: Fragment?) {
    fragment?.let {
        if (this.supportFragmentManager.findFragmentByTag(it.javaClass.simpleName) == null) {
            this.supportFragmentManager.beginTransaction()
                .addToBackStack(fragment.javaClass.simpleName)
                .add(containerID, fragment, fragment.javaClass.simpleName)
                .commitAllowingStateLoss()
        }
    }
}
