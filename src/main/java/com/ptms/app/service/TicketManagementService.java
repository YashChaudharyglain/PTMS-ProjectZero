package com.ptms.app.service;

import com.ptms.app.model.TicketManagement;

import java.sql.SQLException;
import java.util.List;

public interface TicketManagementService {

    void createTicket(TicketManagement ticket) throws SQLException;

    TicketManagement getTicketById(int id) throws SQLException;

    List<TicketManagement> getAllTickets() throws SQLException;

    List<TicketManagement> getTicketsByProject(int projectId) throws SQLException;

    List<TicketManagement> getTicketsByUser(int userId) throws SQLException;

    List<TicketManagement> searchTickets(String keyword) throws SQLException;

    void updateTicket(TicketManagement ticket) throws SQLException;

    void deleteTicket(int id) throws SQLException;
}