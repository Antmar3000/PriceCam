package com.example.pricecam.presentation.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.pricecam.R
import com.example.pricecam.data.PriceTag
import com.example.pricecam.domain.TextRecognitionAnalyzer


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CameraBox() {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    val detectedPriceQuantity = remember { mutableStateOf(PriceTag(0.0, 0.0, 0.0)) }

    fun onTextUpdated(updatedData: PriceTag) {
        detectedPriceQuantity.value = updatedData
    }

    fun torchController () {
        if (cameraController.cameraInfo != null) {
            if (cameraController.cameraInfo!!.hasFlashUnit()) {
                when (cameraController.torchState.value) {
                    0 -> cameraController.enableTorch(true)
                    1 -> cameraController.enableTorch(false)
                }
            }
        }
    }


    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues: PaddingValues ->

        Box (modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.BottomCenter) {

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
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

                OutlinedButton(
                    onClick = { torchController() },
                    shape = CircleShape,
                    border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color.Black),
                    modifier = Modifier.padding(paddingValues),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = androidx.compose.ui.graphics.Color.White.copy(
                            0.3f
                        )
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_flashlight_on_24),
                        contentDescription = "torch",
                        tint = androidx.compose.ui.graphics.Color.White
                    )
                }

            }

            BottomDataDisplay(detectedPriceQuantity.value)

        }

    }
}

private fun startTextRecognition(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    cameraController: LifecycleCameraController,
    previewView: PreviewView,
    onDetectedTextUpdated: (PriceTag) -> Unit
) {
    cameraController.imageAnalysisResolutionSelector = ResolutionSelector.Builder().build()
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        TextRecognitionAnalyzer(onDetectedTextUpdated = onDetectedTextUpdated)
    )
    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}

@Composable
fun KeepScreenOn() {
    val currentView = LocalView.current
    DisposableEffect(Unit) {
        currentView.keepScreenOn = true
        onDispose {
            currentView.keepScreenOn = false
        }
    }
}






