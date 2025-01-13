package com.example.pricecam.presentation.viewmodels

import android.content.Context
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.pricecam.domain.AnalyzeResultUseCase
import com.example.pricecam.domain.PriceTag
import com.example.pricecam.domain.TextRecognitionAnalyzer
import com.google.mlkit.vision.text.Text

class MainViewModel : ViewModel() {

    private val _detectedPriceQuantity = mutableStateOf(PriceTag(0.0f, 0.0f, 0.0f))
    val detectedPriceQuantity = _detectedPriceQuantity

    fun onDetectedText (result : Text) {
        _detectedPriceQuantity.value = AnalyzeResultUseCase(result).invoke()
    }


    fun startTextRecognition(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        cameraController: LifecycleCameraController,
        previewView: PreviewView
    ) {
        cameraController.imageAnalysisResolutionSelector = ResolutionSelector.Builder().build()
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            TextRecognitionAnalyzer(this)
        )
        cameraController.bindToLifecycle(lifecycleOwner)
        previewView.controller = cameraController
    }



}