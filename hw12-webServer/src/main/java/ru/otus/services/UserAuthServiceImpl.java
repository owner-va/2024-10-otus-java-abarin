package ru.otus.services;

public class UserAuthServiceImpl implements UserAuthService {
    private final UserService userService;

    public UserAuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return userService
                .getUserByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User with login '" + login + "' not found"))
                .getPassword()
                .equals(password);
    }
}
