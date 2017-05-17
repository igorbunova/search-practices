package com.igorbunova.model;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

/**
 * Translation.
 */
public class Translation {
    private final long id;
    private final long songId;
    private final String language;
    private final String field;
    private final String value;

    private Translation(long id,
                        long songId,
                        String language,
                        String field,
                        String value) {
        this.id = id;
        this.songId = songId;
        this.language = language;
        this.field = field;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public long getSongId() {
        return songId;
    }

    public String getLanguage() {
        return language;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public static final class Builder {
        private Optional<Long> id = empty();
        private Optional<Long> songId = empty();
        private Optional<String> language = empty();
        private Optional<String> field = empty();
        private Optional<String> value = empty();

        private Builder() {}

        public Builder id(long value) {
            id = of(value);
            return this;
        }

        public Builder songId(long value) {
            songId = of(value);
            return this;
        }

        public Builder language(String value) {
            language = ofNullable(value);
            return this;
        }

        public Builder field(String value) {
            field = ofNullable(value);
            return this;
        }

        public Builder value(String value) {
            this.value = ofNullable(value);
            return this;
        }

        public Translation build() {
            return new Translation(
                id.orElseThrow(ModelException::new),
                songId.orElseThrow(ModelException::new),
                language.orElseThrow(ModelException::new),
                field.orElseThrow(ModelException::new),
                value.orElseThrow(ModelException::new)
            );
        }
    }
}
