package com.ptms.app.controller;

import com.ptms.app.model.TicketTracking;
import com.ptms.app.service.TicketTrackingService;

import java.sql.SQLException;

public class TicketTrackingController {

    private final TicketTrackingService ticketTrackingService;

    public TicketTrackingController(
            TicketTrackingService ticketTrackingService) {

        this.ticketTrackingService =
                ticketTrackingService;
    }

    public void createTracking(
            TicketTracking tracking)
            throws SQLException {

        ticketTrackingService.createTracking(tracking);
    }

    public TicketTracking getTrackingByTicketId(
            int ticketId)
            throws SQLException {

        return ticketTrackingService.getTrackingByTicketId(
                ticketId
        );
    }

    public void updateTracking(
            TicketTracking tracking)
            throws SQLException {

        ticketTrackingService.updateTracking(tracking);
    }

    public void deleteTracking(int ticketId)
            throws SQLException {

        ticketTrackingService.deleteTracking(ticketId);
    }
}