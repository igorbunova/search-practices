package com.igorbunova.service.async;

import rx.Observable;

/**
 * Put.
 */
public interface AsyncOperation<T, R> {
    Observable<R> apply(T obj);
}
