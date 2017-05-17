package com.igorbunova.utils;

import java.io.IOException;
import com.beust.jcommander.IStringConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.igorbunova.model.Song;

/**
 * SongJsonConverter.
 */
public class SongJsonConverter implements IStringConverter<Song> {
    private final ObjectMapper om;

    public SongJsonConverter() {
        om = Util.create();
    }

    @Override
    public Song convert(String s) {
        try {
            return om.readValue(s, Song.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't convert " + s + " to Song object", e);
        }
    }
}
