package com.sr.metmuseum.util

import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sr.metmuseum.databinding.MainGalleryItemBinding
import com.sr.metmuseum.databinding.ThumbItemBinding
import com.sr.metmuseum.models.ObjectDetail
import com.sr.metmuseum.ui.detail.DetailViewModel
import com.sr.metmuseum.ui.detail.GalleryItem
import timber.log.Timber

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
                    type = DetailViewModel.GalleryType.THUMB)
            })
        }
    } else {
        listOf(this)
    }.toMutableList()
}


fun MainGalleryItemBinding.getImage(item: GalleryItem){
    Glide.with(this.primaryImageView)
        .load(item.primaryImage)
        .centerCrop()
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean,
            ): Boolean {
                this@getImage.errorTextView.show()
                this@getImage.progressBar.hide()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean,
            ): Boolean {
                this@getImage.errorTextView.hide()
                this@getImage.progressBar.hide()
                return false
            }
        }).into(this@getImage.primaryImageView)
}


fun ThumbItemBinding.getImage(item: GalleryItem){
    Glide.with(this.thumbImageView)
        .load(item.primaryImage)
        .centerCrop()
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean,
            ): Boolean {
                this@getImage.progressBar.hide()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean,
            ): Boolean {
                this@getImage.progressBar.hide()
                return false
            }
        }).into(this@getImage.thumbImageView)
}