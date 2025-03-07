package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.dto.ClientDto;
import ru.otus.model.Client;
import ru.otus.repository.ClientRepository;
import ru.otus.sessionmanager.TransactionManager;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceClientImpl implements ServiceClient {

    private final TransactionManager transactionManager;
    private final ClientRepository clientRepository;

    public ServiceClientImpl(TransactionManager transactionManager, ClientRepository clientRepository) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDto saveClient(ClientDto clientDTO) {
        var phones = new HashSet<>(clientDTO.phones());
        var client = new Client(clientDTO.id(), clientDTO.name(), clientDTO.address(), phones);
        Client savedClient = transactionManager.doInTransaction(() -> clientRepository.save(client));

        return ClientDto.toDto(savedClient);
    }

    @Override
    public Optional<ClientDto> getClient(long id) {
        return transactionManager
                .doInTransaction(() -> clientRepository.findById(id))
                .map(ClientDto::toDto);
    }

    @Override
    public List<ClientDto> findAll() {
        return transactionManager.doInTransaction(clientRepository::findAll).stream()
                .map(ClientDto::toDto)
                .toList();
    }
}
