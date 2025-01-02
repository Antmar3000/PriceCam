package com.example.pricecam.domain

import android.media.Image
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
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
    private val onDetectedTextUpdated: (Triple<Double, Double, Double>) -> Unit
) : ImageAnalysis.Analyzer {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)


    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
            val mediaImage: Image = imageProxy.image ?: run { imageProxy.close(); return@launch }
            val inputImage: InputImage =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            suspendCoroutine { continuation ->
                textRecognizer.process(inputImage).addOnSuccessListener { visionText ->
                    val detectedText = visionText.text
                    if (detectedText.isNotBlank()) {
                        onDetectedTextUpdated(uniteResults(visionText))
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

    companion object {
        const val TIMEOUT = 1500L
    }
}

private fun filterHighestLine(result: Text): Double {

    var highestElement = 0.0
    var highestElementMatch = 0.0

    for (block in result.textBlocks) {
        if (block.boundingBox != null) {
            for (line in block.lines) {
                for (element in line.elements) {
                    val lineHeight: Double = element.boundingBox!!.height().toDouble()
                    if (lineHeight > highestElement && element.text.matches(Regex("\\d+"))) {
                        highestElement = lineHeight
                        highestElementMatch = element.text.toDouble()
                    }
                }
            }
        }
    }
    return highestElementMatch
}

operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)

private fun identifyQuantity(result: Text): Double {

    var foundFirstMatch = false
    var weightOrVolumeMatch = "0"

    val regex1 = "\\d+([.,]\\d+)?[rpnl]".toRegex()
    val regex2 = "\\d+([.,]\\d+)?[rpf][pP]".toRegex()
    val regex3 = "\\d+([.,]\\d+)?[LA]".toRegex()
    val regex4 = "\\d+[MAml]".toRegex()
    val regex5 = "\\d{3}".toRegex()


    for (block in result.textBlocks) {
        if (block.boundingBox != null && !foundFirstMatch) {
            for (line in block.lines) {
                for (element in line.elements) {
                    when (element.text) {
                        in regex1 -> {
                            weightOrVolumeMatch =
                                regex1.find(element.text)?.value?.filterNot { it.isWhitespace() }
                                    ?: "0"
                            foundFirstMatch = true
                            break
                        }

                        in regex2 -> {
                            weightOrVolumeMatch =
                                regex2.find(element.text)?.value?.filterNot { it.isWhitespace() }
                                    ?: "0"
                            foundFirstMatch = true
                            break
                        }

                        in regex3 -> {
                            weightOrVolumeMatch =
                                regex3.find(element.text)?.value?.filterNot { it.isWhitespace() }
                                    ?: "0"
                            foundFirstMatch = true
                            break
                        }

                        in regex4 -> {
                            weightOrVolumeMatch =
                                regex4.find(element.text)?.value?.filterNot { it.isWhitespace() }
                                    ?: "0"
                            foundFirstMatch = true
                            break
                        }

                        in regex5 -> {
                            weightOrVolumeMatch =
                                regex5.find(element.text)?.value?.filterNot { it.isWhitespace() }
                                    ?: "0"
                            foundFirstMatch = true
                            break
                        }
                    }
                }
            }
        }

    }

    val weightDigit = if (weightOrVolumeMatch.endsWith("n")
        || weightOrVolumeMatch.endsWith("L")
        || weightOrVolumeMatch.endsWith("A")
    ) weightOrVolumeMatch.replace("[nlA]".toRegex(), "").replace(",", ".")
    else weightOrVolumeMatch.replace("\\D+".toRegex(), "").replace(",", ".")

    return if (weightOrVolumeMatch.endsWith("n")
        || weightOrVolumeMatch.endsWith("L")
        || weightOrVolumeMatch.endsWith("A")
    ) {
        weightDigit.toDouble()
    } else weightDigit.toDouble().div(1000)

}

private fun uniteResults(result: Text): Triple<Double, Double, Double> {

    return Triple(
        filterHighestLine(result),
        identifyQuantity(result),
        filterHighestLine(result).div(identifyQuantity(result))
    )
}

