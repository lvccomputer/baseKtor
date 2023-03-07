package android.ncdev.basektornetwork.ui.home.adapter

import android.ncdev.basektornetwork.databinding.ItemListGirlBinding
import android.ncdev.common.utils.extensions.setImageUrl
import android.ncdev.core_db.model.GirlModel
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load

class GirlAdapter(private val onClicked: (GirlModel) -> Unit, private val imageLoader: ImageLoader) :
    ListAdapter<GirlModel, GirlAdapter.ItemVH>(GirlDiffCallback) {

    class ItemVH(private val binding: ItemListGirlBinding, private val imageLoader: ImageLoader) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GirlModel) {
//            binding.imgGirl.load(item.url, imageLoader)
            binding.imgGirl.setImageUrl(item.url)
            binding.name.text = item.id.toString()
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

    companion object {
        private val GirlDiffCallback = object : DiffUtil.ItemCallback<GirlModel>() {
            override fun areItemsTheSame(oldItem: GirlModel, newItem: GirlModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GirlModel, newItem: GirlModel): Boolean {
                return oldItem.url == newItem.url
            }

        }
    }
}