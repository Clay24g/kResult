package com.claygillman.kresult

/**
 * Monad representation of either a value or failure.
 */
sealed class Result<A> {

    /**
     * Maps this result to another value via [f], then returns [B] wrapped in a [Result] object.
     *
     * If this result is a [Failure], then it will be immediately returned without executing [f].
     *
     * If [f] throws an exception, it will be caught and this method will return a [Failure].
     */
    abstract fun <B> map(f: (A) -> B): Result<B>

    /**
     * Maps this result to another result via [f], then returns it.
     *
     * If this result is a [Failure], then it will be immediately returned without executing [f].
     *
     * If [f] throws an exception, it will be caught and this method will return a [Failure].
     */
    abstract fun <B> flatMap(f: (A) -> Result<B>): Result<B>

    /**
     * If this result is a [Failure], this method will map it to another [Failure] that contains [failReason].
     *
     * If this result is a success, it will return itself.
     */
    abstract fun mapFailure(failReason: FailReason): Result<A>

    /**
     * If this result is a [Success], executes [func]. This method always returns itself.
     */
    abstract fun onSuccess(func: (A) -> Unit): Result<A>

    /**
     * If this result is a [Failure], executes [func]. This method always returns itself.
     */
    abstract fun onFailure(func: (FailReason) -> Unit): Result<A>

    /**
     * Tests this result using [condition]. If the result is a [Failure] or the filter returns true,
     * this will return itself.
     *
     * If the result is a [Success] and the filter returns false or throws an exception, this will return a [Failure].
     */
    fun filter(condition: (A) -> Boolean): Result<A> = filter("Condition did not match", condition)

    /**
     * Tests this result using [condition]. If the result is a [Failure] or the filter returns true,
     * this will return itself.
     *
     * If the result is a [Success] and the filter returns false or throws an exception, this will return a [Failure].
     */
    fun filter(failMessage: String, condition: (A) -> Boolean): Result<A> = flatMap {
        if (condition(it))
            this
        else
            failure(failMessage)
    }

    /**
     * Gets the current value [A] if this is a [Success]. Otherwise, returns the result of [handler].
     */
    fun getOrHandle(handler: (failReason: FailReason) -> A): A = when (this) {
        is Success -> this.value
        is Failure -> handler(this.failReason)
    }

    /**
     * Gets the current value [A] if this is a [Success]. Otherwise, returns [defaultValue].
     */
    fun getOrElse(defaultValue: A): A = when (this) {
        is Success -> this.value
        else -> defaultValue
    }

    /**
     * Gets the current value [A] if this is a [Success]. Otherwise, returns the result of [defaultValFunc].
     */
    fun getOrElse(defaultValFunc: () -> A): A = when (this) {
        is Success -> this.value
        else -> defaultValFunc()
    }

    /**
     * Gets the current value [A] if this is a [Success]. Otherwise, returns the result of [defaultValFunc].
     */
    fun orElse(defaultValFunc: () -> Result<A>): Result<A> = when (this) {
        is Success -> this
        else -> try {
            defaultValFunc()
        } catch (e: Exception) {
            failure<A>(e)
        }
    }

    class Failure<A>(val failReason: FailReason): Result<A>() {

        override fun <B> map(f: (A) -> B): Result<B> = Failure(failReason)

        override fun <B> flatMap(f: (A) -> Result<B>): Result<B> = Failure(failReason)

        override fun mapFailure(failReason: FailReason): Result<A> = Failure(failReason)

        override fun onSuccess(func: (A) -> Unit) = this

        override fun onFailure(func: (FailReason) -> Unit) = this.also { func(this.failReason) }
    }

    class Success<A>(val value: A): Result<A>() {

        override fun <B> map(f: (A) -> B): Result<B> = try {
            Success(f(value))
        } catch (e: Exception) {
            Failure(FailReason(exception = e))
        }

        override fun <B> flatMap(f: (A) -> Result<B>): Result<B> = try {
            f(value)
        } catch (e: Exception) {
            Failure(FailReason(exception = e))
        }

        override fun mapFailure(failReason: FailReason): Result<A> = this

        override fun onSuccess(func: (A) -> Unit) = this.also { func(this.value) }

        override fun onFailure(func: (FailReason) -> Unit) = this
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
         * Creates a failure [Result] object that wraps [message]
         */
        fun <A> failure(message: String): Result<A> = Failure(FailReason(message))

        /**
         * Creates a failure [Result] object that wraps [exception]
         */
        fun <A> failure(exception: Exception): Result<A> = Failure(FailReason(exception = exception))

        /**
         * Creates a failure [Result] object that wraps [message] and [exception]
         */
        fun <A> failure(message: String, exception: Exception): Result<A> = Failure(FailReason(message, exception))
    }
}
