package com.igorbunova.db.service.async;

import java.sql.PreparedStatement;
import javax.sql.DataSource;
import com.igorbunova.model.Song;
import rx.Observable;
import com.igorbunova.service.async.AsyncOperation;

/**
 * GetSong.
 */
public class GetSong implements AsyncOperation<Long, Song> {
    private final DataSource ds;

    public GetSong(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Observable<Song> apply(Long obj) {
        return Observable.just(obj)
            .flatMap(this::get);
    }

    private Observable<Song> get(Long id) {
        String query = "select " +
            "id, " +
            "name, " +
            "artist_display_name, " +
            "album_display_name, " +
            "duration_ms, " +
            "isrc " +
            "from song" +
            "where id = ?";
        return Observable.create(new TransactionalRead<>(
            ds::getConnection,
            c -> {
                PreparedStatement ps = c.prepareStatement(query);
                ps.setLong(1, id);
                return ps;
            },
            GetAll.mapping
        ));
    }
}
