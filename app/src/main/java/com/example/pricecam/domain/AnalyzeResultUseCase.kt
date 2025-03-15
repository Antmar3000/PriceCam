package com.example.pricecam.domain

import com.google.mlkit.vision.text.Text
import java.math.RoundingMode

class AnalyzeResultUseCase {

    operator fun invoke(result: Text): PriceTag {
        return uniteResults(result)
    }

    private fun filterHighestLine(result: Text): Float {

        var highestElement = 0.0
        var highestElementMatch = 0.0f

        result.textBlocks.forEach { block ->
            if (block.boundingBox != null) {
                block.lines.forEach { line ->
                    line.elements.forEach { element ->
                        val lineHeight: Double = element.boundingBox!!.height().toDouble()
                        if (lineHeight > highestElement && element.text.matches(Regex("\\d{2,4}"))) {
                            highestElement =
                                lineHeight
                            highestElementMatch = element.text.toFloat()
                        }
                    }
                }
            }
        }

        return highestElementMatch
    }

    operator fun Regex.contains(text: CharSequence): Boolean = this.containsMatchIn(text)

    private fun identifyQuantity(result: Text): Float {

        var foundFirstMatch = false
        var weightOrVolumeMatch = "0.0"
        var matchWithKilos = false

        val regex1 = "\\d+\\s*[rfT]([.,])?\\b".toRegex()
        val regex2 = "\\d+\\s*[Trf][pP]([.,])?\\b".toRegex()
        val regex3 = "\\d{1,2}([.,]\\d+)?\\s*[nLAN]([.,])?\\b".toRegex()
        val regex4 = "\\d+\\s*[Mm][AlnNI]([.,])?\\b".toRegex()
        val regex5 = "\\d+([.,]\\d+)?\\s*[Kk][grT]([.,])?".toRegex()
        val regex6 = "\\d+\\s*[mM](/I|JI|JN)([.,])?".toRegex()
        val regex7 = "\\d+([.,]\\d+)?\\s*(/I|JI|JN)([.,])?".toRegex()


        run loop@{

            result.textBlocks.forEach { block ->
                if (block.boundingBox != null && !foundFirstMatch)
                    block.lines.forEach { line ->
                        when (line.text) {
                            in regex1 -> {
                                weightOrVolumeMatch = regex1.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                return@loop
                            }

                            in regex2 -> {
                                weightOrVolumeMatch = regex2.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                return@loop
                            }

                            in regex3 -> {
                                weightOrVolumeMatch = regex3.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                matchWithKilos = true
                                return@loop
                            }

                            in regex4 -> {
                                weightOrVolumeMatch = regex4.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                return@loop
                            }

                            in regex5 -> {
                                weightOrVolumeMatch = regex5.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                matchWithKilos = true
                                return@loop
                            }

                            in regex6 -> {
                                weightOrVolumeMatch = regex6.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                return@loop
                            }

                            in regex7 -> {
                                weightOrVolumeMatch = regex7.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                matchWithKilos = true
                                return@loop
                            }
                        }
                    }
            }
        }

        val weightOrVolumeDigits =
            weightOrVolumeMatch.replace(",", ".").replace("[^0-9.]".toRegex(), "")


        return if (matchWithKilos) weightOrVolumeDigits.toFloat()
        else weightOrVolumeDigits.toFloat().div(1000)
    }

    private fun uniteResults(result: Text): PriceTag {

        val roundedResult =
            with(filterHighestLine(result).div(identifyQuantity(result))) {
                if (!this.isNaN() && !this.isInfinite())
                    this.toBigDecimal().setScale(2, RoundingMode.UP).toFloat()
                else 0.0f
            }

        return PriceTag(
            filterHighestLine(result),
            identifyQuantity(result),
            roundedResult,
            result.text
        )
    }
}