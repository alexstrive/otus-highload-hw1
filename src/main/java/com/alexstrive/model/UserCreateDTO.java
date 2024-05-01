package com.alexstrive.model;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

@Serdeable
public record UserCreateDTO(String firstName,
                            String lastName,
                            String email,
                            String password,
                            LocalDate birthday,
                            Gender gender,
                            List<Integer> interestIds,
                            Long cityId
) {
}
