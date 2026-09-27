package com.ptms.app.service.impl;

import com.ptms.app.dao.TicketTrackingDAO;
import com.ptms.app.model.TicketTracking;
import com.ptms.app.service.TicketTrackingService;
import com.ptms.app.util.LoggerUtil;

import java.sql.SQLException;
import java.util.logging.Logger;

public class TicketTrackingServiceImpl
        implements TicketTrackingService {

    private static final Logger logger =
            LoggerUtil.getLogger(TicketTrackingServiceImpl.class);

    private final TicketTrackingDAO ticketTrackingDAO;

    public TicketTrackingServiceImpl(
            TicketTrackingDAO ticketTrackingDAO) {

        this.ticketTrackingDAO = ticketTrackingDAO;
    }

    @Override
    public void createTracking(
            TicketTracking tracking)
            throws SQLException {

        validateTracking(tracking);

        ticketTrackingDAO.createTracking(tracking);

        logger.info(
                "Ticket tracking created successfully. Ticket ID: "
                        + tracking.getTicketId()
        );
    }

    @Override
    public TicketTracking getTrackingByTicketId(
            int ticketId)
            throws SQLException {

        if (ticketId <= 0) {
            throw new IllegalArgumentException(
                    "Ticket ID must be greater than zero"
            );
        }

        return ticketTrackingDAO.getTrackingByTicketId(
                ticketId
        );
    }

    @Override
    public void updateTracking(
            TicketTracking tracking)
            throws SQLException {

        if (tracking == null) {
            throw new IllegalArgumentException(
                    "Ticket tracking cannot be null"
            );
        }

        if (tracking.getTicketId() <= 0) {
            throw new IllegalArgumentException(
                    "Ticket ID must be greater than zero"
            );
        }

        validateTracking(tracking);

        ticketTrackingDAO.updateTracking(tracking);

        logger.info(
                "Ticket tracking updated successfully. Ticket ID: "
                        + tracking.getTicketId()
        );
    }

    @Override
    public void deleteTracking(int ticketId)
            throws SQLException {

        if (ticketId <= 0) {
            throw new IllegalArgumentException(
                    "Ticket ID must be greater than zero"
            );
        }

        ticketTrackingDAO.deleteTracking(ticketId);

        logger.info(
                "Ticket tracking deleted successfully. Ticket ID: "
                        + ticketId
        );
    }

    private void validateTracking(
            TicketTracking tracking) {

        if (tracking == null) {
            throw new IllegalArgumentException(
                    "Ticket tracking cannot be null"
            );
        }

        if (tracking.getTicketId() <= 0) {
            throw new IllegalArgumentException(
                    "Ticket ID must be greater than zero"
            );
        }

        if (tracking.getUpdatedBy() <= 0) {
            throw new IllegalArgumentException(
                    "Updated user ID must be greater than zero"
            );
        }

        if (tracking.getStatus() == null
                || tracking.getStatus().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Tracking status cannot be empty"
            );
        }

        if (tracking.getProgress() < 0
                || tracking.getProgress() > 100) {

            throw new IllegalArgumentException(
                    "Progress must be between 0 and 100"
            );
        }
    }
}