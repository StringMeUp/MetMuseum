package com.sr.metmuseum.ui.detail

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginDecorator(private val margin: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = margin
        outRect.right = margin
        outRect.bottom = margin
        outRect.top = if (parent.getChildLayoutPosition(view) == 0) margin else 0
    }
}