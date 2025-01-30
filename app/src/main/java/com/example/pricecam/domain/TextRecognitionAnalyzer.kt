package com.example.pricecam.domain

import android.graphics.Matrix
import android.graphics.Rect
import android.media.Image
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TextRecognitionAnalyzer(
    private val listener: AnalyzeListener,
    private val previewView: PreviewView
) : ImageAnalysis.Analyzer {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.setCropRect(Rect(previewView.left, previewView.top, previewView.right, previewView.bottom))
        scope.launch {
            val mediaImage: Image = imageProxy.image ?: run { imageProxy.close(); return@launch }
            val inputImage: InputImage =
                InputImage.fromMediaImage(
                    mediaImage,
                    imageProxy.imageInfo.rotationDegrees
                )

            suspendCoroutine { continuation ->
                textRecognizer.process(inputImage).addOnSuccessListener { visionText ->
                    val detectedText = visionText.text
                    if (detectedText.isNotBlank()) {
                        listener.onDetectedText(visionText)
                    }
                }.addOnCompleteListener {
                    continuation.resume(Unit)
                }
            }
            delay(TIMEOUT)
        }.invokeOnCompletion { exception ->
            exception?.printStackTrace()
            imageProxy.close()
        }
    }

    private fun getCorrectionMatrix(imageProxy: ImageProxy, previewView: PreviewView): Matrix {

        val cropRect = imageProxy.cropRect
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
        val matrix = Matrix()

        val sourceArray = floatArrayOf(
            cropRect.left.toFloat(),
            cropRect.top.toFloat(),
            cropRect.right.toFloat(),
            cropRect.top.toFloat(),
            cropRect.right.toFloat(),
            cropRect.bottom.toFloat(),
            cropRect.left.toFloat(),
            cropRect.bottom.toFloat()
        )

        val destinationArray = floatArrayOf(
            0f,
            0f,
            previewView.width.toFloat(),
            0f,
            previewView.width.toFloat(),
            previewView.height.toFloat(),
            0f,
            previewView.height.toFloat()
        )

        val vertexSize = 2

        val shiftOffset = rotationDegrees / 90 * vertexSize

        val tempArray = destinationArray.clone()
        for (toIndex in sourceArray.indices) {
            val fromIndex = (toIndex + shiftOffset) % sourceArray.size
            destinationArray[toIndex] = tempArray[fromIndex]
        }

        matrix.setPolyToPoly(sourceArray, 0, destinationArray, 0, 4)

        return matrix
    }

    companion object {
        const val TIMEOUT = 600L
    }
}





