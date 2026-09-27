package com.ptms.app.service.impl;

import com.ptms.app.dao.ClientDAO;
import com.ptms.app.model.Client;
import com.ptms.app.service.ClientService;
import com.ptms.app.util.LoggerUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ClientServiceImpl implements ClientService {

    private static final Logger logger =
            LoggerUtil.getLogger(ClientServiceImpl.class);

    private final ClientDAO clientDAO;

    public ClientServiceImpl(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    @Override
    public void createClient(Client client) throws SQLException {

        validateClient(client);

        clientDAO.createClient(client);

        logger.info(
                "Client created successfully: "
                        + client.getName()
        );
    }

    @Override
    public Client getClientById(int id) throws SQLException {

        validateId(id);

        return clientDAO.getClientById(id);
    }

    @Override
    public List<Client> getAllClients() throws SQLException {

        return clientDAO.getAllClients();
    }

    @Override
    public List<Client> searchClients(String keyword)
            throws SQLException {

        if (keyword == null
                || keyword.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Search keyword cannot be empty"
            );
        }

        return clientDAO.searchClients(keyword.trim());
    }

    @Override
    public void updateClient(Client client)
            throws SQLException {

        if (client == null) {
            throw new IllegalArgumentException(
                    "Client cannot be null"
            );
        }

        validateId(client.getId());

        validateClient(client);

        clientDAO.updateClient(client);

        logger.info(
                "Client updated successfully. ID: "
                        + client.getId()
        );
    }

    @Override
    public void deleteClient(int id)
            throws SQLException {

        validateId(id);

        clientDAO.deleteClient(id);

        logger.info(
                "Client deleted successfully. ID: "
                        + id
        );
    }

    private void validateClient(Client client) {

        if (client == null) {
            throw new IllegalArgumentException(
                    "Client cannot be null"
            );
        }

        if (client.getName() == null
                || client.getName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Client name cannot be empty"
            );
        }

        if (client.getEmail() == null
                || client.getEmail().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Client email cannot be empty"
            );
        }

        if (client.getPhone() == null
                || client.getPhone().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Client phone cannot be empty"
            );
        }
    }

    private void validateId(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Client ID must be greater than zero"
            );
        }
    }
}