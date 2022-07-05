package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Контроллер для управления пользователями
 */
@RestController
@RequestMapping("users")
public class UserController implements Controller<User> {
    private final HashMap<Integer, User> storage = new HashMap<>();
    private int lastId = 1;

    @Override
    @PostMapping()
    public User add(@Valid @RequestBody User user) {
        validate(user);
        user.setId(lastId);
        lastId++;
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        if (storage.containsKey(user.getId())) {
            validate(user);
            storage.put(user.getId(), user);
            return user;
        }
        throw new RuntimeException("Пользователя нет в базе. Невозможно обновить.");
    }

    @Override
    @GetMapping()
    public List<User> getAll() {
        return new ArrayList<>(storage.values());
    }


    public boolean validate(User user) {
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException();
        }
        if (user.getEmail().isBlank() || (!user.getEmail().contains("@"))) {
            throw new ValidationException();
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException();
        }
        return true;
    }
}
