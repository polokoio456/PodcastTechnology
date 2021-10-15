package com.nie.podcasttechnology.extension

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

fun Observable<Any>.throttleClick(duration: Long = 1_000): Observable<Any> {
    return this.throttleFirst(duration, TimeUnit.MILLISECONDS)
}

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}