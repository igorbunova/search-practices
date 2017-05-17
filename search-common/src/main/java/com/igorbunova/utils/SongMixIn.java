package com.igorbunova.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SongMixIn.
 */
final class SongMixIn {
    @JsonCreator
    public SongMixIn(@JsonProperty("id") long id,
                     @JsonProperty("name") String name,
                     @JsonProperty("artist_display_name") String artistDisplayName,
                     @JsonProperty("album_display_name") String albumDisplayName,
                     @JsonProperty("duration_ms") int durationMs,
                     @JsonProperty("isrc") String isrc) {
    }
}
