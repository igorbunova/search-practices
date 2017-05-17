package com.igorbunova.db.service.async;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import com.igorbunova.db.DbException;
import rx.Observable;
import com.igorbunova.service.async.AsyncOperation;

/**
 * DeleteSong.
 */
public class DeleteSong implements AsyncOperation<Long, Long> {

    private final DataSource ds;

    public DeleteSong(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Observable<Long> apply(final Long id) {
        return Observable.just(delete(id))
            .filter(res -> res != 0)
            .map(r -> id);
    }

    private int delete(long id) {
        try (Connection c = ds.getConnection()) {
            c.setAutoCommit(false);
            try {
                int res = delete(id, c);
                c.commit();
                return res;
            } catch (SQLException th) {
                c.rollback();
                throw th;
            }
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    private int delete(long id, Connection c) throws SQLException {
        String deleteTranslations = "delete from translation where song_id = ?";
        String deleteSong = "delete from song where id = ?";
        int res = 0;
        try (PreparedStatement ps = c.prepareStatement(deleteTranslations)) {
            ps.setLong(1, id);
            res += ps.executeUpdate();
        }
        try (PreparedStatement ps = c.prepareStatement(deleteSong)) {
            ps.setLong(1, id);
            res += ps.executeUpdate();
        }
        return res;
    }
}
