package com.github.gibbrich.airmee.core.model

import org.junit.Assert.*
import org.junit.Test

class BookingRangeKtTest {
    @Test
    fun `return true if this BoorkingRange null`() {
        val first: BookingRange? = null
        val second = BookingRange(5, 10)
        assertTrue(first.isNotIntersect(second))
    }

    @Test
    fun `return true if other BoorkingRange null`() {
        val first = BookingRange(5, 10)
        val second: BookingRange? = null
        assertTrue(first.isNotIntersect(second))
    }

    @Test
    fun `return true if this BookingRange end less than other BookingRange start`() {
        val first = BookingRange(5, 10)
        val second = BookingRange(11, 15)
        assertTrue(first.isNotIntersect(second))
    }

    @Test
    fun `return true if this BookingRange start greater than other BookingRange end`() {
        val first = BookingRange(11, 15)
        val second = BookingRange(5, 10)
        assertTrue(first.isNotIntersect(second))
    }

    @Test
    fun `return false if this BookingRange start less AND end greater then other BookingRange start and end respectively`() {
        val first = BookingRange(5, 15)
        val second = BookingRange(7, 13)
        assertFalse(first.isNotIntersect(second))
    }

    @Test
    fun `return false if this BookingRange start greater AND end less then other BookingRange start and end respectively`() {
        val first = BookingRange(7, 13)
        val second = BookingRange(5, 15)
        assertFalse(first.isNotIntersect(second))
    }

    @Test
    fun `return false if this BookingRange start less AND end greater then other BookingRange start`() {
        val first = BookingRange(5, 10)
        val second = BookingRange(7, 13)
        assertFalse(first.isNotIntersect(second))
    }

    @Test
    fun `return false if this BookingRange start less AND end greater then other BookingRange end`() {
        val first = BookingRange(5, 10)
        val second = BookingRange(3, 7)
        assertFalse(first.isNotIntersect(second))
    }
}