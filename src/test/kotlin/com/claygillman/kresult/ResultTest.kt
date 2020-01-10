package com.claygillman.kresult

import org.junit.Assert
import org.junit.Test
import java.lang.AssertionError

class ResultTest {

    @Test
    fun `With successful result, map, ensure correct mapped value`() {
        val result = Result(5).map { 10 }

        Assert.assertEquals(10, result.getOrHandle {
            throw AssertionError("Result was a Failure object")
        })
    }

    @Test
    fun `With failing result, map, ensure original value`() {
        val result = Result.failure<Int>("Fail").map { 10 }

        Assert.assertTrue(result is Result.Failure && result.failReason.friendlyMessage == "Fail")
    }

    @Test
    fun `With successful result, flatMap, ensure correct mapped value`() {
        val result = Result("5").flatMap { Result(it.toInt()) }

        Assert.assertEquals(5, result.getOrHandle {
            throw AssertionError("Result was a Failure object")
        })
    }

    @Test
    fun `With failing result, flatMap, ensure original value`() {
        val result = Result.failure<Int>("Fail").flatMap { Result(10) }

        Assert.assertTrue(result is Result.Failure && result.failReason.friendlyMessage == "Fail")
    }

    @Test
    fun `With successful result, apply true filter, ensure success`() {
        val result = Result(5).filter { it < 10 }

        Assert.assertEquals(5, result.getOrHandle {
            throw AssertionError("Result was a Failure object")
        })
    }

    @Test
    fun `With successful result, apply false filter, ensure failure`() {
        val result = Result(5).filter { it < 1 }

        Assert.assertTrue(result is Result.Failure)
    }

    @Test
    fun `With successful result, apply false filter, ensure correct message`() {
        val result = Result(5).filter("Test") { it < 1 }

        Assert.assertEquals("Test", (result as Result.Failure).failReason.friendlyMessage)
    }

    @Test
    fun `With successful result, getOrElse value, value is correct`()
            = Assert.assertEquals(5, Result(5).getOrElse(10))

    @Test
    fun `With successful result, getOrElse function, value is correct`()
            = Assert.assertEquals(5, Result(5).getOrElse { 10 })

    @Test
    fun `With failing result, getOrElse value, value is correct`()
            = Assert.assertEquals(10, Result.failure<Int>("Fail").getOrElse(10))

    @Test
    fun `With failing result, getOrElse function, value is correct`()
            = Assert.assertEquals(10, Result.failure<Int>("Fail").getOrElse { 10 })

    @Test
    fun `With successful result, orElse, value is correct`() = Assert.assertEquals(5, Result(5)
        .orElse { Result(10) }
        .getOrHandle {
            throw AssertionError("Result was a Failure object")
        }
    )

    @Test
    fun `With failing result, orElse, value is correct`() = Assert.assertEquals(10, Result.failure<Int>("Fail")
        .orElse { Result(10) }
        .getOrHandle {
            throw AssertionError("Result was a Failure object")
        }
    )
}