package com.example.pricecam.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.unit.dp
import com.example.pricecam.presentation.viewmodels.MainViewModel

@Composable
fun TextHighlightOverlay (viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()){

        Canvas(modifier = Modifier.fillMaxSize()) {
            viewModel.detectedPriceQuantity.value.priceBoundingBox.apply {
                if (this != null) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = this.toComposeRect().topLeft,
                        size = this.toComposeRect().size,
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
            }
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            viewModel.detectedPriceQuantity.value.quantityBoundingBox.apply {
                if (this != null) {
                    drawRect(
                        color = Color.Red,
                        topLeft = this.toComposeRect().topLeft,
                        size = this.toComposeRect().size,
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
            }
        }
}
