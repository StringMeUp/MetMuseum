package com.sr.metmuseum.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sr.metmuseum.R
import com.sr.metmuseum.models.ObjectDetail
import com.sr.metmuseum.ui.detail.GalleryItem
import com.sr.metmuseum.ui.main.MainViewModel

fun ObjectDetail.toGalleryItems(): MutableList<GalleryItem> {
    return GalleryItem(
        objectId = objectId,
        primaryImage = primaryImage,
        additionalImages = additionalImages,
        department = department,
        title = title,
        culture = culture,
        period = period,
        artistDisplayName = artistDisplayName,
        artistBeginDate = artistBeginDate,
        artistEndDate = artistEndDate,
        artistWikidataUrl = artistWikidataUrl,
        city = city
    ).asList()
}

private fun GalleryItem.asList(): MutableList<GalleryItem> {
    return if (!this.additionalImages.isNullOrEmpty()) {
        mutableListOf(this).also {
            it.addAll(this.additionalImages.map { addImgs ->
                GalleryItem(
                    objectId = addImgs.hashCode(),
                    primaryImage = addImgs,
                    type = MainViewModel.GalleryType.THUMB)
            })
        }
    } else {
        listOf(this)
    }.toMutableList()
}

fun ViewBinding.loadImage(
    url: String,
    imageView: ImageView,
    onSuccess: () -> Unit,
    onError: () -> Unit,
) {
    Glide.with(this.root)
        .load(url)
        .centerCrop()
        .error(R.drawable.mr_grumpy)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean,
            ): Boolean {
                onSuccess.invoke()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean,
            ): Boolean {
                onError.invoke()
                return false
            }
        }).into(imageView)
}