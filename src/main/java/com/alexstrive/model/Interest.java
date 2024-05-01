package com.alexstrive.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Interest(long id, String name) {
    public Interest(String name) {
        this(-1, name);
    }
}
