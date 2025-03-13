package com.example.pricecam.presentation.ui

import android.content.res.Configuration
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.pricecam.presentation.ui.orientation.LandscapeCameraBox
import com.example.pricecam.presentation.ui.orientation.PortraitCameraBox
import com.example.pricecam.presentation.viewmodels.MainViewModel

@Composable
fun CameraScreen(viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            bindToLifecycle(lifecycleOwner)
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }
    var torchState by remember { mutableStateOf(false) }
    val orientation = LocalConfiguration.current.orientation

    fun torchController() {
        if (cameraController.cameraInfo != null) {
            if (cameraController.cameraInfo!!.hasFlashUnit()) {
                when (cameraController.torchState.value) {
                    0 -> {
                        cameraController.enableTorch(true)
                        torchState = true
                    }

                    1 -> {
                        cameraController.enableTorch(false)
                        torchState = false
                    }
                }
            }
        }
    }

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitCameraBox(viewModel, cameraController, ::torchController, torchState)
    } else {
        LandscapeCameraBox(viewModel, cameraController, ::torchController, torchState)
    }
}


