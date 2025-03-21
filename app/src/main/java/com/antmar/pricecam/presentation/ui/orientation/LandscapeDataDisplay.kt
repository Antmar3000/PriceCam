package com.antmar.pricecam.presentation.ui.orientation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.antmar.pricecam.R
import com.antmar.pricecam.presentation.ui.utils.BaseText
import com.antmar.pricecam.presentation.viewmodels.MainViewModel

@Composable
fun LandscapeDataDisplay (viewModel: MainViewModel) {

    val priceQuantityInfo = viewModel.priceQuantityResult.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(140.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(0.4f))

        Column(modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround) {

            BaseText(when (priceQuantityInfo.quantityInfo.suffix) {
                0, 2, 4 -> "${priceQuantityInfo.quantityInfo.quantity}"
                1, 3 -> "${priceQuantityInfo.quantityInfo.quantity.times(1000).toInt()}"
                else -> "${priceQuantityInfo.quantityInfo.quantity}"
            }, 22, priceQuantityInfo.quantityInfo.suffix)

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
            BaseText(priceQuantityInfo.pricePerUnit.toString(), 32, 0)

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Text(text = " ")

                Text(
                    text = stringResource(R.string.price_per),
                    fontSize = 14.sp,
                    style = TextStyle(color = MaterialTheme.colorScheme.primary)
                )

                Text(
                    text = when(priceQuantityInfo.quantityInfo.suffix) {
                        0 -> "..."
                        1 -> stringResource(R.string.kg)
                        2 -> stringResource(R.string.liter)
                        3 -> stringResource(R.string.liter)
                        4 -> stringResource(R.string.kg)
                        else -> "..."
                    },
                    fontSize = 14.sp,
                    style = TextStyle(color = MaterialTheme.colorScheme.primary)
                )

                Text(text = " ")
            }
        }
    }
}