package com.igorbunova.db.service.async;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import com.igorbunova.db.DbException;
import com.igorbunova.model.Song;
import rx.Observable;
import com.igorbunova.service.async.AsyncOperation;

/**
 * PutSong.
 */
public class PutSong implements AsyncOperation<Song, Song> {
    private final DataSource ds;

    public PutSong(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Observable<Song> apply(final Song obj) {
        return Observable.just(obj)
            .map(this::upsert);
    }

    private Song upsert(Song s) {
        try (Connection c = ds.getConnection()) {
            return upsert(s, c);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    private Song upsert(Song s, Connection c) throws SQLException {
        String query = "insert into song " +
            "(id, duration_ms, name, artist_display_name, album_display_name, isrc)" +
            " values (?, ?, ?, ?, ?, ?)" +
            " on conflict(id) do update set" +
            " id = ?," +
            " duration_ms = ?," +
            " name = ?," +
            " artist_display_name = ?," +
            " album_display_name = ?," +
            " isrc = ?," +
            " modify_dt = now()";
        try (PreparedStatement ps = c.prepareStatement(query)) {
            ps.setLong(1, s.getId());
            ps.setLong(7, s.getId());
            ps.setInt(2, s.getDurationMs());
            ps.setInt(9, s.getDurationMs());
            ps.setString(3, s.getName());
            ps.setString(10, s.getName());
            ps.setString(4, s.getArtistDisplayName());
            ps.setString(11, s.getArtistDisplayName());
            ps.setString(5, s.getAlbumDisplayName());
            ps.setString(12, s.getAlbumDisplayName());
            ps.setString(6, s.getIsrc());
            ps.setString(13, s.getIsrc());
            ps.executeUpdate();
            return s;
        }
    }
}
