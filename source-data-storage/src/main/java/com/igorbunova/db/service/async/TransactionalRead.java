package com.igorbunova.db.service.async;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import com.igorbunova.db.DbException;
import rx.Observable;
import rx.Subscriber;

/**
 * TransactionalRead.
 */
final class TransactionalRead<T> implements Observable.OnSubscribe<T> {

    private final ConnectionProvider connectionProvider;
    private final QueryProvider queryProvider;
    private final DataMapping<T> mapping;

    public interface ConnectionProvider {
        Connection get() throws SQLException;
    }

    public interface QueryProvider {
        PreparedStatement create(Connection c) throws SQLException;
    }

    public interface DataMapping<T> {
        T apply(ResultSet rs) throws SQLException;
    }

    public TransactionalRead(ConnectionProvider connectionProvider,
                             QueryProvider queryProvider,
                             DataMapping<T> mapping) {
        this.connectionProvider = connectionProvider;
        this.queryProvider = queryProvider;
        this.mapping = mapping;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        try (Connection c = connectionProvider.get()) {
            try (PreparedStatement ps = queryProvider.create(c)) {
                try (ResultSet rs = ps.executeQuery()) {
                    Iterator<T> it = new ResultSetIterator<T>(rs, mapping);
                    while (it.hasNext()) {
                        subscriber.onNext(it.next());
                    }
                }
            }
        } catch (SQLException | DbException e) {
            subscriber.onError(e);
        }
    }

    private static final class ResultSetIterator<T> implements Iterator<T> {

        private final ResultSet rs;
        private final DataMapping<T> mapping;

        private ResultSetIterator(ResultSet rs, DataMapping<T> mapping) {
            this.rs = rs;
            this.mapping = mapping;
        }

        @Override
        public boolean hasNext() {
            try {
                return rs.next();
            } catch (SQLException e) {
                throw new DbException(e);
            }
        }

        @Override
        public T next() {
            try {
                return mapping.apply(rs);
            } catch (SQLException e) {
                throw new DbException(e);
            }
        }
    }
}
