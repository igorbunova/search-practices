package com.igorbunova.es.service.async;

import java.util.List;
import java.util.stream.StreamSupport;
import com.igorbunova.model.Song;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Client;
import rx.Observable;
import com.igorbunova.service.async.AsyncOperation;

/**
 * PutSongs.
 */
public class PutSongs implements AsyncOperation<List<Song>, Long> {
    private final Client client;
    private final String index;
    private final String type;
    private final PutSong.EsMapping sm = new PutSong.EsMapping();

    public PutSongs(Client client, String index, String type) {

        this.client = client;
        this.index = index;
        this.type = type;
    }

    public Observable<Long> apply(List<Song> songs) {
        return Observable.from(songs)
            .map(obj ->
                client.prepareUpdate(index, type, sm.id(obj))
                    .setUpsert(sm.source(obj))
                    .setDoc(sm.source(obj))
            )
            .reduce(client.prepareBulk(), BulkRequestBuilder::add)
            .flatMap(br -> Observable.from(br.execute()))
            .map(resp -> StreamSupport.stream(resp.spliterator(), false)
                .filter(i -> !i.isFailed())
                .count()
            )
            .doOnNext(l -> System.out.println(l + " docs indexed"));
    }
}
