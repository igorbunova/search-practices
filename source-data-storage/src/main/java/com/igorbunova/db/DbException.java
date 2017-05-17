package com.igorbunova.db;

/**
 * DbException.
 */
public class DbException extends RuntimeException {
    public DbException(Throwable cause) {
        super("Db problem", cause);
    }
}
