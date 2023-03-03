package android.ncdev.common.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.Size
import android.view.WindowManager

fun Context.getDisplaySize() = Resources.getSystem().displayMetrics.run {
    Size(widthPixels, heightPixels)
}

fun Activity.disableScreenCapture() {
    window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
}

fun Activity.enableScreenCapture() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
}
