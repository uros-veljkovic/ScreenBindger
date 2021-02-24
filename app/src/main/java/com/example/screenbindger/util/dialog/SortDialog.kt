package com.example.screenbindger.util.dialog

import android.content.Context
import android.util.ArrayMap
import android.widget.ArrayAdapter
import com.example.screenbindger.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.ref.WeakReference

class SortDialog(
    val context: WeakReference<Context>
) {
    private val sorts = SortBy.map.values.map { it.name }.toTypedArray()

    fun showDialog(callback: (SortBy) -> Unit) {
        var checked = 0

        context.get()!!.apply {
            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.sort_by))
                .setNeutralButton(resources.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.ok)) { dialog, itemPosition ->
                    callback(SortBy.map[checked]!!)
                }.setSingleChoiceItems(sorts, checked) { _, itemPosition ->
                    checked = itemPosition
                }
                .show()
        }
    }
}

const val RATING_ASC = "Rating Asc"
const val RATING_DESC = "Rating Desc"
const val TITLE_ASC = "Title Asc"
const val TITLE_DESC = "Title Desc"

sealed class SortBy(val name: String) {

    companion object {
        val map = mapOf(
            0 to RatingAsc,
            1 to RatingDesc,
            2 to TitleAsc,
            3 to TitleDesc
        )
    }

    object RatingAsc : SortBy(RATING_ASC)
    object RatingDesc : SortBy(RATING_DESC)
    object TitleAsc : SortBy(TITLE_ASC)
    object TitleDesc : SortBy(TITLE_DESC)

}