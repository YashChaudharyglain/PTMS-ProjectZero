package com.ptms.app.service.impl;

import com.ptms.app.dao.TicketManagementDAO;
import com.ptms.app.model.TicketManagement;
import com.ptms.app.service.TicketManagementService;
import com.ptms.app.util.LoggerUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class TicketManagementServiceImpl
        implements TicketManagementService {

    private static final Logger logger =
            LoggerUtil.getLogger(TicketManagementServiceImpl.class);

    private final TicketManagementDAO ticketManagementDAO;

    public TicketManagementServiceImpl(
            TicketManagementDAO ticketManagementDAO) {

        this.ticketManagementDAO = ticketManagementDAO;
    }

    @Override
    public void createTicket(TicketManagement ticket)
            throws SQLException {

        validateTicket(ticket);

        ticketManagementDAO.createTicket(ticket);

        logger.info(
                "Ticket created successfully: "
                        + ticket.getTitle()
        );
    }

    @Override
    public TicketManagement getTicketById(int id)
            throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Ticket ID must be greater than zero"
            );
        }

        return ticketManagementDAO.getTicketById(id);
    }

    @Override
    public List<TicketManagement> getAllTickets()
            throws SQLException {

        return ticketManagementDAO.getAllTickets();
    }

    @Override
    public List<TicketManagement> getTicketsByProject(
            int projectId)
            throws SQLException {

        if (projectId <= 0) {
            throw new IllegalArgumentException(
                    "Project ID must be greater than zero"
            );
        }

        return ticketManagementDAO.getTicketsByProject(
                projectId
        );
    }

    @Override
    public List<TicketManagement> getTicketsByUser(
            int userId)
            throws SQLException {

        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be greater than zero"
            );
        }

        return ticketManagementDAO.getTicketsByUser(
                userId
        );
    }

    @Override
    public List<TicketManagement> searchTickets(
            String keyword)
            throws SQLException {

        if (keyword == null
                || keyword.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Search keyword cannot be empty"
            );
        }

        return ticketManagementDAO.searchTickets(
                keyword.trim()
        );
    }

    @Override
    public void updateTicket(
            TicketManagement ticket)
            throws SQLException {

        if (ticket == null) {
            throw new IllegalArgumentException(
                    "Ticket cannot be null"
            );
        }

        if (ticket.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Ticket ID must be greater than zero"
            );
        }

        validateTicket(ticket);

        ticketManagementDAO.updateTicket(ticket);

        logger.info(
                "Ticket updated successfully. ID: "
                        + ticket.getId()
        );
    }

    @Override
    public void deleteTicket(int id)
            throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Ticket ID must be greater than zero"
            );
        }

        ticketManagementDAO.deleteTicket(id);

        logger.info(
                "Ticket deleted successfully. ID: "
                        + id
        );
    }

    private void validateTicket(
            TicketManagement ticket) {

        if (ticket == null) {
            throw new IllegalArgumentException(
                    "Ticket cannot be null"
            );
        }

        if (ticket.getProjectId() <= 0) {
            throw new IllegalArgumentException(
                    "Project ID must be greater than zero"
            );
        }

        if (ticket.getTitle() == null
                || ticket.getTitle().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Ticket title cannot be empty"
            );
        }

        if (ticket.getDescription() == null
                || ticket.getDescription().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Ticket description cannot be empty"
            );
        }

        if (ticket.getAssignedTo() <= 0) {
            throw new IllegalArgumentException(
                    "Assigned user ID must be greater than zero"
            );
        }
    }
}