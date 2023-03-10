package android.ncdev.common.utils.extensions

import android.app.Activity
import android.content.Context
import android.ncdev.common.R
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey

object GlideApp {
    fun with(context: Context): RequestManager {
        return Glide.with(context).expire(86400)
    }
}

fun RequestManager.placeholder(@DrawableRes image: Int): RequestManager {
    return this.applyDefaultRequestOptions(
        RequestOptions()
            .placeholder(image)
            .error(image)
    )
}

fun RequestManager.expire(seconds: Int): RequestManager {
    return this.applyDefaultRequestOptions(
        RequestOptions()
            .signature(ObjectKey(System.currentTimeMillis() / 1000 / seconds))
    )
}

fun ImageView.setImageResDrawable(@DrawableRes image: Int) {
    if ((context as? Activity)?.isDestroyed == true) return
    GlideApp.with(context).load(image).into(this)
}

fun ImageView.setImageUri(uri: Uri? = null) {
    if (uri == null) {
        setImageResource(R.drawable.place_holder)//default icon user.
    } else {
        GlideApp.with(context).load(uri).error(R.drawable.place_holder).into(this)//default icon user.
    }
}

fun ImageView.setImageUrl(
    url: String?,
    @DrawableRes placeholder: Int? = null,
) {
    url?.takeIf { it.isNotEmpty() } ?: run {
        placeholder?.let { setImageResource(it) }
        return
    }
    if ((context as? Activity)?.isDestroyed == true) return
    GlideApp.with(context).apply {
        val image = placeholder ?: return@apply
        placeholder(image)
    }.load(url).thumbnail(0.5f).into(this)
}