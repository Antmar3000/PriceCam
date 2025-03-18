package com.antmar.pricecam.domain

import com.antmar.pricecam.domain.entity.PriceTag
import com.antmar.pricecam.domain.entity.QuantityInfo
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

        fun findRegex (regex : Regex, text: CharSequence, suff : Int, kilos : Boolean) {
            weightOrVolumeMatch = regex.find(text)?.value ?: "0"
            foundFirstMatch = true
            suffix = suff
            matchWithKilos = kilos
        }

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
                                findRegex(regex1, line.text, 1, false)
                                return@loop
                            }

                            in regex2 -> {
                                findRegex(regex2, line.text, 1, false)
                                return@loop
                            }

                            in regex3 -> {
                                findRegex(regex3, line.text, 2, true)
                                return@loop
                            }

                            in regex4 -> {
                                findRegex(regex4, line.text, 3, false)
                                return@loop
                            }

                            in regex5 -> {
                                findRegex(regex5, line.text, 4, true)
                                return@loop
                            }

                            in regex6 -> {
                                findRegex(regex6, line.text, 3, false)
                                return@loop
                            }

                            in regex7 -> {
                                findRegex(regex7, line.text, 2, true)
                                return@loop
                            }

                            in regex8 -> {
                                findRegex(regex8, line.text, 2, true)
                                return@loop
                            }

                            in regex9 -> {
                                findRegex(regex9, line.text, 2, true)
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