package com.antmar.pricecam.domain

import com.google.mlkit.vision.text.Text

fun interface AnalyzeListener {
    fun onDetectedText(result : Text)
}