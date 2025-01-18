package com.example.pricecam.domain

import android.graphics.Rect

data class PriceTag (
    val price : Float,
    val quantity : Float,
    val pricePerUnit : Float,
    val priceBoundingBox: Rect?,
    val quantityBoundingBox: Rect?
)

data class PriceInfo(
    val priceMatch : Float,
    val priceBoundingBox : Rect?
)

data class QuantityInfo (
    val quantityMatch : Float,
    val quantityBoundingBox : Rect?
)

