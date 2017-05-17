package com.igorbunova.es.service.async;

import java.util.HashMap;
import java.util.Map;
import com.igorbunova.model.FieldNames;
import com.igorbunova.model.Song;
import com.igorbunova.model.SourceMapping;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import rx.Observable;
import com.igorbunova.service.async.AsyncOperation;

import static java.util.Optional.ofNullable;

/**
 * PutSong.
 */
public class PutSong implements AsyncOperation<Song, String> {
    private final Client client;
    private final String index;
    private final String type;
    private final EsMapping sm = new EsMapping();

    public PutSong(Client client, String index, String type) {
        this.client = client;
        this.index = index;
        this.type = type;
    }

    @Override
    public Observable<String> apply(final Song obj) {

        return Observable.from(
            client.prepareIndex(index, type, sm.id(obj))
            .setSource(sm.source(obj))
            .setOpType(DocWriteRequest.OpType.INDEX)
            .execute()
        ).map(IndexResponse::getId);
    }

    static final class EsMapping implements SourceMapping<Song> {
        EsMapping() {}
        public Map<String, Object> source(Song obj) {
            Map<String, Object> source = new HashMap<>();
            ofNullable(obj.getName()).ifPresent(v -> source.put(FieldNames.NAME, v));
            source.put(FieldNames.DURATION_MS, obj.getDurationMs());
            ofNullable(obj.getAlbumDisplayName()).ifPresent(
                v -> source.put(FieldNames.ALBUM_DISPLAY_NAME, v)
            );
            ofNullable(obj.getArtistDisplayName()).ifPresent(
                v -> source.put(FieldNames.ARTIST_DISPLAY_NAME, v)
            );
            ofNullable(obj.getIsrc()).ifPresent(v -> source.put(FieldNames.ISRC, v));
            return source;
        }

        public String id(Song obj) {
            return String.valueOf(obj.getId());
        }
    }
}
