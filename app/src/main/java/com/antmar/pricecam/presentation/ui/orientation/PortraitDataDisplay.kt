package com.antmar.pricecam.presentation.ui.orientation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            BaseText("${priceQuantityInfo.price}", 28)

            BaseText("/", 28)

            BaseText("${priceQuantityInfo.quantity}", 28)

            BaseText("=", 28)

            BaseText("${priceQuantityInfo.pricePerUnit}", 28)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            BaseText(stringResource(R.string.price), 14)

            BaseText("", 14)

            BaseText(stringResource(R.string.quantity), 14)

            BaseText("", 14)

            BaseText(stringResource(R.string.price_per_unit), 14)
        }
    }
}

@Composable
fun BaseText(text: String, size: Int) {

    Text(
        text = text,
        fontSize = size.sp,
        style = TextStyle(color = MaterialTheme.colorScheme.primary),
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



