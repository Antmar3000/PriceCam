package com.antmar.pricecam.domain

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

    private fun identifyQuantity(result: Text): QuantityInfo {

        var foundFirstMatch = false
        var weightOrVolumeMatch = "0.0"
        var matchWithKilos = false
        /*
        suffix values :
        1 = gram
        2 = liter
        3 = milliliter
        4 = kilogram

         */
        var suffix = 0

        val regex1 = "\\d+\\s*[rfT]([.,])?\\b".toRegex()
        val regex2 = "\\d+\\s*[Trf][pP]([.,])?\\b".toRegex()
        val regex3 = "\\b\\d{1,2}([.,]\\d+)?\\s*[nLANl]([.,])?\\b".toRegex()
        val regex4 = "\\d+\\s*[Mm][AlnNI]([.,])?".toRegex()
        val regex5 = "\\d{1,2}([.,]\\d+)?\\s*[Kk][grT]([.,])?".toRegex()
        val regex6 = "\\d+\\s*[mM](/I|JI|JN)([.,])?".toRegex()
        val regex7 = "\\d{1,2}([.,]\\d+)?\\s*(/I|JI|JN)([.,])?".toRegex()
        val regex8 = "\\d{1,2}([.,]\\d+)?\\s*[nLNl]([.,])?\\b".toRegex()
        val regex9 = "[0-24-9]{1,2}([.,]\\d+)\\s*A([.,])?\\b".toRegex()


        run loop@{

            result.textBlocks.forEach { block ->
                if (block.boundingBox != null && !foundFirstMatch)
                    block.lines.forEach { line ->
                        when (line.text) {
                            in regex1 -> {
                                weightOrVolumeMatch = regex1.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                suffix = 1
                                return@loop
                            }

                            in regex2 -> {
                                weightOrVolumeMatch = regex2.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                suffix = 1
                                return@loop
                            }

                            in regex3 -> {
                                weightOrVolumeMatch = regex3.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                matchWithKilos = true
                                suffix = 2
                                return@loop
                            }

                            in regex4 -> {
                                weightOrVolumeMatch = regex4.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                suffix = 3
                                return@loop
                            }

                            in regex5 -> {
                                weightOrVolumeMatch = regex5.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                matchWithKilos = true
                                suffix = 4
                                return@loop
                            }

                            in regex6 -> {
                                weightOrVolumeMatch = regex6.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                suffix = 3
                                return@loop
                            }

                            in regex7 -> {
                                weightOrVolumeMatch = regex7.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                matchWithKilos = true
                                suffix = 2
                                return@loop
                            }

                            in regex8 -> {
                                weightOrVolumeMatch = regex8.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                matchWithKilos = true
                                suffix = 2
                                return@loop
                            }

                            in regex9 -> {
                                weightOrVolumeMatch = regex9.find(line.text)?.value ?: "0"
                                foundFirstMatch = true
                                matchWithKilos = true
                                suffix = 2
                                return@loop
                            }
                        }
                    }
            }
        }

        val weightOrVolumeDigits =
            weightOrVolumeMatch.replace(",", ".").replace("[^0-9.]".toRegex(), "")


        return if (matchWithKilos) QuantityInfo(weightOrVolumeDigits.toFloat(), suffix)
        else QuantityInfo(weightOrVolumeDigits.toFloat().div(1000), suffix)
    }

    private fun uniteResults(result: Text): PriceTag {

        val roundedResult =
            with(filterHighestLine(result).div(identifyQuantity(result).quantity)) {
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