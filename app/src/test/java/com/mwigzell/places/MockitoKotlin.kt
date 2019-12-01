package com.mwigzell.places

import org.mockito.ArgumentCaptor
import org.mockito.Mockito

/**
 * Return ArgumentCaptor.capture() as non-nullable type to avoid java.lang.IllegalStateException
 * when null is returned.
 * @param argumentCaptor
 * @return ArgumentCaptor.capture()
 */
fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

/**
 * Returns any() as non-nullable type to avoid java.lang.IllegalStateException etc.
 *
 * @return Returns instance of T
 */
fun <T> any(): T {
    Mockito.any<T>()
    return uninitialized()
}

fun <T> eq(value: T): T {
    Mockito.eq(value)
    return uninitialized()
}

private fun <T> uninitialized(): T = null as T