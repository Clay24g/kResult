package com.claygillman.kresult

import org.junit.Assert
import org.junit.Test
import java.lang.AssertionError

class ComprehensionTest {

    @Test
    fun `With successful 2-deep flatMap comprehension, ensure correct value`() {

        Assert.assertEquals(15, doResultFlatMap(
            { Result(5) },
            { Result(10 + it) }
        ).getOrHandle { throw AssertionError("Result was a Failure object") })
    }

    @Test
    fun `With successful 2-deep map comprehension, ensure correct value`() {

        Assert.assertEquals(15, doResultMap(
            { Result(5) },
            { 10 + it }
        ).getOrHandle { throw AssertionError("Result was a Failure object") })
    }


    @Test
    fun `With successful 6-deep flatMap comprehension, ensure correct value`() {

        val arrayToFive = doResultFlatMap(
            { Result(1) },
            { Result(1 + it) },
            { _, two -> Result(1 + two) },
            { _, _, three -> Result(1 + three) },
            { _, _, _, four -> Result(1 + four) },
            { one, two, three, four, five -> Result(arrayOf(one, two, three, four, five)) }
        ).getOrHandle { throw AssertionError("Result was a Failure object") }

        Assert.assertEquals(1, arrayToFive[0])
        Assert.assertEquals(2, arrayToFive[1])
        Assert.assertEquals(3, arrayToFive[2])
        Assert.assertEquals(4, arrayToFive[3])
        Assert.assertEquals(5, arrayToFive[4])
    }

    @Test
    fun `With successful 6-deep map comprehension, ensure correct value`() {

        val arrayToFive = doResultMap(
            { Result(1) },
            { Result(1 + it) },
            { _, two -> Result(1 + two) },
            { _, _, three -> Result(1 + three) },
            { _, _, _, four -> Result(1 + four) },
            { one, two, three, four, five -> arrayOf(one, two, three, four, five) }
        ).getOrHandle { throw AssertionError("Result was a Failure object") }

        Assert.assertEquals(1, arrayToFive[0])
        Assert.assertEquals(2, arrayToFive[1])
        Assert.assertEquals(3, arrayToFive[2])
        Assert.assertEquals(4, arrayToFive[3])
        Assert.assertEquals(5, arrayToFive[4])
    }
}