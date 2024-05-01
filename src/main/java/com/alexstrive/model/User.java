package com.alexstrive.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

@Serdeable
public record User(long id,
                   String firstName,
                   String lastName,
                   String email,
                   @JsonIgnore String password,
                   LocalDate birthday,
                   Gender gender,
                   List<Interest> interests,
                   City city) implements Cloneable {

    public User(String firstName, String lastName, String email, String password, LocalDate birthday, Gender gender, List<Interest> interests, City city) {
        this(-1, firstName, lastName, email, password, birthday, gender, interests, city);
    }

    public User(long id, User user) {
        this(id, user.firstName, user.lastName, user.email, user.password, user.birthday, user.gender, user.interests, user.city);
    }
}

