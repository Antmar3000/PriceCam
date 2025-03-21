package com.antmar.pricecam.domain.entity


data class PriceTag (
    val price : Float,
    val quantityInfo : QuantityInfo,
    val pricePerUnit : Float,
    val text : String
)

data class QuantityInfo (
    val quantity: Float,
    val suffix : Int
)