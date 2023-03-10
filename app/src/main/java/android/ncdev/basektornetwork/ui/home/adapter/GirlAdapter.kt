package android.ncdev.basektornetwork.ui.home.adapter

import android.ncdev.basektornetwork.databinding.ItemListGirlBinding
import android.ncdev.common.utils.extensions.setImageUrl
import android.ncdev.girl_photo.model.GirlModelUI
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class GirlAdapter(
    private val onClicked: (GirlModelUI) -> Unit
) : ListAdapter<GirlModelUI, GirlAdapter.ItemVH>(GirlDiffCallback) {

    class ItemVH(private val binding: ItemListGirlBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GirlModelUI) {
            binding.imgGirl.setImageUrl(item.url)
            bindSelectionState(item.isSelected)
        }


        fun bindSelectionState(isSelected: Boolean) {
            binding.imgChecked.isVisible = isSelected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        return ItemListGirlBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            ItemVH(this).apply {
                itemView.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onClicked.invoke(getItem(adapterPosition))
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }

    }

    override fun onBindViewHolder(
        holder: ItemVH,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (payloads[0] == true) {
                holder.bindSelectionState(getItem(position).isSelected)
            }
        }
    }

    companion object {
        private val GirlDiffCallback = object : DiffUtil.ItemCallback<GirlModelUI>() {
            override fun areItemsTheSame(oldItem: GirlModelUI, newItem: GirlModelUI): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GirlModelUI, newItem: GirlModelUI): Boolean {
                return oldItem.equals(newItem)
            }

            override fun getChangePayload(oldItem: GirlModelUI, newItem: GirlModelUI): Any? {
                return if (oldItem.isSelected != newItem.isSelected) true else null
            }
        }
    }
}