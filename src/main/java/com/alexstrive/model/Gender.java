package com.alexstrive.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public enum Gender {
    MALE,
    FEMALE;


    @Override
    public String toString() {
        if (this == MALE) {
            return "MALE";
        } else {
            return "FEMALE";
        }
    }
}
