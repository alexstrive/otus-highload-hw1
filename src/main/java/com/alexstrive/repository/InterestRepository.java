package com.alexstrive.repository;

import com.alexstrive.model.Interest;
import io.micronaut.context.annotation.Bean;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Bean
public class InterestRepository extends AbstractRepository<Interest> {
    @Override
    public List<Interest> getAll() {
        var sql = "SELECT id, name FROM public.interest";
        var interests = new ArrayList<Interest>();

        try (var conn = getConnection(); var stmt = conn.createStatement(); var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                var interest = new Interest(
                        rs.getInt(1),
                        rs.getString(2)
                );

                interests.add(interest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return interests;
    }

    @Override
    public Optional<Interest> getById(Long id) {
        var sql = "SELECT id, name FROM public.interest WHERE id = ?";
        Interest interest = null;

        try (var conn = getConnection(); var stmt = conn.prepareStatement(sql);) {
            stmt.setLong(1, id);

            try (var rs = stmt.executeQuery();) {
                rs.next();
                interest = new Interest(
                        rs.getInt(1),
                        rs.getString(2)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(interest);
    }

    @Override
    public Optional<Interest> create(Interest entity) {
        var sql = "INSERT INTO public.interest (name) VALUES (?)";
        Interest interest = null;

        try (var conn = getConnection(); var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.name());

            stmt.executeUpdate();
            var genKeys = stmt.getGeneratedKeys();
            genKeys.next();
            var id = genKeys.getInt(1);
            interest = new Interest(id, entity.name());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return Optional.ofNullable(interest);
    }

    public void assignInterestsToUserById(Long userId, List<Long> interestIds) {
        var sql = "INSERT INTO public.user_interest (user_id, interest_id) VALUES (?, ?)";

        try (var conn = getConnection(); var stmt = conn.prepareStatement(sql)) {
            for (var interestId : interestIds) {
                stmt.setLong(1, userId);
                stmt.setLong(2, interestId);
                stmt.addBatch();
            }

            stmt.executeBatch();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public List<Interest> getInterestsByUserId(Long userId) {
        var sql = """
                SELECT id, name FROM interest
                JOIN public.user_interest ui ON interest.id = ui.interest_id
                WHERE user_id = ?
                """;

        List<Interest> interests = null;

        try (var conn = getConnection(); var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            var rs = stmt.executeQuery();
            interests = new ArrayList<>();

            while (rs.next()) {
                interests.add(new Interest(rs.getLong(1), rs.getString(2)));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return interests;
    }

    @Override
    public boolean updateById(Long id, Interest entity) {
        return false;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }
}
