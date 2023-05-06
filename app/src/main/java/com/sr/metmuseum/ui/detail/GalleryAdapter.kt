package com.sr.metmuseum.ui.detail

import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.sr.metmuseum.databinding.MainGalleryItemBinding
import com.sr.metmuseum.databinding.ThumbItemBinding
import com.sr.metmuseum.util.hide
import com.sr.metmuseum.util.loadImage
import com.sr.metmuseum.util.show

val galleryDiffUtil = object : DiffUtil.ItemCallback<GalleryItem>() {
    override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return oldItem.objectId == newItem.objectId
    }

    override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return oldItem == newItem
    }
}

class GalleryAdapter : ListAdapter<GalleryItem, RecyclerView.ViewHolder>(galleryDiffUtil) {
    abstract class BaseViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: GalleryItem)
        abstract fun onSuccess()
        abstract fun onError()
    }

    class GalleryViewHolder(private val binding: MainGalleryItemBinding) : BaseViewHolder(binding) {
        override fun bind(item: GalleryItem) {
            binding.apply {
                loadImage(item.primaryImage, primaryImageView, { onSuccess() }, { onError() })
            }
        }

        override fun onSuccess() {
            binding.errorTextView.show()
            binding.progressBar.hide()
        }

        override fun onError() {
            binding.errorTextView.hide()
            binding.progressBar.hide()
        }
    }

    class ThumbViewHolder(private val binding: ThumbItemBinding) : BaseViewHolder(binding) {
        override fun bind(item: GalleryItem) {
            binding.apply {
                loadImage(item.primaryImage, thumbImageView, { onSuccess() }, { onError() })
            }
        }

        override fun onSuccess() {
            binding.progressBar.hide()
        }

        override fun onError() {
            binding.progressBar.hide()
        }
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int, ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            DetailViewModel.GalleryType.MAIN.ordinal -> { GalleryViewHolder(MainGalleryItemBinding.inflate(inflater, parent, false)) }
            DetailViewModel.GalleryType.THUMB.ordinal -> { ThumbViewHolder(ThumbItemBinding.inflate(inflater, parent, false)) }
            else -> {
                throw IllegalArgumentException("Invalid view type.")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GalleryViewHolder -> { holder.bind(getItem(position)) }
            is ThumbViewHolder -> { holder.bind(getItem(position)) }
            else -> {
                throw IllegalArgumentException("Unknown view holder.")
            }
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).type.ordinal
}