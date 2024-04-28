package com.alexstrive.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record UserLoginDTO(String username, String password) {
}
