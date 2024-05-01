package com.alexstrive.repository;

import com.alexstrive.model.City;
import io.micronaut.context.annotation.Bean;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Bean
public class CityRepository extends AbstractRepository<City> {
    @Override
    public List<City> getAll() {
        var sql = "SELECT id, name FROM public.city";
        var cities = new ArrayList<City>();

        try (var conn = getConnection(); var stmt = conn.createStatement(); var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                var city = new City(
                        rs.getInt(1),
                        rs.getString(2)
                );

                cities.add(city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cities;
    }

    @Override
    public Optional<City> getById(Long id) {
        var sql = "SELECT id, name FROM public.city WHERE id = ?";
        City city = null;

        try (var conn = getConnection(); var stmt = conn.prepareStatement(sql);) {
            stmt.setLong(1, id);

            try (var rs = stmt.executeQuery();) {
                rs.next();
                city = new City(
                        rs.getInt(1),
                        rs.getString(2)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(city);
    }

    @Override
    public Optional<City> create(City entity) {
        var sql = "INSERT INTO public.city (name) VALUES (?)";

        try (var conn = getConnection(); var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.name());

            stmt.executeUpdate();
            var genKeys = stmt.getGeneratedKeys();
            genKeys.next();
            var id = genKeys.getInt(1);
            return Optional.of(new City(id, entity.name()));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean updateById(Long id, City entity) {
        return false;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }
}
