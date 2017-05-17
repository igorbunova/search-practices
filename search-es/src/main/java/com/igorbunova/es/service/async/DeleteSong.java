package com.igorbunova.es.service.async;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Client;
import rx.Observable;
import com.igorbunova.service.async.AsyncOperation;

/**
 * DeleteSong.
 */
public class DeleteSong implements AsyncOperation<Long, String> {

    private final Client client;
    private final String index;
    private final String type;

    public DeleteSong(Client client, String index, String type) {
        this.client = client;
        this.index = index;
        this.type = type;
    }

    @Override
    public Observable<String> apply(Long id) {
        return Observable.from(client.prepareDelete(index, type, String.valueOf(id))
            .execute()).map(DeleteResponse::getId);
    }
}
