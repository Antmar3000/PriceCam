package com.example.pricecam.presentation.ui.orientation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pricecam.R
import com.example.pricecam.presentation.viewmodels.MainViewModel

@Composable
fun LandscapeDataDisplay(viewModel: MainViewModel) {

    val priceQuantityInfo = viewModel.priceQuantityResult.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(90.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BaseText(priceQuantityInfo.price.toString(), 28)

            BaseText(stringResource(R.string.price), 14)
        }

        BaseText("÷", 20)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BaseText(priceQuantityInfo.quantity.toString(), 28)

            BaseText(stringResource(R.string.quantity), 14)
        }

        BaseText("=", 20)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BaseText(priceQuantityInfo.pricePerUnit.toString(), 28)

            BaseText(stringResource(R.string.price_per_unit), 14)
        }
    }
}