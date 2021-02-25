package com.example.screenbindger.model.domain

import com.example.screenbindger.model.enums.ItemType

interface Item{
    abstract fun getItemType(): ItemType
}