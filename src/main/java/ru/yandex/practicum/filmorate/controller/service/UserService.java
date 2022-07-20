package ru.yandex.practicum.filmorate.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
public class UserService {
    @Autowired
    UserStorage userStorage;
}
