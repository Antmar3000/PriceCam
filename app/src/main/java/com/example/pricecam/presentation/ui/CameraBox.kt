package com.example.pricecam.presentation.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.pricecam.R
import com.example.pricecam.presentation.viewmodels.MainViewModel

@Composable
fun CameraBox(viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            bindToLifecycle(lifecycleOwner)
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    fun torchController() {
        if (cameraController.cameraInfo != null) {
            if (cameraController.cameraInfo!!.hasFlashUnit()) {
                when (cameraController.torchState.value) {
                    0 -> cameraController.enableTorch(true)
                    1 -> cameraController.enableTorch(false)
                }
            }
        }
    }

    fun isTorchEnabled(): Boolean {
        return if (cameraController.cameraInfo != null) {
            if (cameraController.cameraInfo!!.hasFlashUnit()) {
                when (cameraController.torchState.value) {
                    0 -> false
                    1 -> true
                    else -> false
                }
            } else false
        } else false
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues: PaddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.BottomCenter
        ) {

            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter
            ) {

                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    factory = { context ->
                        PreviewView(context).apply {
                            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                            setBackgroundColor(Color.BLACK)
                            scaleType = PreviewView.ScaleType.FILL_START
                            viewModel.startTextRecognition(context, cameraController, this)
                        }
                    })

                CameraPreviewRectangle()

                OutlinedButton(
                    onClick = { torchController() },
                    shape = CircleShape,
                    border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color.Black),
                    modifier = Modifier.padding(paddingValues),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = androidx.compose.ui.graphics.Color.DarkGray.copy(alpha = 0.5f)
                    )
                ) {
                    Icon(
                        painter = painterResource(if (isTorchEnabled()) R.drawable.baseline_flashlight_on_24 else R.drawable.baseline_flashlight_off_24),
                        contentDescription = "torch",
                        tint = androidx.compose.ui.graphics.Color.White
                    )
                }
            }

            BottomDataDisplay()

//            PreviewScannedText()
        }
    }
}

@Composable
fun CameraPreviewRectangle() {

    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val frameWidth = 370.dp.toPx()
        val frameHeight = 250.dp.toPx()

        val left = (canvasWidth - frameWidth) / 2
        val top = (canvasHeight - frameHeight) / 2
        val right = left + frameWidth
        val bottom = top + frameHeight

        val path = Path().apply {
            addRect(Rect(0f, 0f, canvasWidth, canvasHeight))
            addRect(Rect(left, top, right, bottom))
            fillType = PathFillType.EvenOdd
        }

        val dashedPath = Path().apply {
            moveTo(left, top)
            lineTo(right, top)
            lineTo(right, bottom)
            lineTo(left, bottom)
            lineTo(left, top)
        }

        val dashedEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(5.dp.toPx(), 10.dp.toPx()), phase = 0f
        )

        drawPath(
            path = path,
            color = androidx.compose.ui.graphics.Color.Black.copy(0.5f),
            blendMode = BlendMode.SrcOver
        )

        drawPath(
            path = dashedPath,
            color = androidx.compose.ui.graphics.Color.White.copy(0.4f),
            style = Stroke(
                width = 2.dp.toPx(), pathEffect = dashedEffect
            )
        )
    }
}
