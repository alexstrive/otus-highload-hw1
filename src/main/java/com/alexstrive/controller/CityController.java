package com.alexstrive.controller;

import com.alexstrive.model.City;
import com.alexstrive.repository.CityRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@Controller("city")
@Tag(name = "city")
public class CityController {

    CityRepository cityRepository;

    public CityController(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Get
    public List<City> getAll() {
        return cityRepository.getAll();
    }

    @Get("{id}")
    public Optional<City> getAll(@PathVariable Long id) {
        return cityRepository.getById(id);
    }
}
