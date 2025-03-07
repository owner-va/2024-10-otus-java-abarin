package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.dto.ClientDto;
import ru.otus.services.ServiceClient;

@Controller
public class ClientController {

    private final ServiceClient serviceClient;

    public ClientController(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    @GetMapping("/")
    public String getClients(Model model) {
        var clients = serviceClient.findAll();
        model.addAttribute("clients", clients);
        return "clients";
    }

    @PostMapping("/")
    public RedirectView newClient(ClientDto clientDTO) {
        serviceClient.saveClient(clientDTO);
        return new RedirectView("/", true);
    }
}
