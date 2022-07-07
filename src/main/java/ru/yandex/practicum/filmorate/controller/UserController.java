package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Контроллер для управления пользователями
 */
@Slf4j
@RestController
@RequestMapping("users")
public class UserController implements Controller<User> {
    private final Map<Integer, User> storage = new HashMap<>();
    private int lastId = 1;

    @Override
    @PostMapping()
    public User add(@Valid @RequestBody User user) {
        validate(user);
        user.setId(lastId);
        lastId++;
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.trace("Имя нового пользователя пусто. Пользователь " + user.getId());
        }
        storage.put(user.getId(), user);
        log.info("Добавлен фильм: " + user);
        return user;
    }

    @Override
    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        if (storage.containsKey(user.getId())) {
            validate(user);
            storage.put(user.getId(), user);
            log.info("Обновлена запись пользователя id: " + user.getId());
            return user;
        }
        log.error("Ошибка обновления фильма с id: " + user.getId());
        throw new RuntimeException("Пользователя нет в базе. Невозможно обновить.");
    }

    @Override
    @GetMapping()
    public List<User> getAll() {
        log.info("Получен запрос списка всех пользователей в базе.");
        return new ArrayList<>(storage.values());
    }


    public void validate(User user) {
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            String msg = "Ошибка валидации пользователя. Неверная формат логина. " + user.getId();
            log.warn(msg);
            throw new ValidationException(msg);
        }
        if (user.getEmail().isBlank() || (!user.getEmail().contains("@"))) {
            String msg = "Ошибка валидации пользователя. Неверная формат email. " + user.getId();
            log.warn(msg);
            throw new ValidationException(msg);
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            String msg = "Ошибка валидации пользователя. Неверная дата рождения. " + user.getId();
            log.warn(msg);
            throw new ValidationException(msg);
        }
        log.trace("Успешная валидация пользователя " + user);
    }
}
