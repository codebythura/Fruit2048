package com.codebythura.fruit2048.data

import java.text.NumberFormat
import java.util.Locale


fun Int.format() : String {
    return NumberFormat.getNumberInstance(Locale.getDefault()).format(this)
}
