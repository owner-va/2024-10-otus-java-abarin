package ru.otus.services;

import ru.otus.dto.ClientDto;

import java.util.List;
import java.util.Optional;

public interface ServiceClient {

    ClientDto saveClient(ClientDto clientDTO);

    Optional<ClientDto> getClient(long id);

    List<ClientDto> findAll();
}
