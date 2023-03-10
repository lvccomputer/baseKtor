package android.ncdev.basektornetwork.ui.home

import android.ncdev.basektornetwork.R
import android.ncdev.basektornetwork.core.base.BaseFragment
import android.ncdev.basektornetwork.databinding.FragmentHomeBinding
import android.ncdev.basektornetwork.ui.home.adapter.GirlAdapter
import android.ncdev.common.utils.viewbinding.viewBinding
import android.ncdev.girl_photo.model.GirlModelUI
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()
    private var mIsLoading = false

    @Inject
    lateinit var imageLoader: ImageLoader

    private val girlAdapter by lazy {
        fun onClicked(girlModel: GirlModelUI) {
            viewModel.setSelected(girlModel.id)
        }
        GirlAdapter(::onClicked)
    }

    override fun initView() {
        initRecyclerView()
        observeError(viewModel)
    }

    override fun observeViewModels() = with(viewModel) {
        girlPhotoListFlow.observe {
            girlAdapter.submitList(it) {
                mIsLoading = false
            }
            viewModel.limit += 60
        }

    }

    private fun initRecyclerView() = with(binding) {
        rcvGirls.adapter = girlAdapter
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)

        rcvGirls.layoutManager = gridLayoutManager
        rcvGirls.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition: Int = gridLayoutManager.findLastVisibleItemPosition()
                val totalItemCount: Int = gridLayoutManager.itemCount
                if (!mIsLoading && totalItemCount <= lastVisibleItemPosition + 50) {
                    viewModel.loadData()
                    mIsLoading = true

                }
            }
        })

    }


    companion object {
        private const val TAG = "HomeFragment"
    }
}