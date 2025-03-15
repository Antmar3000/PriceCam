package com.example.pricecam.presentation.ui

import android.content.res.Configuration
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val orientation = LocalConfiguration.current.orientation

    val torchState = viewModel.torchState.collectAsStateWithLifecycle().value

    fun torchController() {
        if (cameraController.cameraInfo != null) {
            if (cameraController.cameraInfo!!.hasFlashUnit()) {
                when (cameraController.torchState.value) {
                    0 -> {
                        viewModel.torchState.value = true
                        cameraController.enableTorch(torchState)
                    }

                    1 -> {
                        viewModel.torchState.value = false
                        cameraController.enableTorch(torchState)
                    }
                }
            }
        }
    }

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitCameraBox(viewModel, cameraController, ::torchController)
    } else {
        LandscapeCameraBox(viewModel, cameraController, ::torchController)
    }
}


