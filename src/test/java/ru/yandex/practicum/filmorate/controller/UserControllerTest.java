package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    Controller<User> controller;
    User user;

    @BeforeEach
    void init() {
        controller = new UserController();
        user = new User(null, 1, "a@", "login", "name",
                LocalDate.of(1895, 12, 28));
    }

    @Test
    void shouldValidateCorrectUser() {
        assertDoesNotThrow(() -> controller.validate(user));
    }

    @Test
    void shouldTrowExceptionWithBlankLogin() {
        user.setLogin("");
        assertThrows(ValidationException.class, () -> controller.validate(user));
    }

    @Test
    void shouldTrowExceptionWithSpaceInLogin() {
        user.setLogin("s ");
        assertThrows(ValidationException.class, () -> controller.validate(user));
    }

    @Test
    void emailShouldNotBeEmpty() {
        user.setEmail("s@ ");
        assertDoesNotThrow(() -> controller.validate(user));

        user.setEmail("");
        assertThrows(ValidationException.class, () -> controller.validate(user));

        user.setEmail("s");
        assertThrows(ValidationException.class, () -> controller.validate(user));
    }

    @Test
    void birthdayShouldBeInPast() {
        assertDoesNotThrow(() -> controller.validate(user));

        user.setBirthday(LocalDate.of(2024, 1, 1));
        assertThrows(ValidationException.class, () -> controller.validate(user));
    }
}