package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер управления пользователями
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController implements Controller<User> {
    @Autowired
    UserService userService;

    /**
     * Получение пользователя по ид
     *
     * @param userId ид пользователя
     * @return объект User
     */
    @GetMapping({"/{userId}"})
    public User getUser(@PathVariable int userId) {
        log.info("Получен запрос получения данных пользователя ID " + userId);
        return userService.get(userId);
    }

    @Override
    @GetMapping()
    public List<User> getAll() {
        log.info("Получен запрос списка всех пользователей в базе.");
        return userService.getAll();
    }

    /**
     * Получение списка друзей
     *
     * @param userId ид пользователя
     * @return список друзей
     */
    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable int userId) {
        log.info("Запрос получения списка друзей пользователя id: " + userId);
        User user = userService.get(userId);
        return userService.getFriends(user);
    }

    /**
     * Получение списка общих друзей у пользователей
     */
    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int userId, @PathVariable int otherId) {
        log.info("Запрос получения списка общих друзей пользователей: " + userId + ", " + otherId);
        User user = userService.get(userId);
        User otherUser = userService.get(otherId);
        return userService.getCommonFriends(user, otherUser);

    }

    @Override
    @PostMapping()
    public User save(@RequestBody @Valid User user) {
        log.info("Запрос добавления записи пользователя id: " + user);
        validate(user);
        return userService.save(user);
    }

    /**
     * Добавление пользователя в список друзей
     */
    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable int userId, @PathVariable int friendId) {
        log.info("Запрос добавления в друзья пользователей с id : " + userId + ", " + friendId);
        User user = userService.get(userId);
        User friend = userService.get(friendId);
        userService.addFriend(user, friend);
    }

    @Override
    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        log.info("Запрос обновления записи пользователя id: " + user.getId());
        return userService.update(user);
    }

    /**
     * Удаление пользователя из списка друзей
     */
    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable int userId, @PathVariable int friendId) {
        User user = userService.get(userId);
        User friend = userService.get(friendId);
        userService.removeFriend(user, friend);
    }

    @Override
    public void validate(User user) {
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            String msg = "Ошибка валидации пользователя. Неверная формат логина. " + user.getId();
            log.warn(msg);
            throw new ValidationException(msg);
        }
        if (user.getEmail().isBlank() || (!user.getEmail().contains("@"))) {
            String msg = "Ошибка валидации пользователя. Неверный формат email. " + user.getId();
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
