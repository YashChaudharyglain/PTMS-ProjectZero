package com.ptms.app.dao;

import com.ptms.app.model.TicketTracking;

import java.sql.SQLException;

public interface TicketTrackingDAO {

    void createTracking(TicketTracking tracking)
            throws SQLException;

    TicketTracking getTrackingByTicketId(int ticketId)
            throws SQLException;

    void updateTracking(TicketTracking tracking)
            throws SQLException;

    void deleteTracking(int ticketId)
            throws SQLException;
}