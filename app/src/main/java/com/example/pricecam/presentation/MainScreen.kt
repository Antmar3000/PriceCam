package com.example.pricecam.presentation

import androidx.compose.runtime.Composable
import com.example.pricecam.presentation.camera.CameraBox
import com.example.pricecam.presentation.permission.NoPermissionScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    MainContent(
        hasPermission = cameraPermissionState.status.isGranted,
        requestPermission = cameraPermissionState::launchPermissionRequest
    )

}

@Composable
fun MainContent(
    hasPermission: Boolean,
    requestPermission: () -> Unit
) {
    if (hasPermission) CameraBox() else NoPermissionScreen (requestPermission)

}