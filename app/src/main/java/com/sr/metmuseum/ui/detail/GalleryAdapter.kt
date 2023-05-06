package com.sr.metmuseum.ui.detail

import android.content.Context
import android.text.SpannedString
import android.view.LayoutInflater
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.sr.metmuseum.R
import com.sr.metmuseum.databinding.MainGalleryItemBinding
import com.sr.metmuseum.databinding.ThumbItemBinding
import com.sr.metmuseum.util.*

val galleryDiffUtil = object : DiffUtil.ItemCallback<GalleryItem>() {
    override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return oldItem.objectId == newItem.objectId
    }

    override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return oldItem == newItem
    }
}

class GalleryAdapter(val listener: OnItemClickListener) : ListAdapter<GalleryItem, RecyclerView.ViewHolder>(galleryDiffUtil) {
    abstract class BaseViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: GalleryItem)
        abstract fun onSuccess()
        abstract fun onError()
    }

    class GalleryViewHolder(private val binding: MainGalleryItemBinding) : BaseViewHolder(binding) {
        override fun bind(item: GalleryItem) {
            val context = binding.root.context
            val hColor = context.getColor(android.R.color.black)
            binding.apply {
                loadImage(item.primaryImage, primaryImageView, { onSuccess() }, { onError() })
                binding.descriptionTextView.text = getDescription(hColor, context, item)
            }
        }

        private fun getDescription(
            hColor: Int,
            context: Context,
            item: GalleryItem,
        ): SpannedString {
            val lifeSpan = if (item.artistBeginDate.validate() == "-" || item.artistEndDate.validate() == "-") context.getString(R.string.empty_label)
            else "${item.artistBeginDate.validate()} to ${item.artistEndDate.validate()}"
            return buildSpannedString {
                color(hColor) { append(context.getString(R.string.title_label)) }
                append(" ${item.title.validate()}\n")
                color(hColor) { append(context.getString(R.string.artist_label)) }
                append(" ${item.artistDisplayName.validate()}\n")
                color(hColor) { append(context.getString(R.string.lifespan_label)) }
                append(" $lifeSpan\n")
                color(hColor) { append(context.getString(R.string.department_label)) }
                append(" ${item.department.validate()}\n")
                color(hColor) { append(context.getString(R.string.culture_label)) }
                append(" ${item.culture.validate()}\n")
                color(hColor) { append(context.getString(R.string.period_label)) }
                append(" ${item.period.validate()}\n")
                color(hColor) { append(context.getString(R.string.city_label)) }
                append(" ${item.city.validate()}\n")
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

        holder.itemView.clickWithDebounce {
            val item = getItem(position)
            if (item.type != DetailViewModel.GalleryType.MAIN) {
               listener.onItemClick(position)
            }
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).type.ordinal
}

interface OnItemClickListener {
    fun onItemClick(position: Int)
}