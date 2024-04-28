package com.alexstrive.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record UserCreateDTO(String firstName,
                            String lastName,
                            String email,
                            String password) {

}
