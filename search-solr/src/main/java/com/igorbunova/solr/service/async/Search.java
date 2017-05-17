package com.igorbunova.solr.service.async;

import java.util.Map;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.SolrParams;
import rx.Observable;
import com.igorbunova.service.async.AsyncSearch;
import com.igorbunova.solr.SolrException;

/**
 * Search.
 */
public class Search implements AsyncSearch<Map<String, Object>> {
    private final SolrClient client;
    private final String collection;

    public Search(SolrClient client, String collection) {
        this.client = client;
        this.collection = collection;
    }

    public Observable<Map<String, Object>> apply(String query, int offset, int limit) {

        SolrParams solrParams = new SolrQuery(query)
            .setStart(offset)
            .setRows(limit);
        try {
            return Observable.from(client.query(collection, solrParams).getResults())
                .map(SolrDocument::getFieldValueMap);
        } catch (Exception e) {
            throw new SolrException(e);
        }
    }
}
