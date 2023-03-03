package android.ncdev.basektornetwork.view.toolbar

import android.content.Context
import android.graphics.PorterDuff
import android.ncdev.basektornetwork.R
import android.ncdev.basektornetwork.utils.getColorRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.view.marginBottom
import androidx.core.view.marginStart
import androidx.core.view.marginTop

class ToolbarButtonContainerView(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {
    private lateinit var buttons: View

    fun addButton(
        @DrawableRes imageRes: Int,
        @ColorRes colorTint: Int,
        onBackClicked: (() -> Unit)
    ) {
        getInflatedButton().apply {
            id = generateViewId()
            setOnClickListener { onBackClicked.invoke() }
            addEndMargin(this)
            (this as ImageView).setColorFilter(context.getColorRes(colorTint), PorterDuff.Mode.SRC_IN)
            this.setImageResource(imageRes)
        }.also {
            addView(it)
        }
    }

    private fun addEndMargin(button: View) {
        (button.layoutParams as? LayoutParams)?.setMargins(
            button.marginStart, button.marginTop, 0, button.marginBottom
        )
    }

    private fun getInflatedButton(): View {
        buttons = LayoutInflater.from(context).inflate(R.layout.custom_toolbar_button, this, false)
        return buttons
    }

}
