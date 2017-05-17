package com.igorbunova.es;

import java.net.InetAddress;
import java.net.UnknownHostException;
import com.typesafe.config.Config;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * ClientFactory.
 */
public class ClientFactory {
    private ClientFactory() {}

    public static Client create(Config config) {
        InetAddress address;
        try {
            address = InetAddress.getByName(config.getString("host"));
        } catch (UnknownHostException e) {
            throw new EsException("Can't define addres", e);
        }
        int port = config.getInt("port");

        Client client = new PreBuiltTransportClient(Settings.EMPTY)
            .addTransportAddress(new InetSocketTransportAddress(address, port));
        Runtime.getRuntime().addShutdownHook(new Thread(client::close));
        return client;
    }
}
