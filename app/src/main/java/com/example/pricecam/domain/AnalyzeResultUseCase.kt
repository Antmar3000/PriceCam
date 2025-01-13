package com.example.pricecam.domain

import com.google.mlkit.vision.text.Text
import java.math.RoundingMode

class AnalyzeResultUseCase {

    operator fun invoke(result: Text) : PriceTag {
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
                            highestElement = lineHeight
                            highestElementMatch = element.text.toFloat()
                        }
                    }
                }
            }
        }

        return highestElementMatch
    }

    operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)
    private fun Regex.findAndFilter(text: CharSequence) =
        find(text)?.value?.filterNot { it.isWhitespace() }

    private fun identifyQuantity(result: Text): Float {

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
                                    weightOrVolumeMatch = regex1.findAndFilter(element.text) ?: "0"
                                    foundFirstMatch = true
                                    return@loop
                                }

                                in regex2 -> {
                                    weightOrVolumeMatch = regex2.findAndFilter(element.text) ?: "0"
                                    foundFirstMatch = true
                                    return@loop
                                }

                                in regex3 -> {
                                    weightOrVolumeMatch = regex3.findAndFilter(element.text) ?: "0"
                                    foundFirstMatch = true
                                    return@loop
                                }

                                in regex4 -> {
                                    weightOrVolumeMatch = regex4.findAndFilter(element.text) ?: "0"
                                    foundFirstMatch = true
                                    return@loop
                                }

                                in regex5 -> {
                                    weightOrVolumeMatch = regex5.findAndFilter(element.text) ?: "0"
                                    foundFirstMatch = true
                                    return@loop
                                }
                            }
                        }
                    }
            }
        }

        val weightDigit = with(weightOrVolumeMatch) {
            if (this.endsWith("n") || this.endsWith("L") || this.endsWith("A"))
                this.replace("[nLA]".toRegex(), "").replace(",", ".")
            else this.replace("\\D+".toRegex(), "").replace(",", ".")
        }

        return with(weightOrVolumeMatch) {
            if (this.endsWith("n") || this.endsWith("L") || this.endsWith("A")) weightDigit.toFloat()
            else weightDigit.toFloat().div(1000)
        }
    }

    private fun uniteResults(result: Text): PriceTag {

        val roundedResult = with(filterHighestLine(result).div(identifyQuantity(result))) {
            if (!this.isNaN() && !this.isInfinite()) this.toBigDecimal().setScale(2, RoundingMode.UP).toFloat() else 0.0f
        }

        return PriceTag(
            filterHighestLine(result),
            identifyQuantity(result),
            roundedResult
        )
    }
}