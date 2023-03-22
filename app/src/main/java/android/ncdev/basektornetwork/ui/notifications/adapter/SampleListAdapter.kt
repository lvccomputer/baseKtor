package android.ncdev.basektornetwork.ui.notifications.adapter

import android.ncdev.basektornetwork.R
import android.ncdev.basektornetwork.databinding.ItemListSampleBinding
import android.ncdev.common.utils.extensions.setImageResDrawable
import android.ncdev.core_db.model.SampleModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SampleListAdapter : ListAdapter<SampleModel, SampleListAdapter.ItemVH>(SampleDiffCallback) {
    class ItemVH(private val binding: ItemListSampleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SampleModel) = with(binding) {
            imgLogo.setImageResDrawable(R.drawable.test)
            tvTitle.text = item.name
            tvDescription.text = item.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        return ItemListSampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .run {
                ItemVH(this)
            }
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val SampleDiffCallback = object : DiffUtil.ItemCallback<SampleModel>() {
            override fun areItemsTheSame(oldItem: SampleModel, newItem: SampleModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SampleModel, newItem: SampleModel): Boolean {
                return oldItem.name == newItem.name
            }

        }
    }
}