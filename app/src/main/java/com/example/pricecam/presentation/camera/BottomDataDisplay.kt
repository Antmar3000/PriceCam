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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.pricecam.R
import com.example.pricecam.domain.PriceTag

@Composable
fun BottomDataDisplay(detectedValue: PriceTag) {

    Column(
        modifier = Modifier
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
                text = "${detectedValue.price}",
                fontSize = 28.sp
            )
            Text(
                text = "/",
                fontSize = 28.sp
            )
            Text(
                text = "${detectedValue.quantity}",
                fontSize = 28.sp
            )
            Text(
                text = "=",
                fontSize = 28.sp
            )
            Text(
                text = "${detectedValue.pricePerUnit}",
                fontSize = 28.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = stringResource(R.string.price),
                fontSize = 14.sp
            )
            Text(
                text = "",
                fontSize = 14.sp
            )
            Text(
                text = stringResource(R.string.quantity),
                fontSize = 14.sp
            )
            Text(
                text = "",
                fontSize = 14.sp
            )
            Text(
                text = stringResource(R.string.price_per_unit),
                fontSize = 14.sp
            )

        }
    }

}