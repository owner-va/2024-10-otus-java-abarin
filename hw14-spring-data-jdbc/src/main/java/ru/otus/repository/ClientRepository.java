package ru.otus.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.otus.model.Client;

public interface ClientRepository extends ListCrudRepository<Client, Long> {
}
