package com.ptms.app.controller;

import com.ptms.app.model.TicketManagement;
import com.ptms.app.service.TicketManagementService;

import java.sql.SQLException;
import java.util.List;

public class TicketManagementController {

    private final TicketManagementService ticketManagementService;

    public TicketManagementController(
            TicketManagementService ticketManagementService) {

        this.ticketManagementService =
                ticketManagementService;
    }

    public void createTicket(TicketManagement ticket)
            throws SQLException {

        ticketManagementService.createTicket(ticket);
    }

    public TicketManagement getTicketById(int id)
            throws SQLException {

        return ticketManagementService.getTicketById(id);
    }

    public List<TicketManagement> getAllTickets()
            throws SQLException {

        return ticketManagementService.getAllTickets();
    }

    public List<TicketManagement> getTicketsByProject(
            int projectId)
            throws SQLException {

        return ticketManagementService.getTicketsByProject(
                projectId
        );
    }

    public List<TicketManagement> getTicketsByUser(
            int userId)
            throws SQLException {

        return ticketManagementService.getTicketsByUser(
                userId
        );
    }

    public List<TicketManagement> searchTickets(
            String keyword)
            throws SQLException {

        return ticketManagementService.searchTickets(
                keyword
        );
    }

    public void updateTicket(
            TicketManagement ticket)
            throws SQLException {

        ticketManagementService.updateTicket(ticket);
    }

    public void deleteTicket(int id)
            throws SQLException {

        ticketManagementService.deleteTicket(id);
    }
}