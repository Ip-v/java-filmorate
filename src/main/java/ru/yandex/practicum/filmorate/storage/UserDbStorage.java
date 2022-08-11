package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User save(User user) {
        if (get(user.getId()) != null) {
            throw new ValidationException("Пользователь с id = " + user.getId() + " уже есть в базе.");
        }
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("USERS").usingGeneratedKeyColumns("USER_ID");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("USER_EMAIL", user.getEmail())
                .addValue("USER_LOGIN", user.getLogin())
                .addValue("USER_NAME", user.getName())
                .addValue("USER_BIRTHDAY", user.getBirthday());

        Number num = jdbcInsert.executeAndReturnKey(parameters);
        user.setId(num.intValue());
        return user;
    }

    @Override
    public User update(User user) {
        if (get(user.getId()) == null) {
            throw new UserDoesNotExistException("Пользователя с id = " + user.getId() + " нет в базе.");
        }
        String sql = "update USERS set " +
                "USER_EMAIL=?," +
                "USER_LOGIN=?," +
                "USER_NAME=?," +
                "USER_BIRTHDAY=? " +
                "where USER_ID = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        updateFriendsIds(user);
        return user;
    }

    private void updateFriendsIds(User user) {
        String deleteSql = "delete from FRIENDSHIP where USER_ID=?";
        jdbcTemplate.update(deleteSql, user.getId());

        String insertSql = "insert into friendship (USER_ID, FRIEND_ID) values (?,?)";
        if (user.getFriendsIds() != null) {
            user.getFriendsIds().forEach(id -> jdbcTemplate.update(insertSql, user.getId(), id));
        }
    }

    @Override
    public User get(Integer id) {
        String sql = "select * from USERS where USER_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::createUserFromRow, id);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    private User createUserFromRow(ResultSet resultSet, int id) throws SQLException {
        if (resultSet.getRow() == 0) {
            throw new UserDoesNotExistException("Пользователь не найден в базе. ИД = " + id);
        }
        User user = new User(
                null,
                resultSet.getInt("USER_ID"),
                resultSet.getString("USER_EMAIL"),
                resultSet.getString("USER_LOGIN"),
                resultSet.getString("USER_NAME"),
                resultSet.getDate("USER_BIRTHDAY").toLocalDate()
        );
        user.setFriendsIds(getFriendsIdsByUserId(user.getId()));
        return user;
    }

    private Set<Integer> getFriendsIdsByUserId(Integer id) {
        String sql = "select FRIEND_ID from friendship where USER_ID=?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, num) -> rs.getInt("friend_id"), id));
    }

    @Override
    public List<User> getAll() {
        String sql = "select * from USERS";
        return jdbcTemplate.query(sql, this::createUserFromRow);
    }

    @Override
    public void delete(int id) {
        String sql = "delete from USERS where USER_ID = ?";
        jdbcTemplate.update(sql, id);
    }
}
