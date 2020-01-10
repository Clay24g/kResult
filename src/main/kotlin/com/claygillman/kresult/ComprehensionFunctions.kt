package com.claygillman.kresult

/**
 * Since Kotlin does not have built-in support for comprehensions, this is a hard-coded workaround that supports
 * comprehensions for up to 6 Result maps.
 */


/**
 * Performs a nested chain of [Result.flatMap] calls (comprehension)
 */
fun <A, B> doResultFlatMap(f1: () -> Result<A>, f2: (A) -> Result<B>): Result<B> {
    return f1().flatMap { a -> f2(a) }
}

/**
 * Performs a nested chain of [Result.flatMap] calls (comprehension)
 */
fun <A, B, C> doResultFlatMap(f1: () -> Result<A>,
                              f2: (A) -> Result<B>,
                              f3: (A, B) -> Result<C>): Result<C> {
    return f1().flatMap { a ->
        f2(a).flatMap { b -> f3(a, b) }
    }
}

/**
 * Performs a nested chain of [Result.flatMap] calls (comprehension)
 */
fun <A, B, C, D> doResultFlatMap(f1: () -> Result<A>,
                                 f2: (A) -> Result<B>,
                                 f3: (A, B) -> Result<C>,
                                 f4: (A, B, C) -> Result<D>): Result<D> {
    return f1().flatMap { a ->
        f2(a).flatMap { b ->
            f3(a, b).flatMap { c ->
                f4(a, b, c)
            }
        }
    }
}

/**
 * Performs a nested chain of [Result.flatMap] calls (comprehension)
 */
fun <A, B, C, D, E> doResultFlatMap(f1: () -> Result<A>,
                                    f2: (A) -> Result<B>,
                                    f3: (A, B) -> Result<C>,
                                    f4: (A, B, C) -> Result<D>,
                                    f5: (A, B, C, D) -> Result<E>): Result<E> {
    return f1().flatMap { a ->
        f2(a).flatMap { b ->
            f3(a, b).flatMap { c ->
                f4(a, b, c).flatMap { d ->
                    f5(a, b, c, d)
                }
            }
        }
    }
}

/**
 * Performs a nested chain of [Result.flatMap] calls (comprehension)
 */
fun <A, B, C, D, E, F> doResultFlatMap(f1: () -> Result<A>,
                                       f2: (A) -> Result<B>,
                                       f3: (A, B) -> Result<C>,
                                       f4: (A, B, C) -> Result<D>,
                                       f5: (A, B, C, D) -> Result<E>,
                                       f6: (A, B, C, D, E) -> Result<F>): Result<F> {
    return f1().flatMap { a ->
        f2(a).flatMap { b ->
            f3(a, b).flatMap { c ->
                f4(a, b, c).flatMap { d ->
                    f5(a, b, c, d).flatMap { e ->
                        f6(a, b, c, d, e)
                    }
                }
            }
        }
    }
}



/**
 * Performs a nested chain of [Result.flatMap] calls (comprehension) with the last call being a [Result.map]
 */
fun <A, B> doResultMap(f1: () -> Result<A>, f2: (A) -> B): Result<B> {
    return f1().map { a -> f2(a) }
}

/**
 * Performs a nested chain of [Result.flatMap] calls (comprehension) with the last call being a [Result.map]
 */
fun <A, B, C> doResultMap(f1: () -> Result<A>,
                          f2: (A) -> Result<B>,
                          f3: (A, B) -> C): Result<C> {
    return f1().flatMap { a ->
        f2(a).map { b -> f3(a, b) }
    }
}

/**
 * Performs a nested chain of [Result.flatMap] calls (comprehension) with the last call being a [Result.map]
 */
fun <A, B, C, D> doResultMap(f1: () -> Result<A>,
                             f2: (A) -> Result<B>,
                             f3: (A, B) -> Result<C>,
                             f4: (A, B, C) -> D): Result<D> {
    return f1().flatMap { a ->
        f2(a).flatMap { b ->
            f3(a, b).map { c ->
                f4(a, b, c)
            }
        }
    }
}

/**
 * Performs a nested chain of [Result.flatMap] calls (comprehension) with the last call being a [Result.map]
 */
fun <A, B, C, D, E> doResultMap(f1: () -> Result<A>,
                                f2: (A) -> Result<B>,
                                f3: (A, B) -> Result<C>,
                                f4: (A, B, C) -> Result<D>,
                                f5: (A, B, C, D) -> E): Result<E> {
    return f1().flatMap { a ->
        f2(a).flatMap { b ->
            f3(a, b).flatMap { c ->
                f4(a, b, c).map { d ->
                    f5(a, b, c, d)
                }
            }
        }
    }
}

/**
 * Performs a nested chain of [Result.flatMap] calls (comprehension) with the last call being a [Result.map]
 */
fun <A, B, C, D, E, F> doResultMap(f1: () -> Result<A>,
                                   f2: (A) -> Result<B>,
                                   f3: (A, B) -> Result<C>,
                                   f4: (A, B, C) -> Result<D>,
                                   f5: (A, B, C, D) -> Result<E>,
                                   f6: (A, B, C, D, E) -> F): Result<F> {
    return f1().flatMap { a ->
        f2(a).flatMap { b ->
            f3(a, b).flatMap { c ->
                f4(a, b, c).flatMap { d ->
                    f5(a, b, c, d).map { e ->
                        f6(a, b, c, d, e)
                    }
                }
            }
        }
    }
}