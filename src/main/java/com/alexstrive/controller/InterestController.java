package com.alexstrive.controller;

import com.alexstrive.model.Interest;
import com.alexstrive.repository.InterestRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@Controller("interest")
@Tag(name="interest")
public class InterestController {
    InterestRepository interestRepository;

    public InterestController(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    @Get
    public List<Interest> getInterests() {
        return interestRepository.getAll();
    }

    @Get("{id}")
    public Optional<Interest> getInterestById(@PathVariable Long id) {
        return interestRepository.getById(id);
    }
}
