package android.ncdev.common.glide

import android.content.Context
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.caverock.androidsvg.SVG
import android.ncdev.common.glide.svg.SvgDecoder
import android.ncdev.common.glide.svg.SvgDrawableTranscoder
import java.io.InputStream


@GlideModule
class MyAppGlideModule: AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry
            .prepend(String::class.java, InputStream::class.java, DataUrlLoader.StreamFactory())
            .register(SVG::class.java, PictureDrawable::class.java, SvgDrawableTranscoder())
            .append(InputStream::class.java, SVG::class.java, SvgDecoder())
    }
}