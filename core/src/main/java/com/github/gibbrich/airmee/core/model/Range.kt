package com.github.gibbrich.airmee.core.model

data class Range(
    val start: Long,
    val end: Long
)

fun Pair<Long, Long>.toRange() = Range(first, second)