package android.ncdev.basektornetwork.core.toolbar

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

data class ToolbarConfiguration(
    @StringRes val titleResId: Int? = null,
    @ColorRes val colorTint: Int? = null,
    val onBackClicked: (() -> Unit)? = null,
)
