package com.example.pricecam.domain

import android.media.Image
import android.util.Log
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
    private val onDetectedTextUpdated: (PriceTag) -> Unit
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

    result.textBlocks.forEach { block ->
        if (block.boundingBox != null) {
            block.lines.forEach { line ->
                line.elements.forEach { element ->
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

    val regex1 = "\\d+([.,]\\d+)?[rp]".toRegex()
    val regex2 = "\\d+([.,]\\d+)?[rpf][pP]".toRegex()
    val regex3 = "\\d+([.,]\\d+)?[nLA]".toRegex()
    val regex4 = "\\d+[MAml]".toRegex()
    val regex5 = "\\d{3}".toRegex()


    run loop@{

        result.textBlocks.forEach { block ->
            if (block.boundingBox != null && !foundFirstMatch)
                block.lines.forEach { line ->
                    line.elements.forEach { element ->
                        when (element.text) {
                            in regex1 -> {
                                weightOrVolumeMatch =
                                    regex1.find(element.text)?.value?.filterNot { it.isWhitespace() }
                                        ?: "0"
                                foundFirstMatch = true
                                return@loop
                            }

                            in regex2 -> {
                                weightOrVolumeMatch =
                                    regex2.find(element.text)?.value?.filterNot { it.isWhitespace() }
                                        ?: "0"
                                foundFirstMatch = true
                                return@loop
                            }

                            in regex3 -> {
                                weightOrVolumeMatch =
                                    regex3.find(element.text)?.value?.filterNot { it.isWhitespace() }
                                        ?: "0"
                                foundFirstMatch = true
                                return@loop
                            }

                            in regex4 -> {
                                weightOrVolumeMatch =
                                    regex4.find(element.text)?.value?.filterNot { it.isWhitespace() }
                                        ?: "0"
                                foundFirstMatch = true
                                return@loop
                            }

                            in regex5 -> {
                                weightOrVolumeMatch =
                                    regex5.find(element.text)?.value?.filterNot { it.isWhitespace() }
                                        ?: "0"
                                foundFirstMatch = true
                                return@loop
                            }
                        }
                    }
                }
        }
    }

    val weightDigit = if (weightOrVolumeMatch.endsWith("n")
        || weightOrVolumeMatch.endsWith("L")
        || weightOrVolumeMatch.endsWith("A")
    ) weightOrVolumeMatch.replace("[nLA]".toRegex(), "").replace(",", ".")
    else weightOrVolumeMatch.replace("\\D+".toRegex(), "").replace(",", ".")

    Log.d("MyLog", weightDigit)


    return if (weightOrVolumeMatch.endsWith("n")
        || weightOrVolumeMatch.endsWith("L")
        || weightOrVolumeMatch.endsWith("A")
    ) {
        weightDigit.toDouble()
    } else weightDigit.toDouble().div(1000)

}

private fun uniteResults(result: Text): PriceTag {

    return PriceTag(
        filterHighestLine(result),
        identifyQuantity(result),
        filterHighestLine(result).div(identifyQuantity(result))
    )
}


