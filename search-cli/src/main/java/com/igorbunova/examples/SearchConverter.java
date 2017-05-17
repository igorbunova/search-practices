package com.igorbunova.examples;

import java.util.Map;
import com.beust.jcommander.IStringConverter;
import com.typesafe.config.Config;
import com.igorbunova.es.ClientFactory;
import org.apache.solr.client.solrj.SolrClient;
import org.elasticsearch.client.Client;
import com.igorbunova.service.async.AsyncSearch;
import com.igorbunova.utils.Util;

/**
 * SearchConvertert.
 */
public class SearchConverter implements IStringConverter<AsyncSearch<Map<String, Object>>> {
    @Override
    public AsyncSearch<Map<String, Object>> convert(String s) {
        if ("es".equalsIgnoreCase(s)) {
            Config esConf = Util.loadConfig("es/es.conf");

            String index = esConf.getString("index");
            String type = esConf.getString("type");
            Client es = ClientFactory.create(esConf);

            return new com.igorbunova.es.service.async.Search(es, index, type);
        }

        if ("solr".equalsIgnoreCase(s)) {
            Config solrConf = Util.loadConfig("solr/solr.conf");
            String zkAddr = solrConf.getString("zookeeper");
            String collection = solrConf.getString("collection");
            SolrClient sc = com.igorbunova.solr.ClientFactory.cloud(zkAddr);

            return new com.igorbunova.solr.service.async.Search(sc, collection);
        }
        throw new RuntimeException("Unsupported engine " + s);
    }
}
