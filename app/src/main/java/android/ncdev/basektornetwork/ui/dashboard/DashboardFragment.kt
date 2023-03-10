package android.ncdev.basektornetwork.ui.dashboard

import android.ncdev.basektornetwork.R
import android.ncdev.basektornetwork.core.base.BaseFragment
import android.ncdev.basektornetwork.databinding.FragmentDashboardBinding
import android.ncdev.common.utils.extensions.formatNumber
import android.ncdev.common.utils.viewbinding.viewBinding
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels

class DashboardFragment : BaseFragment(R.layout.fragment_dashboard) {

    private val binding by viewBinding(FragmentDashboardBinding::bind)
    private val viewModel by viewModels<DashboardViewModel>()
    override fun initView() {
        binding.edtNumber.doAfterTextChanged {
            viewModel.rawNumber = it.also { !it.isNullOrBlank() }.toString()
        }

    }

    override fun observeViewModels() {
        viewModel.dotNumberFormatFlow.observe {
            binding.tvFormatComma.text = it.toBigDecimal().formatNumber()
            binding.tvFormatComma.text = it.toBigDecimal().formatNumber()
            binding.tvFormatComma.text = it.toBigDecimal().formatNumber()
        }
    }

}