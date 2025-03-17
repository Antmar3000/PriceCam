package com.antmar.pricecam.presentation.ui.orientation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.antmar.pricecam.R
import com.antmar.pricecam.presentation.viewmodels.MainViewModel

@Composable
fun LandscapeDataDisplay(viewModel: MainViewModel) {

    val priceQuantityInfo = viewModel.priceQuantityResult.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(120.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BaseText(priceQuantityInfo.price.toString(), 28, priceQuantityInfo.quantityInfo.suffix)

            BaseText(stringResource(R.string.price), 14, 0)
        }

        BaseText("÷", 20, 0)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BaseText(priceQuantityInfo.quantityInfo.quantity.toString(), 28, priceQuantityInfo.quantityInfo.suffix)

            BaseText(stringResource(R.string.quantity), 14, 0)
        }

        BaseText("=", 20, 0)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BaseText(priceQuantityInfo.pricePerUnit.toString(), 28, priceQuantityInfo.quantityInfo.suffix)

            BaseText(stringResource(R.string.price_per_unit), 14, 0)
        }
    }
}

@Composable
fun LandscapeDataDisplayNew (viewModel: MainViewModel) {

    val priceQuantityInfo = viewModel.priceQuantityResult.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(120.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(0.4f))

        Column(modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround) {
            BaseText("${priceQuantityInfo.quantityInfo.quantity}", 24, priceQuantityInfo.quantityInfo.suffix)

            BaseText(stringResource(R.string.quantity), 14, 0)
        }

        BaseText("÷", 20, 0)

        Column(modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround) {
            BaseText("${priceQuantityInfo.price}", 28, 0)

            BaseText(stringResource(R.string.price), 14, 0)
        }

        BaseText("=", 20, 0)

        Column(modifier = Modifier
            .fillMaxWidth()
            .weight(3.2f)
            .background(MaterialTheme.colorScheme.surface)
            .border(2.dp, MaterialTheme.colorScheme.tertiary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround) {
            BaseText(priceQuantityInfo.pricePerUnit.toString(), 36, 0)

            BaseText(stringResource(R.string.price_per_unit), 14, 0)
        }
    }
}