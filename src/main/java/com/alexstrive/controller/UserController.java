package com.alexstrive.controller;

import com.alexstrive.Constants;
import com.alexstrive.model.City;
import com.alexstrive.model.User;
import com.alexstrive.model.UserCreateDTO;
import com.alexstrive.repository.CityRepository;
import com.alexstrive.repository.InterestRepository;
import com.alexstrive.repository.UserRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.session.Session;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller("user")
@Tag(name = "user")
public class UserController {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final InterestRepository interestRepository;
    private final Constants constants;

    public UserController(CityRepository cityRepository,
                          InterestRepository interestRepository,
                          UserRepository userRepository, Constants constants) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.interestRepository = interestRepository;
        this.constants = constants;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<List<User>> getAllUsers(Session session) {
        if (session.get(constants.USER_SESSION_LOGIN_FIELD, String.class).isEmpty()) {
            return HttpResponse.unauthorized();
        }

        return HttpResponse.ok(userRepository.getAll());
    }

    @Get(uri = "get/{id}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<User> getUserById(Session session, @PathVariable Long id) {
        if (session.get(constants.USER_SESSION_LOGIN_FIELD, String.class).isEmpty()) {
            return HttpResponse.unauthorized();
        }

        var user = userRepository.getById(id);

        if (user.isEmpty()) {
            return HttpResponse.notFound();
        }

        return HttpResponse.ok(user.get());
    }

    @Post(uri = "register")
    public HttpResponse register(Session session, @Body UserCreateDTO userCreateDTO) {
        var city = cityRepository.getById(userCreateDTO.cityId());

        if (city.isEmpty()) {
            return HttpResponse.notFound("City with provided id not found");
        }

        // TODO: make special method for `interest-in-set` operation
        var interests = interestRepository.getAll()
                .stream()
                .filter(interest -> userCreateDTO.interestIds().contains(interest.id()))
                .toList();

        var newUser = userRepository.create(
                new User(-1,
                        userCreateDTO.firstName(),
                        userCreateDTO.lastName(),
                        userCreateDTO.email(),
                        userCreateDTO.password(),
                        userCreateDTO.birthday(),
                        userCreateDTO.gender(),
                        interests,
                        city.get()
                )
        );

        if (newUser.isEmpty()) {
            return HttpResponse.badRequest("Unable to create a new user, see logs.");
        }

        return HttpResponse.created(newUser.get(), URI.create("/user/get/" + newUser.get().id()));
    }
}
