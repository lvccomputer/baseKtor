package android.ncdev.basektornetwork.ui.dashboard

import android.ncdev.basektornetwork.R
import android.ncdev.basektornetwork.core.base.BaseFragment
import android.ncdev.basektornetwork.databinding.FragmentDashboardBinding
import android.ncdev.common.utils.extensions.formatNumber
import android.ncdev.common.utils.extensions.toBigDecimalOrZero
import android.ncdev.common.utils.viewbinding.viewBinding
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import java.util.*

class DashboardFragment : BaseFragment(R.layout.fragment_dashboard) {

    override val binding by viewBinding(FragmentDashboardBinding::bind)
    private val viewModel by viewModels<DashboardViewModel>()
    override fun initView() {
        binding.edtNumber.doAfterTextChanged {
            val value = it?.toString()?.ifEmpty { "0" } ?: "0"
            viewModel.rawNumber = value

        }

    }

    override fun observeViewModels() {
        viewModel.dotNumberFormatFlow.observe {
            binding.tvComma.text = it.toBigDecimalOrZero().formatNumber(Locale("vi", "VN"))

            binding.tvDot.text = it.toBigDecimalOrZero().formatNumber()

            binding.tvSuffix.text = it.toBigDecimalOrZero().formatNumber(suffix = "$")

            binding.tvPrefix.text = it.toBigDecimalOrZero().formatNumber(prefix = "$")

            binding.tvPrefixAndSuffix.text =
                it.toBigDecimalOrZero().formatNumber(prefix = "~", suffix = "$")

            binding.tvMaxFraction6.text =
                it.toBigDecimalOrZero().formatNumber(prefix = "~", suffix = "$", maxFraction = 6)

            binding.tvMaxFraction12.text =
                it.toBigDecimalOrZero().formatNumber(prefix = "~", suffix = "$", maxFraction = 12)

            binding.tvNotGroup.text =
                it.toBigDecimalOrZero()
                    .formatNumber(prefix = "~", suffix = "USD", isGroupingUsed = false)
        }
    }

}