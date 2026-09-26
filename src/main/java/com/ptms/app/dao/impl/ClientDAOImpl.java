package com.ptms.app.dao.impl;

import com.ptms.app.dao.ClientDAO;
import com.ptms.app.model.Client;
import com.ptms.app.util.DBConnection;
import com.ptms.app.util.LoggerUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientDAOImpl implements ClientDAO {

    private static final Logger logger =
            LoggerUtil.getLogger(ClientDAOImpl.class);

    @Override
    public void createClient(Client client) throws SQLException {

        String sql = """
                INSERT INTO clients
                (name, email, phone, company_name)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, client.getName());
            statement.setString(2, client.getEmail());
            statement.setString(3, client.getPhone());
            statement.setString(4, client.getCompanyName());

            statement.executeUpdate();

            logger.info(
                    "Client created successfully: "
                            + client.getName()
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while creating client",
                    e
            );

            throw e;
        }
    }

    @Override
    public Client getClientById(int id) throws SQLException {

        String sql =
                "SELECT * FROM clients WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    logger.info(
                            "Client found with ID: " + id
                    );

                    return mapClient(resultSet);
                }
            }

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while getting client with ID: " + id,
                    e
            );

            throw e;
        }

        logger.warning(
                "Client not found with ID: " + id
        );

        return null;
    }

    @Override
    public List<Client> getAllClients()
            throws SQLException {

        String sql =
                "SELECT * FROM clients";

        List<Client> clients = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                clients.add(
                        mapClient(resultSet)
                );
            }

            logger.info(
                    "All clients fetched successfully"
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while getting all clients",
                    e
            );

            throw e;
        }

        return clients;
    }

    @Override
    public List<Client> searchClients(
            String keyword)
            throws SQLException {

        String sql = """
                SELECT * FROM clients
                WHERE name LIKE ?
                   OR email LIKE ?
                   OR phone LIKE ?
                   OR company_name LIKE ?
                """;

        List<Client> clients = new ArrayList<>();

        String searchKeyword =
                "%" + keyword + "%";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, searchKeyword);
            statement.setString(2, searchKeyword);
            statement.setString(3, searchKeyword);
            statement.setString(4, searchKeyword);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    clients.add(
                            mapClient(resultSet)
                    );
                }
            }

            logger.info(
                    "Client search completed for: "
                            + keyword
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while searching clients",
                    e
            );

            throw e;
        }

        return clients;
    }

    @Override
    public void updateClient(Client client)
            throws SQLException {

        String sql = """
                UPDATE clients
                SET name = ?,
                    email = ?,
                    phone = ?,
                    company_name = ?
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, client.getName());
            statement.setString(2, client.getEmail());
            statement.setString(3, client.getPhone());
            statement.setString(4, client.getCompanyName());
            statement.setInt(5, client.getId());

            statement.executeUpdate();

            logger.info(
                    "Client updated successfully with ID: "
                            + client.getId()
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while updating client: "
                            + client.getId(),
                    e
            );

            throw e;
        }
    }

    @Override
    public void deleteClient(int id)
            throws SQLException {

        String sql =
                "DELETE FROM clients WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

            logger.info(
                    "Client deleted successfully with ID: "
                            + id
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while deleting client: " + id,
                    e
            );

            throw e;
        }
    }

    private Client mapClient(
            ResultSet resultSet)
            throws SQLException {

        return new Client(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("phone"),
                resultSet.getString("company_name")
        );
    }
}