package android.ncdev.basektornetwork.ui.home

import android.ncdev.basektornetwork.R
import android.ncdev.basektornetwork.core.base.BaseFragment
import android.ncdev.basektornetwork.databinding.FragmentHomeBinding
import android.ncdev.basektornetwork.ui.home.adapter.GirlAdapter
import android.ncdev.basektornetwork.utils.EndlessRecyclerViewScrollListener
import android.ncdev.common.utils.viewbinding.viewBinding
import android.ncdev.core_db.model.GirlModel
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()
    private val visibleThreshold = 30
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
    }

    @Inject
    lateinit var imageLoader: ImageLoader
    private val girlAdapter by lazy {
        fun onClicked(girlModel: GirlModel) {

        }
        GirlAdapter(::onClicked, imageLoader)
    }

    override fun initView() {
        initRecyclerView()
        observeError(viewModel)
    }

    override fun observeViewModels() = with(viewModel) {
        girlPhotoDownloadLiveData.observeResource(onSuccess = {
            Log.e(TAG, "observeViewModels: " + it.size)
            endlessRecyclerViewScrollListener.setLoading(viewModel.isLoading)
            girlAdapter.submitList(it)
        })
    }

    private fun initRecyclerView() = with(binding) {
        rcvGirls.adapter = girlAdapter
//        val layoutManager = FlexboxLayoutManager(context)
//        layoutManager.flexDirection = FlexDirection.COLUMN_REVERSE
//        layoutManager.justifyContent = JustifyContent.FLEX_END
//        rcvGirls.layoutManager = layoutManager

        rcvGirls.layoutManager = staggeredGridLayoutManager

        rcvGirls.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    private val endlessRecyclerViewScrollListener =
        object : EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                // Load more data here
                Log.e(TAG, "onLoadMore: ", )
                viewModel.loadData()
            }
        }

    companion object {
        private const val TAG = "HomeFragment"
    }
}