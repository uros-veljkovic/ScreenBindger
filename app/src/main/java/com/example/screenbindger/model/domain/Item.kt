package com.example.screenbindger.model.domain

import androidx.databinding.BaseObservable
import com.example.screenbindger.model.enums.ItemType

interface Item{
    abstract fun getItemType(): ItemType
}