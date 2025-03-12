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

    operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)

    private fun identifyQuantity(result: Text): Float {

        var foundFirstMatch = false
        var weightOrVolumeMatch = "0"
        var matchWithKilos = false

        val regex1 = "\\d+[rpT]".toRegex()
        val regex2 = "\\d+[rpfT][pP]".toRegex()
        val regex3 = "\\d+([.,]\\d+)?[nLAN]".toRegex()
        val regex4 = "\\d+[Mm][AlnN]".toRegex()
        val regex5 = "\\d+([.,]\\d+)?[Kk][grT]".toRegex()
        val regex6 = "\\d+[M/I]".toRegex()


        run loop@{

            result.textBlocks.forEach { block ->
                if (block.boundingBox != null && !foundFirstMatch)
                    block.lines.forEach { line ->
                        line.text.filterNot { it.isWhitespace() }
                        line.elements.forEach { element ->
                            when (element.text) {
                                in regex1 -> {
                                    weightOrVolumeMatch = regex1.find(element.text)?.value ?: "0"
                                    foundFirstMatch = true
                                    return@loop
                                }

                                in regex2 -> {
                                    weightOrVolumeMatch = regex2.find(element.text)?.value ?: "0"
                                    foundFirstMatch = true
                                    return@loop
                                }

                                in regex3 -> {
                                    weightOrVolumeMatch = regex3.find(element.text)?.value ?: "0"
                                    foundFirstMatch = true
                                    matchWithKilos = true
                                    return@loop
                                }

                                in regex4 -> {
                                    weightOrVolumeMatch = regex4.find(element.text)?.value ?: "0"
                                    foundFirstMatch = true
                                    return@loop
                                }

                                in regex5 -> {
                                    weightOrVolumeMatch = regex5.find(element.text)?.value ?: "0"
                                    foundFirstMatch = true
                                    matchWithKilos = true
                                    return@loop
                                }

                                in regex6 -> {
                                    weightOrVolumeMatch = regex6.find(element.text)?.value ?: "0"
                                    foundFirstMatch = true
                                    return@loop
                                }
                            }
                        }
                    }
            }
        }

        val weightOrVolumeDigits =
            weightOrVolumeMatch.replace("\\D+".toRegex(), "").replace(",", ".")

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