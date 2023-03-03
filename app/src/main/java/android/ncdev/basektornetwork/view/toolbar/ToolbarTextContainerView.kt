package android.ncdev.basektornetwork.view.toolbar

import android.content.Context
import android.ncdev.basektornetwork.R
import android.ncdev.basektornetwork.utils.getColorRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.view.marginBottom
import androidx.core.view.marginStart
import androidx.core.view.marginTop

class ToolbarTextContainerView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : LinearLayout(context, attributeSet) {
    private lateinit var buttons: View

    fun addText(
        @StringRes stringRes: Int,
        @ColorRes colorTint: Int,
        onBackClicked: (() -> Unit)
    ) {
        getInflatedText().apply {
            id = generateViewId()
            setOnClickListener { onBackClicked.invoke() }
            addEndMargin(this)
            (this as TextView).setTextColor(context.getColorRes(colorTint))
            this.setText(stringRes)
        }.also {
            addView(it)
        }
    }

    private fun addEndMargin(button: View) {
        (button.layoutParams as? LayoutParams)?.setMargins(
            button.marginStart, button.marginTop, 0, button.marginBottom
        )
    }
    private fun getInflatedText(): View {
        buttons = LayoutInflater.from(context).inflate(R.layout.custom_toolbar_text, this, false)
        return buttons
    }
}
