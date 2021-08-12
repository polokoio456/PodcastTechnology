package com.nie.podcasttechnology.extension

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun Observable<Any>.throttleClick(duration: Long = 1_000): Observable<Any> {
    return this.throttleFirst(duration, TimeUnit.MILLISECONDS)
}