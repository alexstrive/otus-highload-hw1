package com.alexstrive.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.annotation.Nullable;

@Serdeable
public record User(int id,
                   String firstName,
                   String lastName,
                   String email,
                   @JsonIgnore String password) {
}

