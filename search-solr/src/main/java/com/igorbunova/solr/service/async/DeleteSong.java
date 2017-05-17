package com.igorbunova.solr.service.async;

import com.igorbunova.service.async.AsyncOperation;
import com.igorbunova.solr.SolrException;
import org.apache.solr.client.solrj.SolrClient;
import rx.Observable;

/**
 * DeleteSong.
 */
public class DeleteSong implements AsyncOperation<Long, Long> {

    private final SolrClient client;
    private final String collection;
    private final int commitWithinMs;

    public DeleteSong(SolrClient client, String index, int commitWithinMs) {
        this.client = client;
        this.collection = index;
        this.commitWithinMs = commitWithinMs;
    }

    @Override
    public Observable<Long> apply(Long id) {
        return Observable.just(id)
            .map(i -> {
                try {
                    return client.deleteById(collection, String.valueOf(i), commitWithinMs);
                } catch (Exception e) {
                    throw new SolrException(e);
                }
            })
            .map(ur -> {
                if (ur.getStatus() != 0) {
                    try {
                        client.rollback(collection);
                    } catch (Exception e) {
                        throw new SolrException(e);
                    }
                    return 0L;
                } else {
                    return id;
                }
            });
    }
}
