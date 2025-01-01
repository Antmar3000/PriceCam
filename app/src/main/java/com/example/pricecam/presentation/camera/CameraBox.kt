package com.example.pricecam.presentation.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.pricecam.domain.TextRecognitionAnalyzer


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CameraBox() {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    val detectedPriceQuantity = remember { mutableStateOf(Triple(0.0, 0.0, 0.0)) }

    fun onTextUpdated(updatedData: Triple<Double, Double, Double>) {
        detectedPriceQuantity.value = updatedData
    }


    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues: PaddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.BottomCenter
        ) {

            AndroidView(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        setBackgroundColor(Color.BLACK)
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        startTextRecognition(
                            context = context,
                            lifecycleOwner = lifecycleOwner,
                            cameraController = cameraController,
                            previewView = previewView,
                            onDetectedTextUpdated = ::onTextUpdated
                        )
                    }
                })
            Text(
                text = "${detectedPriceQuantity.value.first}" + "/" +
                        "${detectedPriceQuantity.value.second}" + "=" +
                        "${detectedPriceQuantity.value.third}" + "P",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(androidx.compose.ui.graphics.Color.White)
            )
        }

    }
}

private fun startTextRecognition(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    cameraController: LifecycleCameraController,
    previewView: PreviewView,
    onDetectedTextUpdated: (Triple<Double, Double, Double>) -> Unit
) {
    cameraController.imageAnalysisResolutionSelector = ResolutionSelector.Builder().build()
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        TextRecognitionAnalyzer(onDetectedTextUpdated = onDetectedTextUpdated)
    )
    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}


