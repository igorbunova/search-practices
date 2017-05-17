package com.igorbunova.db.service.async;

import javax.sql.DataSource;
import com.igorbunova.model.Song;
import rx.Observable;

/**
 * GetAll.
 */
public class GetAll {
    private final DataSource ds;

    public GetAll(DataSource ds) {
        this.ds = ds;
    }

    public Observable<Song> apply() {
        String query = "select " +
            "id, " +
            "name, " +
            "artist_display_name, " +
            "album_display_name, " +
            "duration_ms, " +
            "isrc " +
            "from song";
        return Observable.create(new TransactionalRead<>(
            ds::getConnection,
            c -> c.prepareStatement(query),
            mapping
        ));
    }

    static final TransactionalRead.DataMapping<Song> mapping = rs -> new Song(
        rs.getLong(1),
        rs.getString(2),
        rs.getString(3),
        rs.getString(4),
        rs.getInt(5),
        rs.getString(6)
    );
}
