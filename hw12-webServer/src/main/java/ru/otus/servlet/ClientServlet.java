package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.services.ServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class ClientServlet extends HttpServlet {

    private static final String PARAM_CLIENT_NAME = "name";
    private static final String PARAM_CLIENT_ADDRESS = "address";
    private static final String PARAM_CLIENT_PHONE = "phone";

    private static final String CLIENT_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";

    private final transient TemplateProcessor templateProcessor;
    private final transient ServiceClient serviceClient;

    public ClientServlet(TemplateProcessor templateProcessor, ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<Client> clients = serviceClient.findAll();
        paramsMap.put(TEMPLATE_ATTR_CLIENTS, clients);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENT_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String clientName = request.getParameter(PARAM_CLIENT_NAME);
        String clientAddress = request.getParameter(PARAM_CLIENT_ADDRESS);
        String[] clientPhones = request.getParameterValues(PARAM_CLIENT_PHONE);

        Address address = new Address(null, clientAddress);
        List<Phone> phones = new ArrayList<>();

        for (String phone : clientPhones) {
            phones.add(new Phone(null, phone));
        }

        Client client = new Client(null, clientName, address, phones);
        Client saveClient = serviceClient.saveClient(client);
        if (saveClient != null) {
            response.sendRedirect("/admin/clients");
        } else {
            response.setStatus(SC_UNAUTHORIZED);
        }
    }
}
