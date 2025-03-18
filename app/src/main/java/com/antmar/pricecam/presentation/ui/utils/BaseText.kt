package com.antmar.pricecam.presentation.ui.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.antmar.pricecam.R

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