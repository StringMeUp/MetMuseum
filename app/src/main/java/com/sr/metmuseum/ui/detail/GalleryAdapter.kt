package com.sr.metmuseum.ui.detail

import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sr.metmuseum.databinding.MainGalleryItemBinding
import com.sr.metmuseum.databinding.ThumbItemBinding
import com.sr.metmuseum.util.Constants
import com.sr.metmuseum.util.getImage

val galleryDiffUtil = object : DiffUtil.ItemCallback<GalleryItem>() {
    override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return oldItem.objectId == newItem.objectId
    }

    override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return oldItem == newItem
    }
}

class GalleryAdapter: ListAdapter<GalleryItem, RecyclerView.ViewHolder>(galleryDiffUtil) {
    class GalleryViewHolder(private val binding: MainGalleryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GalleryItem) {
            binding.apply {
                getImage(item)
            }
        }
    }

    class ThumbViewHolder(private val binding: ThumbItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GalleryItem) {
          binding.apply {
              getImage(item)
          }
        }
    }

    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            DetailViewModel.GalleryType.MAIN.ordinal -> { GalleryViewHolder(MainGalleryItemBinding.inflate(inflater, parent, false)) }
            DetailViewModel.GalleryType.THUMB.ordinal -> { ThumbViewHolder(ThumbItemBinding.inflate(inflater, parent, false)) }
            else -> { throw IllegalArgumentException("Invalid view type.") }
       }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       when (holder) {
              is GalleryViewHolder -> { holder.bind(getItem(position)) }
              is ThumbViewHolder -> { holder.bind(getItem(position)) }
              else -> { throw IllegalArgumentException("Unknown view holder.") }
       }
    }

    override fun getItemViewType(position: Int) = getItem(position).type.ordinal
}