package ru.otus.services;

import ru.otus.model.Client;

import java.util.List;
import java.util.Optional;

public interface ServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
