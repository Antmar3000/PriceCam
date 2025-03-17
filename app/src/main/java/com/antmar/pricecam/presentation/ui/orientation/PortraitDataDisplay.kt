package com.antmar.pricecam.presentation.ui.orientation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.antmar.pricecam.R
import com.antmar.pricecam.presentation.viewmodels.MainViewModel

@Composable
fun PortraitDataDisplay(viewModel: MainViewModel) {

    val priceQuantityInfo = viewModel.priceQuantityResult.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            BaseText("${priceQuantityInfo.price}", 28, priceQuantityInfo.quantityInfo.suffix)

            BaseText("/", 28, priceQuantityInfo.quantityInfo.suffix)

            BaseText("${priceQuantityInfo.quantityInfo.quantity}", 28, priceQuantityInfo.quantityInfo.suffix)

            BaseText("=", 28, priceQuantityInfo.quantityInfo.suffix)

            BaseText("${priceQuantityInfo.pricePerUnit}", 28, priceQuantityInfo.quantityInfo.suffix)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            BaseText(stringResource(R.string.price), 14, 0)

            BaseText("", 14, 0)

            BaseText(stringResource(R.string.quantity), 14, 0)

            BaseText("", 14, 0)

            BaseText(stringResource(R.string.price_per_unit), 14, 0)
        }
    }
}

@Composable
fun BaseText(text: String, size: Int, suffix : Int) {

    Text(
        text = "$text " + when(suffix) {
            0 -> ""
            1 -> stringResource(R.string.kg)
            2 -> stringResource(R.string.liter)
            3 -> stringResource(R.string.liter)
            4 -> stringResource(R.string.kg)
            else -> ""
        },
        fontSize = size.sp,
        style = TextStyle(color = MaterialTheme.colorScheme.primary),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun PreviewScannedText(viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        text = viewModel.priceQuantityResult.collectAsState().value.text,
    )
}

@Composable
fun PortraitDataDisplayNew (viewModel: MainViewModel) {

    val priceQuantityInfo = viewModel.priceQuantityResult.collectAsStateWithLifecycle().value

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row (modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column (modifier = Modifier.weight(1.8f),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.3f))

                BaseText("${priceQuantityInfo.quantityInfo.quantity}", 22, priceQuantityInfo.quantityInfo.suffix)

                BaseText(stringResource(R.string.quantity), 14, 0)

                Spacer(modifier = Modifier.weight(2f))
            }

            Column (modifier = Modifier.weight(0.4f),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                BaseText("/", 30, 0)

                Spacer(modifier = Modifier.weight(1f))
            }

            Column (modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(2f))

                BaseText("${priceQuantityInfo.price.toInt()}", 28, 0)

                BaseText(stringResource(R.string.price), 14, 0)

                Spacer(modifier = Modifier.weight(0.3f))
            }

            Column (
                modifier = Modifier.weight(0.8f).fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BaseText("=", 24, 0)
            }

            Column (modifier = Modifier
                .fillMaxHeight()
                .weight(3f)
                .background(MaterialTheme.colorScheme.surface)
                .border(2.dp, MaterialTheme.colorScheme.tertiary),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${priceQuantityInfo.pricePerUnit}",
                    fontSize = 40.sp,
                    style = TextStyle(color = MaterialTheme.colorScheme.primary),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                BaseText(stringResource(R.string.price_per_unit), 16, 0)

            }
        }
    }
}



