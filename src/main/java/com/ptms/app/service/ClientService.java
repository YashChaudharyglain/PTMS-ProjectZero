package com.ptms.app.service;

import com.ptms.app.model.Client;

import java.sql.SQLException;
import java.util.List;

public interface ClientService {

    void createClient(Client client) throws SQLException;

    Client getClientById(int id) throws SQLException;

    List<Client> getAllClients() throws SQLException;

    List<Client> searchClients(String keyword) throws SQLException;

    void updateClient(Client client) throws SQLException;

    void deleteClient(int id) throws SQLException;
}