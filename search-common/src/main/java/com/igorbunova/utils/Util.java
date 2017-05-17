package com.igorbunova.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.igorbunova.model.Song;
import org.apache.commons.io.IOUtils;

/**
 * ConfigUtil.
 */
public class Util {
    private Util() {}

    public static Config loadConfig(String resourcePath) {
        URL location = Optional.of(Thread.currentThread().getContextClassLoader())
            .map(cl -> cl.getResource(resourcePath))
            .orElseThrow(() -> new ConfException("Can't locate config " + resourcePath));
        return ConfigFactory.parseURL(location);
    }
    
    public static String loadMapping(String resourcePath) {
        return load(Thread.currentThread().getContextClassLoader(), resourcePath);
    }
    
    private static String load(ClassLoader cl, String resourcePath) {
        try (InputStream is = cl.getResourceAsStream(resourcePath)) {
            return IOUtils.toString(
                Optional.ofNullable(is)
                    .orElseThrow(() -> new IOException("Can't get resource " + resourcePath))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectMapper create() {
        ObjectMapper om = new ObjectMapper();
        om.addMixIn(Song.class, SongMixIn.class);
        return om;
    }
}
