package android.ncdev.basektornetwork.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EndlessRecyclerViewScrollListener(private val layoutManager: StaggeredGridLayoutManager) :
    RecyclerView.OnScrollListener() {
 
    private var lastVisibleItemPositions: IntArray? = null
    private var totalItemCount = 0
    private var loading = false
    private val visibleThreshold = 30
    private var currentPage = 0
 
    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(view, dx, dy)
 
        // Get the last visible item positions
        lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)
        // Get the total item count
        totalItemCount = layoutManager.itemCount
 
        // Determine if we need to load more data
        if (!loading && totalItemCount <= lastVisibleItemPositions!![0] + visibleThreshold) {
            // End has been reached
            currentPage++
            onLoadMore(currentPage)
            loading = true
        }
    }
 
    fun setLoading(loading: Boolean) {
        this.loading = loading
    }
 
    abstract fun onLoadMore(currentPage: Int)
}