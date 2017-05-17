package com.igorbunova.examples;

import java.util.Map;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.igorbunova.service.async.AsyncSearch;
import com.igorbunova.utils.Util;

/**
 * SolrSearch.
 */
@Parameters(commandDescription = "search across Solr")
public class Search implements Runnable {

    private String query = null;
    private int offset = 0;
    private int limit = 10;
    private AsyncSearch<Map<String, Object>> engine = null;

    private static final Logger LOG = LoggerFactory.getLogger(Search.class);

    @Parameter(names = {"--query", "-q"}, required = true)
    public void setQuery(String query) {
        this.query = query;
    }
    @Parameter(names = {"--offset", "-o"})
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Parameter(names = {"--limit", "-l"})
    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Parameter(names = {"--engine", "-e"}, converter = SearchConverter.class,
        description = "possible values: es, solr",
        required = true)
    public void setEngine(AsyncSearch<Map<String, Object>> engine) {
        this.engine = engine;
    }
    @Override
    public void run() {
        ObjectMapper om = Util.create();

        engine.apply(query, offset, limit)
            .map(value -> {
                try {
                    return om.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            })
            .doOnNext(LOG::info)
            .doOnError(th -> LOG.error("Error occurred", th))
            .subscribe();
    }

}
