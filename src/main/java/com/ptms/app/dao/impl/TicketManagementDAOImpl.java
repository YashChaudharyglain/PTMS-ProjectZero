package com.ptms.app.dao.impl;

import com.ptms.app.dao.TicketManagementDAO;
import com.ptms.app.model.TicketManagement;
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

public class TicketManagementDAOImpl
        implements TicketManagementDAO {

    private static final Logger logger =
            LoggerUtil.getLogger(
                    TicketManagementDAOImpl.class
            );

    @Override
    public void createTicket(
            TicketManagement ticket)
            throws SQLException {

        String sql = """
                INSERT INTO ticket_management
                (project_id, title, description, priority,
                 deadline, assigned_to, created_at, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    ticket.getProjectId()
            );

            statement.setString(
                    2,
                    ticket.getTitle()
            );

            statement.setString(
                    3,
                    ticket.getDescription()
            );

            statement.setString(
                    4,
                    ticket.getPriority()
            );

            if (ticket.getDeadline() == null) {

                statement.setNull(
                        5,
                        java.sql.Types.DATE
                );

            } else {

                statement.setDate(
                        5,
                        java.sql.Date.valueOf(
                                ticket.getDeadline()
                        )
                );
            }

            statement.setInt(
                    6,
                    ticket.getAssignedTo()
            );

            if (ticket.getCreatedAt() == null) {

                statement.setNull(
                        7,
                        java.sql.Types.TIMESTAMP
                );

            } else {

                statement.setTimestamp(
                        7,
                        java.sql.Timestamp.valueOf(
                                ticket.getCreatedAt()
                        )
                );
            }

            statement.setString(
                    8,
                    ticket.getStatus()
            );

            statement.executeUpdate();

            logger.info(
                    "Ticket created successfully: "
                            + ticket.getTitle()
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while creating ticket",
                    e
            );

            throw e;
        }
    }

    @Override
    public TicketManagement getTicketById(
            int id)
            throws SQLException {

        String sql = """
                SELECT id,
                       project_id,
                       title,
                       description,
                       priority,
                       deadline,
                       assigned_to,
                       created_at,
                       status
                FROM ticket_management
                WHERE id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    logger.info(
                            "Ticket found with ID: "
                                    + id
                    );

                    return mapTicket(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while getting ticket with ID: "
                            + id,
                    e
            );

            throw e;
        }

        logger.warning(
                "Ticket not found with ID: " + id
        );

        return null;
    }

    @Override
    public List<TicketManagement> getAllTickets()
            throws SQLException {

        String sql = """
                SELECT id,
                       project_id,
                       title,
                       description,
                       priority,
                       deadline,
                       assigned_to,
                       created_at,
                       status
                FROM ticket_management
                ORDER BY id
                """;

        List<TicketManagement> tickets =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                tickets.add(
                        mapTicket(resultSet)
                );
            }

            logger.info(
                    "All tickets fetched successfully"
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while getting all tickets",
                    e
            );

            throw e;
        }

        return tickets;
    }

    @Override
    public List<TicketManagement> getTicketsByProject(
            int projectId)
            throws SQLException {

        String sql = """
                SELECT id,
                       project_id,
                       title,
                       description,
                       priority,
                       deadline,
                       assigned_to,
                       created_at,
                       status
                FROM ticket_management
                WHERE project_id = ?
                ORDER BY id
                """;

        List<TicketManagement> tickets =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, projectId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    tickets.add(
                            mapTicket(resultSet)
                    );
                }
            }

            logger.info(
                    "Tickets fetched for project ID: "
                            + projectId
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while getting project tickets",
                    e
            );

            throw e;
        }

        return tickets;
    }

    @Override
    public List<TicketManagement> getTicketsByUser(
            int userId)
            throws SQLException {

        String sql = """
                SELECT id,
                       project_id,
                       title,
                       description,
                       priority,
                       deadline,
                       assigned_to,
                       created_at,
                       status
                FROM ticket_management
                WHERE assigned_to = ?
                ORDER BY id
                """;

        List<TicketManagement> tickets =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    tickets.add(
                            mapTicket(resultSet)
                    );
                }
            }

            logger.info(
                    "Tickets fetched for user ID: "
                            + userId
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while getting user tickets",
                    e
            );

            throw e;
        }

        return tickets;
    }

    @Override
    public List<TicketManagement> searchTickets(
            String keyword)
            throws SQLException {

        String sql = """
                SELECT id,
                       project_id,
                       title,
                       description,
                       priority,
                       deadline,
                       assigned_to,
                       created_at,
                       status
                FROM ticket_management
                WHERE title LIKE ?
                   OR description LIKE ?
                   OR priority LIKE ?
                   OR status LIKE ?
                ORDER BY id
                """;

        List<TicketManagement> tickets =
                new ArrayList<>();

        String searchKeyword =
                "%" + keyword + "%";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    searchKeyword
            );

            statement.setString(
                    2,
                    searchKeyword
            );

            statement.setString(
                    3,
                    searchKeyword
            );

            statement.setString(
                    4,
                    searchKeyword
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    tickets.add(
                            mapTicket(resultSet)
                    );
                }
            }

            logger.info(
                    "Ticket search completed for: "
                            + keyword
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while searching tickets",
                    e
            );

            throw e;
        }

        return tickets;
    }

    @Override
    public void updateTicket(
            TicketManagement ticket)
            throws SQLException {

        String sql = """
                UPDATE ticket_management
                SET project_id = ?,
                    title = ?,
                    description = ?,
                    priority = ?,
                    deadline = ?,
                    assigned_to = ?,
                    created_at = ?,
                    status = ?
                WHERE id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    ticket.getProjectId()
            );

            statement.setString(
                    2,
                    ticket.getTitle()
            );

            statement.setString(
                    3,
                    ticket.getDescription()
            );

            statement.setString(
                    4,
                    ticket.getPriority()
            );

            if (ticket.getDeadline() == null) {

                statement.setNull(
                        5,
                        java.sql.Types.DATE
                );

            } else {

                statement.setDate(
                        5,
                        java.sql.Date.valueOf(
                                ticket.getDeadline()
                        )
                );
            }

            statement.setInt(
                    6,
                    ticket.getAssignedTo()
            );

            if (ticket.getCreatedAt() == null) {

                statement.setNull(
                        7,
                        java.sql.Types.TIMESTAMP
                );

            } else {

                statement.setTimestamp(
                        7,
                        java.sql.Timestamp.valueOf(
                                ticket.getCreatedAt()
                        )
                );
            }

            statement.setString(
                    8,
                    ticket.getStatus()
            );

            statement.setInt(
                    9,
                    ticket.getId()
            );

            statement.executeUpdate();

            logger.info(
                    "Ticket updated successfully with ID: "
                            + ticket.getId()
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while updating ticket: "
                            + ticket.getId(),
                    e
            );

            throw e;
        }
    }

    @Override
    public void deleteTicket(
            int id)
            throws SQLException {

        String sql = """
                DELETE FROM ticket_management
                WHERE id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

            logger.info(
                    "Ticket deleted successfully with ID: "
                            + id
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while deleting ticket: "
                            + id,
                    e
            );

            throw e;
        }
    }

    private TicketManagement mapTicket(
            ResultSet resultSet)
            throws SQLException {

        return new TicketManagement(
                resultSet.getInt("id"),
                resultSet.getInt("project_id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getString("priority"),

                resultSet.getDate("deadline")
                        != null
                        ? resultSet.getDate("deadline")
                        .toLocalDate()
                        : null,

                resultSet.getInt("assigned_to"),

                resultSet.getTimestamp("created_at")
                        != null
                        ? resultSet.getTimestamp("created_at")
                        .toLocalDateTime()
                        : null,

                resultSet.getString("status")
        );
    }
}