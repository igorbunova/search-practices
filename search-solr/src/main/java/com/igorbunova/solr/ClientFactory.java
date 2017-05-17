package com.igorbunova.solr;

import java.io.IOException;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;

/**
 * ClientFactory.
 */
public class ClientFactory {
    public static SolrClient cloud(String zkAddr) {
        SolrClient sc = new CloudSolrClient.Builder().withZkHost(zkAddr).build();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                sc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        return sc;
    }
}
