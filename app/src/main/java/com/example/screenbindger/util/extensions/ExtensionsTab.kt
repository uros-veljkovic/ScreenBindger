package com.example.screenbindger.util.extensions

import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.show_tabs.view.*

fun TabLayout.onTabSelected(callback: (Int) -> Unit) {
    addOnTabSelectedListener(object :
        TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {}
        override fun onTabUnselected(tab: TabLayout.Tab?) {}
        override fun onTabSelected(tab: TabLayout.Tab?) {
            callback(tab!!.position)
        }
    })
}

