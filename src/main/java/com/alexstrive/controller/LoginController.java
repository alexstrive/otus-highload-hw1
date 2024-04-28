package com.alexstrive.controller;

import com.alexstrive.Constants;
import com.alexstrive.model.UserLoginDTO;
import com.alexstrive.repository.UserRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.session.Session;

import java.util.Optional;

@Controller("/login")
public class LoginController {
    private final UserRepository userRepository;
    private final Constants constants;

    public LoginController(Constants constants, UserRepository userRepository) {
        this.constants = constants;
        this.userRepository = userRepository;
    }

    @Post
    public HttpResponse login(Session session, @Body UserLoginDTO userLoginDTO) {
        if (userRepository.login(userLoginDTO.username(), userLoginDTO.password())) {
            session.put(constants.USER_SESSION_LOGIN_FIELD, userLoginDTO.username());
            return HttpResponse.ok();
        }

        return HttpResponse.unauthorized();
    }

    @Get(uri = "whoami", consumes = MediaType.ALL, produces = MediaType.TEXT_PLAIN)
    public String getLoginInfo(Session session) {
        return session.get(constants.USER_SESSION_LOGIN_FIELD, String.class).orElse("anonymous");
    }

    @Delete
    public void logout(Session session) {
        session.remove(constants.USER_SESSION_LOGIN_FIELD);
    }
}
