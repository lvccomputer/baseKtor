package android.ncdev.basektornetwork.ui.home.adapter

import android.graphics.Bitmap
import android.ncdev.basektornetwork.databinding.ItemListGirlBinding
import android.ncdev.common.utils.extensions.setImageUrl
import android.ncdev.core_db.model.GirlModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition


class GirlAdapter(
    private val onClicked: (GirlModel) -> Unit,
    private val imageLoader: ImageLoader
) :
    ListAdapter<GirlModel, GirlAdapter.ItemVH>(GirlDiffCallback) {

    class ItemVH(private val binding: ItemListGirlBinding, private val imageLoader: ImageLoader) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GirlModel) {
            binding.imgGirl.setImageUrl(item.url)
        }


        fun bindSelectionState(isSelected: Boolean) {
            binding.frameSelected.isVisible = isSelected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        return ItemListGirlBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            ItemVH(this, imageLoader).apply {
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
        private val GirlDiffCallback = object : DiffUtil.ItemCallback<GirlModel>() {
            override fun areItemsTheSame(oldItem: GirlModel, newItem: GirlModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GirlModel, newItem: GirlModel): Boolean {
                return oldItem.equals(newItem)
            }

            override fun getChangePayload(oldItem: GirlModel, newItem: GirlModel): Any? {
                return if (oldItem.isSelected != newItem.isSelected) true else null
            }
        }
    }
}