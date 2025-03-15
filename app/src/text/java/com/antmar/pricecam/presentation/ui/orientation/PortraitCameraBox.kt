package com.antmar.pricecam.presentation.ui.orientation

import android.graphics.Color
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.antmar.pricecam.R
import com.antmar.pricecam.presentation.ui.utils.CameraPreviewRectangle
import com.antmar.pricecam.presentation.viewmodels.MainViewModel

@Composable
fun PortraitCameraBox(
    viewModel: MainViewModel,
    cameraController: LifecycleCameraController,
    torchController: () -> Unit
) {

    val torchState = viewModel.torchState.collectAsState().value

    SideEffect {
        cameraController.enableTorch(torchState)
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
                        .fillMaxSize(),
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
                    modifier = Modifier
                        .padding(paddingValues)
                        .size(90.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = androidx.compose.ui.graphics.Color.DarkGray.copy(alpha = 0.5f)
                    )
                ) {
                    Icon(
                        painter = painterResource(if (torchState) R.drawable.baseline_flashlight_on_24 else R.drawable.baseline_flashlight_off_24),
                        contentDescription = "torch",
                        tint = androidx.compose.ui.graphics.Color.White
                    )
                }
            }

//            PortraitDataDisplay(viewModel)

            PreviewScannedText()
        }
    }
}
