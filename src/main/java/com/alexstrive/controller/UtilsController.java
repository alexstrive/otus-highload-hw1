package com.alexstrive.controller;

import com.alexstrive.model.City;
import com.alexstrive.model.Gender;
import com.alexstrive.model.Interest;
import com.alexstrive.model.User;
import com.alexstrive.repository.CityRepository;
import com.alexstrive.repository.InterestRepository;
import com.alexstrive.repository.UserRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller("utils")
public class UtilsController {
    CityRepository cityRepository;
    UserRepository userRepository;
    InterestRepository interestRepository;

    public UtilsController(CityRepository cityRepository, UserRepository userRepository, InterestRepository interestRepository) {
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;
    }

    @Get("seed")
    public void seed() {
        if (cityRepository.getAll().toArray().length == 0) {
            var spb = new City("Saint-Petersburg");
            var moscow = new City("Moscow");
            var ykb = new City("Yekaterinburg");
            cityRepository.create(spb);
            cityRepository.create(moscow);
            cityRepository.create(ykb);
        }

        if (interestRepository.getAll().toArray().length == 0) {
            var cycling = new Interest("Cycling");
            var reading = new Interest("Reading");
            var surfing = new Interest("Surfing");
            var dance = new Interest("Dance");
            var ww = new Interest("Woodworking");
            var photo = new Interest("Photography");
            interestRepository.create(cycling);
            interestRepository.create(reading);
            interestRepository.create(surfing);
            interestRepository.create(dance);
            interestRepository.create(ww);
            interestRepository.create(photo);
        }

        if (userRepository.getAll().toArray().length == 0) {
            var allInterests = interestRepository.getAll();
            var reading = allInterests.stream().filter(interest -> Objects.equals(interest.name(), "Reading")).findFirst().get();
            var woodworking = allInterests.stream().filter(interest -> Objects.equals(interest.name(), "Woodworking")).findFirst().get();
            var photography = allInterests.stream().filter(interest -> Objects.equals(interest.name(), "Photography")).findFirst().get();
            var cities = cityRepository.getAll();
            var spb = cities.getFirst();
            var ykb = cities.getLast();
            var alex = new User("Alexey", "Novopashin", "alexei.novoppashin@gmail.com", "1234", LocalDate.now(), Gender.MALE, List.of(reading), spb);
            var john = new User("John", "Smith", "john.smith@gmail.com", "5678", LocalDate.now(), Gender.MALE, List.of(photography, woodworking), ykb);
            userRepository.create(alex);
            userRepository.create(john);
        }
    }
}
