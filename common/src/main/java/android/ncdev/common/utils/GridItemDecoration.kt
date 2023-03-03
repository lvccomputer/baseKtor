package android.ncdev.common.utils

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class GridItemDecoration(
    /**
     * Evenly spaced between item.
     */
    private val spacing: Int,
    /**
     * column of recyclerview.
     */
    private val column: Int
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position: Int = parent.getChildAdapterPosition(view) // item position

        val column: Int = position % this.column // item column

        outRect.left = column * spacing / this.column // column * ((1f / spanCount) * spacing)
        outRect.right =
            spacing - (column + 1) * spacing / this.column // spacing - (column + 1) * ((1f /    spanCount) * spacing)
        if (position >= this.column) {
            outRect.top = spacing // item top
        }
    }

}

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean,
    private val headerNum: Int,
    private val isReverse: Boolean
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) - headerNum

        if (position >= 0) {
            val column = position % spanCount

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 1) * spacing / spanCount

                if (position < spanCount) {
                    if (isReverse)
                        outRect.bottom = spacing else
                        outRect.top = spacing
                }

                if (isReverse)
                    outRect.top = spacing else
                    outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount

                if (position >= spanCount) {
                    if (isReverse)
                        outRect.bottom = spacing else
                        outRect.top = spacing
                }
            }
        } else {
            outRect.left = 0
            outRect.right = 0
            outRect.top = 0
            outRect.bottom = 0
        }
    }

}

class GridItemDecorations(
    /**
     * Evenly spaced between item.
     */
    private val spacing: Int,
    /**
     * column of recyclerview.
     */
    private val column: Int,
    private val isPaddingLeft: Boolean = false,
    private val isPaddingRight: Boolean = false
) :
    ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val params = view.layoutParams as GridLayoutManager.LayoutParams
        val spanIndex = params.spanIndex
        val spanSize = params.spanSize
        // If it is in column 0 you apply the full offset on the start side, else only half
        if (spanIndex == 0) {
            if (!isPaddingLeft)
                outRect.left = 0
            else outRect.left = spacing
        } else {
            outRect.left = spacing / 2
        }

        // If spanIndex + spanSize equals spanCount (it occupies the last column) you apply the full offset on the end, else only half.
        if (spanIndex + spanSize == column) {
            if (!isPaddingRight)
                outRect.right = 0
            else outRect.right = spacing
        } else {
            outRect.right = spacing / 2
        }


        // just add some vertical padding as well
        outRect.top = spacing / 2
        outRect.bottom = spacing / 2
        /**
         * nếu type 1 item nằm ngang thì bỏ hết khoảng cách
         */
        if (spanSize == 2) {
            outRect.left = 0
            outRect.right = 0
            outRect.top = 0
        }
        if (isLayoutRTL(parent)) {
            val tmp = outRect.left
            outRect.left = outRect.right
            outRect.right = tmp
        }
    }

    companion object {
        @SuppressLint("NewApi", "WrongConstant")
        private fun isLayoutRTL(parent: RecyclerView): Boolean {
            return parent.layoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL
        }
    }
}