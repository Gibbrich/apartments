package com.github.gibbrich.airmee.core.model

data class BookingRange(
    val start: Long,
    val end: Long
)

fun BookingRange?.isNotIntersect(other: BookingRange?): Boolean =
    this == null
            || other == null
            || this.end < other.start
            || this.start > other.end