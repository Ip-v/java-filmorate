package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @Order(1)
    @Test
    void save() {
        User user = new User(null, -1, "mail@mail.ru", "ll", "Name",
                LocalDate.of(1990, 1, 1));
        assertTrue(userDbStorage.save(user).getId() != -1);
        assertThrows(ValidationException.class, () -> userDbStorage.save(user));
    }

    @Order(2)
    @Test
    void update() {
        User user = new User(null, 1, "mail1@mail.ru", "ll", "Name",
                LocalDate.of(1990, 1, 1));
        assertEquals("Name", userDbStorage.update(user).getName());
    }

    @Order(3)
    @Test
    void get() {
        assertThat(userDbStorage.get(2)).hasFieldOrPropertyWithValue("name", "username2");
    }

    @Order(1)
    @Test
    void getAll() {
        assertEquals(2, userDbStorage.getAll().size());
    }

    @Order(4)
    @Test
    void delete() {
        assertNotNull(userDbStorage.get(1));
        userDbStorage.delete(1);
        assertNull(userDbStorage.get(1));
    }
}