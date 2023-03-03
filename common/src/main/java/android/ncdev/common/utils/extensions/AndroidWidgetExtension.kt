package android.ncdev.common.utils.extensions

import android.text.InputFilter
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

fun TextView.setTextColorRes(@ColorRes id: Int) {
    setTextColor(ContextCompat.getColor(context, id))
}
fun TextView.setErrorFocus(error:String) {
    setError(error)
    requestFocus()
}
fun TextView.clearErrorFocus(){
    error = null
    requestFocus()
}
fun EditText.addFilterMaxDigit(digits: Int) {
    filters = arrayOf(
        InputFilter { charSequence, start, end, spanned, dstart, dend ->
            val text = spanned.toString().substring(0, dstart) + charSequence + spanned.toString()
                .substring(dend)
            if (text.matches(Regex("^[0-9]+([.][0-9]{0,$digits})?$"))) {
                return@InputFilter null
            }
            ""
        }
    )
}

fun RecyclerView.ViewHolder.getString(@StringRes id:Int) = itemView.context.getString(id)
