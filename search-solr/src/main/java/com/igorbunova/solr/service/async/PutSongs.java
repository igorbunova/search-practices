package com.igorbunova.solr.service.async;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.igorbunova.model.FieldNames;
import com.igorbunova.model.Song;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import rx.Observable;
import com.igorbunova.service.async.AsyncOperation;
import com.igorbunova.solr.SolrException;

import static java.util.Optional.ofNullable;

/**
 * PutSongs.
 */
public class PutSongs implements AsyncOperation<List<Song>, Integer> {
    private final SolrClient client;
    private final String collection;
    private final int commitWithinMs;
    private final SolrMapping mapping = new SolrMapping();

    public PutSongs(SolrClient client, String collection, int commitWithinMs) {
        this.client = client;
        this.collection = collection;
        this.commitWithinMs = commitWithinMs;
    }

    public Observable<Integer> apply(List<Song> songs) {
        return Observable.from(songs)
            .map(mapping::source)
            .map(m -> {
                SolrInputDocument doc = new SolrInputDocument();
                m.forEach(doc::addField);
                return doc;
            })
            .buffer(songs.size())
            .map(l -> {
                try {
                    return client.add(collection, l, commitWithinMs);
                } catch (Exception e) {
                    throw new SolrException(e);
                }
            }).map(ur -> {
            if (ur.getStatus() != 0) {
                try {
                    client.rollback(collection);
                } catch (Exception e) {
                    throw new SolrException(e);
                }
                return 0;
            } else {
                return songs.size();
            }
        });
    }

    static final class SolrMapping {

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
            source.put(FieldNames.ID, id(obj));
            return source;
        }

        public String id(Song obj) {
            return String.valueOf(obj.getId());
        }
    }
}
