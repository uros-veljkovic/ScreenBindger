package com.example.screenbindger.util.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemReviewDecorator constructor(
    private val margin: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            bottom = margin
            left = margin
            right = margin
            if (parent.getChildLayoutPosition(view) == 0)
                top = margin
        }
    }
}