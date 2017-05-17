package com.igorbunova.service.async;

import rx.Observable;

/**
 * AsyncSearch.
 */
public interface AsyncSearch<T> {
    Observable<T> apply(String query, int offset, int limit);
}
