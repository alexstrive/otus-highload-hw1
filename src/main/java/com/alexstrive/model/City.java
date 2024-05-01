package com.alexstrive.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record City(int id, String name) {
    public City(String name) {
        this(-1, name);
    }
}
