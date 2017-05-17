package com.igorbunova.model;

import java.util.Map;

/**
 * SourceMapping.
 */
public interface SourceMapping<T> {
    Map<String, Object> source(T obj);

    String id(T obj);
}
