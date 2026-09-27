package com.ptms.app.controller;

import com.ptms.app.model.Client;
import com.ptms.app.service.ClientService;

import java.sql.SQLException;
import java.util.List;

public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    public void createClient(Client client)
            throws SQLException {

        clientService.createClient(client);
    }

    public Client getClientById(int id)
            throws SQLException {

        return clientService.getClientById(id);
    }

    public List<Client> getAllClients()
            throws SQLException {

        return clientService.getAllClients();
    }

    public List<Client> searchClients(String keyword)
            throws SQLException {

        return clientService.searchClients(keyword);
    }

    public void updateClient(Client client)
            throws SQLException {

        clientService.updateClient(client);
    }

    public void deleteClient(int id)
            throws SQLException {

        clientService.deleteClient(id);
    }
}