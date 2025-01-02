package com.example.pricecam.presentation.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun BottomDataDisplay(detectedValue: Triple<Double, Double, Double>) {

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = "${detectedValue.first}",
                fontSize = 28.sp)
            Text(
                text = "/",
                fontSize = 28.sp)
            Text(
                text = "${detectedValue.second}",
                fontSize = 28.sp)
            Text(
                text = "=",
                fontSize = 28.sp)
            Text(
                text = "${detectedValue.third}",
                fontSize = 28.sp)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = "quantity",
                fontSize = 14.sp)
            Text(
                text = "",
                fontSize = 14.sp)
            Text(
                text = "price",
                fontSize = 14.sp)
            Text(
                text = "",
                fontSize = 14.sp)
            Text(
                text = "price/unit",
                fontSize = 14.sp)

        }
    }

}