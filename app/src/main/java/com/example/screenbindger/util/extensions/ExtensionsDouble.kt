package com.example.screenbindger.util.extensions

infix fun Double?.isGreaterThan(other: Double?) =
    this != null && other != null && this > other

infix fun Double?.isLessThan(other: Double?) =
    this != null && other != null && this < other

infix fun Double?.isGreaterOrEqualTo(other: Double?) =
    this != null && other != null && this > other

infix fun Double?.isLessOrEqualTo(other: Double?) =
    this != null && other != null && this < other