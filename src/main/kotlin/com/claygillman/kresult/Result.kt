@file:Suppress("UNCHECKED_CAST")

package com.claygillman.kresult

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Monad representation of either a value or failure.
 */
sealed class Result<out A> {

    /**
     * Maps this result to another result via [f], then returns it.
     *
     * If this result is a [Failure], then it will be immediately returned without executing [f].
     *
     * If [f] throws an exception, it will be caught and this method will return a [Failure].
     */
    abstract fun <B> flatMap(f: (A) -> Result<B>): Result<B>

    /**
     * If this result is a [Failure], this method will map it to another [Failure] that contains [Throwable].
     *
     * If this result is a success, it will return itself.
     */
    abstract fun mapFailure(exception: Throwable): Result<A>

    /**
     * If this result is a [Success], executes [func]. This method always returns itself.
     */
    abstract fun onSuccess(func: (A) -> Unit): Result<A>

    /**
     * If this result is a [Failure], executes [func]. This method always returns itself.
     */
    abstract fun onFailure(func: (Throwable) -> Unit): Result<A>

    /**
     * Tests this result using [condition]. If the result is a [Failure] or the filter returns true,
     * this will return itself.
     *
     * If the result is a [Success] and the filter returns false or throws an exception, this will return a [Failure].
     */
    fun filter(exception: Throwable, condition: (A) -> Boolean): Result<A> = flatMap {
        if (condition(it))
            this
        else
            failure(exception)
    }

    companion object {

        /**
         * Creates a successful [Result] object that wraps [value].
         */
        operator fun <A> invoke(value: A): Result<A> = Success(value)

        /**
         * Creates a [Result] object that wraps the result of [func]. If [func] throws an exception, the [Result]
         * will be a [Failure] that wraps the thrown exception.
         */
        operator fun <A> invoke(func: () -> A): Result<A> = try {
            Result(func())
        } catch (e: Exception) {
            failure(e)
        }

        /**
         * Creates a failure [Result] object that wraps [exception]
         */
        fun <A> failure(exception: Throwable): Result<A> = Failure(exception)
    }
}

class Failure<out A>(val exception: Throwable): Result<A>() {

    override fun <B> flatMap(f: (A) -> Result<B>): Result<B> = Failure(exception)

    override fun mapFailure(exception: Throwable): Result<A> = Failure(exception)

    override fun onSuccess(func: (A) -> Unit) = this

    override fun onFailure(func: (Throwable) -> Unit) = this.also { func(this.exception) }
}

class Success<out A>(val value: A): Result<A>() {

    override fun <B> flatMap(f: (A) -> Result<B>): Result<B> = try {
        f(value)
    } catch (e: Exception) {
        Failure(e)
    }

    override fun mapFailure(exception: Throwable): Result<A> = this

    override fun onSuccess(func: (A) -> Unit) = this.also { func(this.value) }

    override fun onFailure(func: (Throwable) -> Unit) = this
}

/**
 * Maps this result to another value via [transform], then returns [B] wrapped in a [Result] object.
 *
 * If this result is a [Failure], then it will be immediately returned without executing [transform].
 *
 * If [transform] throws an exception, it will be caught and this method will return a [Failure].
 */
@ExperimentalContracts
inline infix fun <A, B> Result<A>.map(transform: (A) -> B): Result<B> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }

    return when (this) {
        is Success -> try {
            Success(transform(value))
        } catch (e: Exception) {
            Failure<B>(e)
        }
        is Failure -> this as Result<B>
    }
}


/**
 * Gets the current value [A] if this is a [Success]. Otherwise, returns [defaultValue].
 */
fun <A> Result<A>.getOrElse(defaultValue: A): A = when (this) {
    is Success -> this.value
    else -> defaultValue
}

/**
 * Gets the current value [A] if this is a [Success]. Otherwise, returns the result of [defaultValFunc].
 */
fun <A> Result<A>.getOrElse(defaultValFunc: () -> A): A = when (this) {
    is Success -> this.value
    else -> defaultValFunc()
}

/**
 * Gets the current value [A] if this is a [Success]. Otherwise, returns the result of [defaultValFunc].
 */
fun <A> Result<A>.orElse(defaultValFunc: () -> Result<A>): Result<A> = when (this) {
    is Success -> this
    else -> try {
        defaultValFunc()
    } catch (e: Exception) {
        Result.failure<A>(e)
    }
}

/**
 * Gets the current value [A] if this is a [Success]. Otherwise, returns the result of [handler].
 */
fun <A> Result<A>.getOrHandle(handler: (Throwable: Throwable) -> A): A = when (this) {
    is Success -> this.value
    is Failure -> handler(this.exception)
}
