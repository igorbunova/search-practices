package com.igorbunova.model;

/**
 * Person.
 */
public class Song {
    private final long id;
    private final String name;
    private final String artistDisplayName;
    private final String albumDisplayName;
    private final int durationMs;
    private final String isrc;

    public Song(long id,
                String name,
                String artistDisplayName,
                String albumDisplayName,
                int durationMs,
                String isrc) {
        this.id = id;
        this.name = name;
        this.artistDisplayName = artistDisplayName;
        this.albumDisplayName = albumDisplayName;
        this.durationMs = durationMs;
        this.isrc = isrc;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtistDisplayName() {
        return artistDisplayName;
    }

    public String getAlbumDisplayName() {
        return albumDisplayName;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public String getIsrc() {
        return isrc;
    }
}
