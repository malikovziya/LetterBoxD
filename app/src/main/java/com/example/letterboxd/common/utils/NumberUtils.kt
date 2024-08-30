package com.example.letterboxd.common.utils

import java.math.BigDecimal
import java.math.RoundingMode

fun roundFloats(value: Float, n: Int): Float {
    return BigDecimal(value.toString()).setScale(n, RoundingMode.HALF_UP).toFloat()
}