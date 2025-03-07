package ru.otus.dto;

import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.List;

public record ClientDto(Long id, String name, Address address, List<Phone> phones) {
    public static ClientDto toDto(Client client) {
        var phones = client.phones().stream().toList();
        return new ClientDto(client.id(), client.name(), client.address(), phones);
    }
}
