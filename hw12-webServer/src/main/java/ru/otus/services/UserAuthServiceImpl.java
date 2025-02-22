package ru.otus.services;

import ru.otus.db.repository.DataTemplate;
import ru.otus.db.sessionmanager.TransactionManager;
import ru.otus.model.User;

import java.util.List;
import java.util.Optional;

public class UserAuthServiceImpl implements UserAuthService {
    private final DataTemplate<User> template;
    private final TransactionManager transactionManager;

    public UserAuthServiceImpl(DataTemplate<User> template, TransactionManager transactionManager) {
        this.template = template;
        this.transactionManager = transactionManager;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return getUserByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User with login '" + login + "' not found"))
                .getPassword()
                .equals(password);
    }

    private Optional<User> getUserByLogin(String login) {
        List<User> users = transactionManager.doInReadOnlyTransaction(
                session -> template.findByEntityField(session, "login", login));

        if (users.size() > 1) {
            throw new IllegalStateException("More than one user found with login: " + login);
        }

        return users.isEmpty() ? Optional.empty() : Optional.of(users.getFirst());
    }
}
