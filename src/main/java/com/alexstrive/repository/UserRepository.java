package com.alexstrive.repository;

import com.alexstrive.PasswordValidator;
import com.alexstrive.model.User;
import io.micronaut.context.annotation.Bean;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Bean
public class UserRepository extends AbstractRepository<User> {
    private final PasswordValidator passwordValidator;

    public UserRepository(PasswordValidator passwordValidator) {
        this.passwordValidator = passwordValidator;
    }

    @Override
    public List<User> getAll() {
        var sql = "SELECT * FROM public.user";
        var users = new ArrayList<User>();

        try (var conn = getConnection(); var stmt = conn.createStatement(); var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                var user = new User(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)
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
        var sql = "SELECT * FROM public.user WHERE id = ?";
        User user;

        try (var conn = getConnection(); var stmt = conn.prepareStatement(sql);) {
            stmt.setLong(1, id);

            try (var rs = stmt.executeQuery();) {
                rs.next();
                user = new User(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)
                );
            }

            return Optional.of(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> create(User entity) {

        var sql = "INSERT INTO public.user (firstname, lastname, email, password) VALUES (?, ?, ?, ?)";

        try (var conn = getConnection(); var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var hashedPassword = passwordValidator.hashPassword(entity.password());

            stmt.setString(1, entity.firstName());
            stmt.setString(2, entity.lastName());
            stmt.setString(3, entity.email());
            stmt.setString(4, new String(hashedPassword, StandardCharsets.UTF_8));

            stmt.executeUpdate();
            var genKeys = stmt.getGeneratedKeys();
            genKeys.next();
            var id = genKeys.getInt(1);
            return Optional.of(new User(id, entity.firstName(), entity.lastName(), entity.email(), ""));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return Optional.empty();
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
