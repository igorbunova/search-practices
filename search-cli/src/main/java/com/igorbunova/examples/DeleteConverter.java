package com.igorbunova.examples;

import com.beust.jcommander.IStringConverter;
import com.igorbunova.es.ClientFactory;
import com.igorbunova.es.service.async.DeleteSong;
import com.igorbunova.service.async.AsyncOperation;
import com.igorbunova.utils.Util;
import com.typesafe.config.Config;
import org.apache.solr.client.solrj.SolrClient;
import org.elasticsearch.client.Client;

/**
 * DeleteConverter.
 */
public class DeleteConverter implements IStringConverter<AsyncOperation<Long, ?>> {
    @Override
    public AsyncOperation<Long, ?> convert(String s) {
        if ("es".equalsIgnoreCase(s)) {
            Config esConf = Util.loadConfig("es.conf");

            String index = esConf.getString("index");
            String type = esConf.getString("type");
            Client es = ClientFactory.create(esConf);

            return new DeleteSong(es, index, type);
        }

        if ("solr".equalsIgnoreCase(s)) {
            Config solrConf = Util.loadConfig("solr.conf");
            String zkAddr = solrConf.getString("zookeeper");
            String collection = solrConf.getString("collection");
            int commitWithinMs = solrConf.getInt("commit.within.ms");
            SolrClient sc = com.igorbunova.solr.ClientFactory.cloud(zkAddr);

            return new com.igorbunova.solr.service.async.DeleteSong(sc, collection,
                commitWithinMs);
        }
        throw new RuntimeException("Unsupported engine " + s);
    }
}
