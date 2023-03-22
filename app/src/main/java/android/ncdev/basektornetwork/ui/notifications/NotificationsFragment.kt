package android.ncdev.basektornetwork.ui.notifications

import android.ncdev.basektornetwork.R
import android.ncdev.basektornetwork.core.base.BaseFragment
import android.ncdev.basektornetwork.databinding.FragmentNotificationsBinding
import android.ncdev.basektornetwork.ui.notifications.adapter.SampleListAdapter
import android.ncdev.basektornetwork.utils.addItemDivider
import android.ncdev.common.utils.viewbinding.viewBinding
import android.ncdev.core_db.model.SampleModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : BaseFragment(R.layout.fragment_notifications) {

    override val binding by viewBinding(FragmentNotificationsBinding::bind)

    private val viewModel by viewModels<NotificationsViewModel>()

    private val adapter by lazy { SampleListAdapter() }

    private val name get() = binding.edtName.text.toString()
    override fun initView() {
        initRecyclerView()
        initActions()
    }

    private fun initRecyclerView() = with(binding) {
        rcvSample.layoutManager = LinearLayoutManager(requireContext())
        rcvSample.adapter = adapter
        rcvSample.addItemDivider()
    }

    private fun initActions() = with(binding) {
        imgAdd.setOnClickListener {
            addName()
        }
    }

    override fun observeViewModels() = with(viewModel) {
        sampleModelListFlow.observe {
            adapter.submitList(it.asReversed()) {
                if (it.isNotEmpty())
                    binding.rcvSample.smoothScrollToPosition(0)
            }
        }
    }

    private fun addName() {
        val sample = SampleModel(name = name)
        viewModel.insertSampleModel(sample)
    }

}