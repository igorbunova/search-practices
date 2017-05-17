package com.igorbunova.model;

/**
 * ModelException.
 */
public class ModelException extends RuntimeException {
    public ModelException() {
        super("Can't create model object");
    }
}
