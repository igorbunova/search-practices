package com.igorbunova.examples;

import java.util.List;
import com.beust.jcommander.IStringConverter;
import com.typesafe.config.Config;
import com.igorbunova.es.ClientFactory;
import com.igorbunova.es.service.async.PutSongs;
import com.igorbunova.model.Song;
import org.apache.solr.client.solrj.SolrClient;
import org.elasticsearch.client.Client;
import com.igorbunova.service.async.AsyncOperation;
import com.igorbunova.utils.Util;

/**
 * OpConverter.
 */
public class OpConverter implements IStringConverter<AsyncOperation<List<Song>, ?>> {
    @Override
    public AsyncOperation<List<Song>, ?> convert(String s) {
        if ("es".equalsIgnoreCase(s)) {
            Config esConf = Util.loadConfig("es/es.conf");

            String index = esConf.getString("index");
            String type = esConf.getString("type");
            Client es = ClientFactory.create(esConf);

            return new PutSongs(es, index, type);
        }

        if ("solr".equalsIgnoreCase(s)) {
            Config solrConf = Util.loadConfig("solr/solr.conf");
            String zkAddr = solrConf.getString("zookeeper");
            String collection = solrConf.getString("collection");
            int commitWithinMs = solrConf.getInt("commit.within.ms");
            SolrClient sc = com.igorbunova.solr.ClientFactory.cloud(zkAddr);

            return new com.igorbunova.solr.service.async.PutSongs(sc, collection, commitWithinMs);
        }
        throw new RuntimeException("Unsupported engine " + s);
    }
}
