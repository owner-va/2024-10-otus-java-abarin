package ru.otus.services;

import ru.otus.db.repository.DataTemplate;
import ru.otus.db.sessionmanager.TransactionManager;
import ru.otus.model.User;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final DataTemplate<User> template;
    private final TransactionManager transactionManager;

    public UserServiceImpl(DataTemplate<User> template, TransactionManager transactionManager) {
        this.template = template;
        this.transactionManager = transactionManager;
    }

    @Override
    public Optional<User> getUser(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> template.findById(session, id));
    }

    @Override
    public Optional<User> getRandomUser() {
        return transactionManager.doInReadOnlyTransaction(template::findAll).stream()
                .findAny();
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        List<User> users = transactionManager.doInReadOnlyTransaction(
                session -> template.findByEntityField(session, "login", login));

        if (users.size() > 1) {
            throw new IllegalStateException("More than one user found with login: " + login);
        }

        return users.isEmpty() ? Optional.empty() : Optional.of(users.getFirst());
    }
}
