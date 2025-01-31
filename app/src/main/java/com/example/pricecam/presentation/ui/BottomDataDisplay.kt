package com.example.pricecam.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.pricecam.R
import com.example.pricecam.presentation.viewmodels.MainViewModel

@Composable
fun BottomDataDisplay(viewModel : MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                BaseText("${viewModel.detectedPriceQuantity.value.price}", 28)

                BaseText("/", 28)

                BaseText("${viewModel.detectedPriceQuantity.value.quantity}", 28)

                BaseText("=", 28)

                BaseText("${viewModel.detectedPriceQuantity.value.pricePerUnit}", 28)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                BaseText( stringResource(R.string.price), 14)

                BaseText("", 14)

                BaseText( stringResource(R.string.quantity), 14)

                BaseText("", 14)

                BaseText( stringResource(R.string.price_per_unit), 14)
            }
        }
}

@Composable
fun BaseText (text : String, size : Int) {
    Text(
        text = text,
        fontSize = size.sp,
        style = TextStyle(color = MaterialTheme.colorScheme.primary),
    )
}


