package com.example.pricecam.presentation.viewmodels

import android.content.Context
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.pricecam.domain.AnalyzeResultUseCase
import com.example.pricecam.domain.AnalyzeListener
import com.example.pricecam.domain.PriceTag
import com.example.pricecam.domain.TextRecognitionAnalyzer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    val resultUseCase = AnalyzeResultUseCase()

    private val _priceQuantityResult = MutableStateFlow(PriceTag(0.0f, 0.0f, 0.0f, ""))
    val priceQuantityResult: StateFlow<PriceTag> get() = _priceQuantityResult

    val torchState = MutableStateFlow(false)

    private val interfaceImpl =
        AnalyzeListener { result -> _priceQuantityResult.value = resultUseCase(result) }

    fun startTextRecognition(
        context: Context,
        cameraController: LifecycleCameraController,
        previewView: PreviewView
    ) {
        cameraController.imageAnalysisResolutionSelector =
            ResolutionSelector.Builder().setResolutionStrategy(
                ResolutionStrategy.HIGHEST_AVAILABLE_STRATEGY
            ).build()
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            TextRecognitionAnalyzer(interfaceImpl, previewView)
        )
        previewView.controller = cameraController
    }

}