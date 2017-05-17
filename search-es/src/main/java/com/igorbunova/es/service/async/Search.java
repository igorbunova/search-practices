package com.igorbunova.es.service.async;

import java.util.LinkedHashMap;
import java.util.Map;
import com.igorbunova.model.FieldNames;
import com.igorbunova.service.async.AsyncSearch;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import rx.Observable;

/**
 * Search.
 */
public class Search implements AsyncSearch<Map<String, Object>> {
    private final Client client;
    private final String index;
    private final String type;

    public Search(Client client, String index, String type) {
        this.client = client;
        this.index = index;
        this.type = type;
    }

    public Observable<Map<String, Object>> apply(String query, int offset, int limit) {
        return Observable.from(client.prepareSearch(index)
            .setTypes(type)
            .setQuery(QueryBuilders.multiMatchQuery(query, FieldNames.ARTIST_DISPLAY_NAME,
                FieldNames.ALBUM_DISPLAY_NAME, FieldNames.NAME))
            .setFrom(offset)
            .setSize(limit)
            .execute())
            .flatMap(r -> Observable.from(r.getHits()))
            .map(h -> {
                Map<String, Object> res = new LinkedHashMap<>();
                res.put(FieldNames.ID, h.getId());
                res.putAll(h.getSource());
                return res;
            });
    }
}
