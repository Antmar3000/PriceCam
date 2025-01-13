package com.example.pricecam.presentation.viewmodels

import android.content.Context
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.pricecam.domain.AnalyzeResultUseCase
import com.example.pricecam.domain.AnalyzeListener
import com.example.pricecam.domain.PriceTag
import com.example.pricecam.domain.TextRecognitionAnalyzer

class MainViewModel : ViewModel() {

    val resultUseCase = AnalyzeResultUseCase()

    private val _detectedPriceQuantity = mutableStateOf(PriceTag(0.0f, 0.0f, 0.0f))
    val detectedPriceQuantity : State<PriceTag> = _detectedPriceQuantity

    private val interfaceImpl = AnalyzeListener {result -> _detectedPriceQuantity.value = resultUseCase(result) }

    fun startTextRecognition(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        cameraController: LifecycleCameraController,
        previewView: PreviewView
    ) {
        cameraController.imageAnalysisResolutionSelector = ResolutionSelector.Builder().build()
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            TextRecognitionAnalyzer(interfaceImpl)
        )
        cameraController.bindToLifecycle(lifecycleOwner)
        previewView.controller = cameraController
    }
}