package com.antmar.pricecam.presentation

import androidx.compose.runtime.Composable
import com.antmar.pricecam.presentation.ui.CameraScreen
import com.antmar.pricecam.presentation.permission.NoPermissionScreen
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
    if (hasPermission) CameraScreen() else NoPermissionScreen(requestPermission)

}