package com.example.letterboxd.common.utils

fun String.countOccurrences(char: Char): Int {
    return this.count { it == char }
}