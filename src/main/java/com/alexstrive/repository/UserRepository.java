package com.alexstrive.repository;

import com.alexstrive.PasswordValidator;
import com.alexstrive.model.City;
import com.alexstrive.model.Gender;
import com.alexstrive.model.Interest;
import com.alexstrive.model.User;
import io.micronaut.context.annotation.Bean;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Bean
public class UserRepository extends AbstractRepository<User> {
    private final PasswordValidator passwordValidator;
    private final InterestRepository interestRepository;
    private final CityRepository cityRepository;

    public UserRepository(PasswordValidator passwordValidator, InterestRepository interestRepository, CityRepository cityRepository) {
        this.passwordValidator = passwordValidator;
        this.interestRepository = interestRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public List<User> getAll() {
        var sql = "SELECT id, first_name, last_name, email, password, birthday, gender, city_id FROM public.user";
        var users = new ArrayList<User>();

        try (var conn = getConnection(); var stmt = conn.createStatement(); var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                var city = cityRepository.getById(rs.getLong(8));

                if (city.isEmpty()) {
                    throw new IllegalArgumentException("City with id " + rs.getLong(8) + " not found");
                }

                var interests = interestRepository.getInterestsByUserId(rs.getLong(1));

                var user = new User(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getObject(6, LocalDate.class),
                        Gender.valueOf(rs.getString(7)),
                        interests,
                        city.get()
                );

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public Optional<User> getById(Long id) {
        var sql = "SELECT id, first_name, last_name, email, password, birthday, gender, city_id FROM public.user WHERE id = ?";
        User user = null;

        try (var conn = getConnection(); var stmt = conn.prepareStatement(sql);) {
            stmt.setLong(1, id);

            try (var rs = stmt.executeQuery();) {
                if (rs.next()) {
                    var city = cityRepository.getById(rs.getLong(8));

                    if (city.isEmpty()) {
                        throw new IllegalArgumentException("City with id " + rs.getLong(8) + " not found");
                    }

                    var interests = interestRepository.getInterestsByUserId(rs.getLong(1));

                    user = new User(
                            rs.getLong(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getObject(6, LocalDate.class),
                            Gender.valueOf(rs.getString(7)),
                            interests,
                            city.get()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> create(User entity) {
        var sql = "INSERT INTO public.user (first_name, last_name, email, password, birthday, gender, city_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        User user = null;

        try (var conn = getConnection(); var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var hashedPassword = passwordValidator.hashPassword(entity.password());

            stmt.setString(1, entity.firstName());
            stmt.setString(2, entity.lastName());
            stmt.setString(3, entity.email());
            stmt.setString(4, new String(hashedPassword, StandardCharsets.UTF_8));
            stmt.setObject(5, entity.birthday());
            stmt.setString(6, String.valueOf(entity.gender()));
            stmt.setInt(7, entity.city().id());

            stmt.executeUpdate();
            var genKeys = stmt.getGeneratedKeys();
            genKeys.next();
            var id = genKeys.getLong(1);
            user = new User(id, entity);

            interestRepository.assignInterestsToUserById(id, entity.interests().stream().map(Interest::id).toList());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return Optional.ofNullable(user);
    }

    public boolean login(String username, String password) {
        var sql = "SELECT email, password FROM public.user WHERE email = ? AND password = ?";

        try (var conn = getConnection(); var stmt = conn.prepareStatement(sql)) {
            var hashedPassword = passwordValidator.hashPassword(password);

            stmt.setString(1, username);
            stmt.setString(2, new String(hashedPassword));

            try (var rs = stmt.executeQuery();) {
                if (rs.next()) {
                    return true;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public boolean updateById(Long id, User entity) {
        return false;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }
}
