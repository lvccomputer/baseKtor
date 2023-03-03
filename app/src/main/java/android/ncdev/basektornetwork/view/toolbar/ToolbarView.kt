package android.ncdev.basektornetwork.view.toolbar

import android.content.Context
import android.graphics.PorterDuff
import android.ncdev.basektornetwork.R
import android.ncdev.basektornetwork.core.toolbar.ToolbarConfiguration
import android.ncdev.basektornetwork.databinding.ViewToolbarBinding
import android.ncdev.basektornetwork.utils.getColorRes
import android.ncdev.common.utils.viewbinding.viewBinding
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class ToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val binding = viewBinding(ViewToolbarBinding::inflate)
    val btnBack get() = binding.btnBack

    fun configure(config: ToolbarConfiguration) {
        btnBack.apply {
            setImageResource(R.drawable.ic_arrow_left)
            config.colorTint?.let {
                setColorFilter(context.getColorRes(it), PorterDuff.Mode.SRC_IN)
            }
        }
        binding.buttonContainer.removeAllViews()
        binding.textContainer.removeAllViews()
        configure(config.titleResId?.run { resources.getString(this) } ?: "",
            config.colorTint,
            config.onBackClicked)
    }

    fun configure(title: String, titleColor: Int? = null, onBackClicked: (() -> Unit)?) {
        btnBack.setImageResource(R.drawable.ic_arrow_left)
        binding.buttonContainer.removeAllViews()
        binding.textContainer.removeAllViews()
        isVisible = true
        binding.tvToolbar.text = title
        titleColor?.let {
            binding.tvToolbar.setTextColor(
                ContextCompat.getColor(context, it)
            )
        }
        binding.btnBack.setOnClickListener { onBackClicked?.invoke() }
    }

    fun addButtonToEnd(
        @DrawableRes imageRes: Int,
        @ColorRes colorTint: Int,
        onBackClicked: (() -> Unit)
    ) {
        binding.buttonContainer.addButton(imageRes, colorTint, onBackClicked)
    }

    fun addTextToEnd(
        @StringRes stringRes: Int,
        @ColorRes colorTint: Int,
        onBackClicked: (() -> Unit)
    ) {
        binding.textContainer.addText(stringRes, colorTint, onBackClicked)
    }

}
