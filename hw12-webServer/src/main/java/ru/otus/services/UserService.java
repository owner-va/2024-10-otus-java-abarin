package ru.otus.services;

import ru.otus.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getUser(long id);

    Optional<User> getRandomUser();

    Optional<User> getUserByLogin(String login);
}
