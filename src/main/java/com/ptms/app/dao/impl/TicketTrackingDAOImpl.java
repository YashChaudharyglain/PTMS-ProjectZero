package com.ptms.app.dao.impl;

import com.ptms.app.dao.TicketTrackingDAO;
import com.ptms.app.model.TicketTracking;
import com.ptms.app.util.DBConnection;
import com.ptms.app.util.LoggerUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketTrackingDAOImpl
        implements TicketTrackingDAO {

    private static final Logger logger =
            LoggerUtil.getLogger(
                    TicketTrackingDAOImpl.class
            );

    @Override
    public void createTracking(
            TicketTracking tracking)
            throws SQLException {

        String sql = """
                INSERT INTO ticket_tracking
                (ticket_id, status, progress, comment,
                 updated_by, updated_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    tracking.getTicketId()
            );

            statement.setString(
                    2,
                    tracking.getStatus()
            );

            statement.setInt(
                    3,
                    tracking.getProgress()
            );

            statement.setString(
                    4,
                    tracking.getComment()
            );

            statement.setInt(
                    5,
                    tracking.getUpdatedBy()
            );

            if (tracking.getUpdatedAt() == null) {

                statement.setNull(
                        6,
                        java.sql.Types.TIMESTAMP
                );

            } else {

                statement.setTimestamp(
                        6,
                        java.sql.Timestamp.valueOf(
                                tracking.getUpdatedAt()
                        )
                );
            }

            statement.executeUpdate();

            logger.info(
                    "Ticket tracking created successfully for Ticket ID: "
                            + tracking.getTicketId()
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while creating ticket tracking",
                    e
            );

            throw e;
        }
    }

    @Override
    public TicketTracking getTrackingByTicketId(
            int ticketId)
            throws SQLException {

        String sql = """
                SELECT id,
                       ticket_id,
                       status,
                       progress,
                       comment,
                       updated_by,
                       updated_at
                FROM ticket_tracking
                WHERE ticket_id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, ticketId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    logger.info(
                            "Ticket tracking found for Ticket ID: "
                                    + ticketId
                    );

                    return mapTracking(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while getting ticket tracking for Ticket ID: "
                            + ticketId,
                    e
            );

            throw e;
        }

        logger.warning(
                "Ticket tracking not found for Ticket ID: "
                        + ticketId
        );

        return null;
    }

    @Override
    public void updateTracking(
            TicketTracking tracking)
            throws SQLException {

        String sql = """
                UPDATE ticket_tracking
                SET status = ?,
                    progress = ?,
                    comment = ?,
                    updated_by = ?,
                    updated_at = ?
                WHERE ticket_id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    tracking.getStatus()
            );

            statement.setInt(
                    2,
                    tracking.getProgress()
            );

            statement.setString(
                    3,
                    tracking.getComment()
            );

            statement.setInt(
                    4,
                    tracking.getUpdatedBy()
            );

            if (tracking.getUpdatedAt() == null) {

                statement.setNull(
                        5,
                        java.sql.Types.TIMESTAMP
                );

            } else {

                statement.setTimestamp(
                        5,
                        java.sql.Timestamp.valueOf(
                                tracking.getUpdatedAt()
                        )
                );
            }

            statement.setInt(
                    6,
                    tracking.getTicketId()
            );

            statement.executeUpdate();

            logger.info(
                    "Ticket tracking updated successfully for Ticket ID: "
                            + tracking.getTicketId()
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while updating ticket tracking",
                    e
            );

            throw e;
        }
    }

    @Override
    public void deleteTracking(
            int ticketId)
            throws SQLException {

        String sql = """
                DELETE FROM ticket_tracking
                WHERE ticket_id = ?
                """;

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, ticketId);

            statement.executeUpdate();

            logger.info(
                    "Ticket tracking deleted successfully for Ticket ID: "
                            + ticketId
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while deleting ticket tracking",
                    e
            );

            throw e;
        }
    }

    private TicketTracking mapTracking(
            ResultSet resultSet)
            throws SQLException {

        return new TicketTracking(
                resultSet.getInt("id"),
                resultSet.getInt("ticket_id"),
                resultSet.getString("status"),
                resultSet.getInt("progress"),
                resultSet.getString("comment"),
                resultSet.getInt("updated_by"),
                resultSet.getTimestamp("updated_at")
                        != null
                        ? resultSet.getTimestamp("updated_at")
                        .toLocalDateTime()
                        : null
        );
    }
}