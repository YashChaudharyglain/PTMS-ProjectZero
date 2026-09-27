package com.ptms.app.service;

import com.ptms.app.dao.TicketTrackingDAO;
import com.ptms.app.model.TicketTracking;
import com.ptms.app.service.impl.TicketTrackingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketTrackingServiceImplTest {

    @Mock
    private TicketTrackingDAO ticketTrackingDAO;

    private TicketTrackingServiceImpl ticketTrackingService;

    @BeforeEach
    void setUp() {
        ticketTrackingService =
                new TicketTrackingServiceImpl(
                        ticketTrackingDAO
                );
    }

    @Test
    void createTrackingShouldCallDAO()
            throws SQLException {

        TicketTracking tracking =
                mock(TicketTracking.class);

        when(tracking.getTicketId())
                .thenReturn(1);

        when(tracking.getUpdatedBy())
                .thenReturn(2);

        when(tracking.getStatus())
                .thenReturn("IN_PROGRESS");

        when(tracking.getProgress())
                .thenReturn(50);

        ticketTrackingService.createTracking(tracking);

        verify(ticketTrackingDAO)
                .createTracking(tracking);
    }

    @Test
    void getTrackingByTicketIdShouldReturnTracking()
            throws SQLException {

        TicketTracking tracking =
                mock(TicketTracking.class);

        when(ticketTrackingDAO.getTrackingByTicketId(1))
                .thenReturn(tracking);

        TicketTracking result =
                ticketTrackingService
                        .getTrackingByTicketId(1);

        assertSame(tracking, result);

        verify(ticketTrackingDAO)
                .getTrackingByTicketId(1);
    }

    @Test
    void updateTrackingShouldCallDAO()
            throws SQLException {

        TicketTracking tracking =
                mock(TicketTracking.class);

        when(tracking.getTicketId())
                .thenReturn(1);

        when(tracking.getUpdatedBy())
                .thenReturn(2);

        when(tracking.getStatus())
                .thenReturn("IN_PROGRESS");

        when(tracking.getProgress())
                .thenReturn(75);

        ticketTrackingService.updateTracking(tracking);

        verify(ticketTrackingDAO)
                .updateTracking(tracking);
    }

    @Test
    void deleteTrackingShouldCallDAO()
            throws SQLException {

        ticketTrackingService.deleteTracking(1);

        verify(ticketTrackingDAO)
                .deleteTracking(1);
    }

    @Test
    void createTrackingShouldRejectInvalidProgress() {

        TicketTracking tracking =
                mock(TicketTracking.class);

        when(tracking.getTicketId())
                .thenReturn(1);

        when(tracking.getUpdatedBy())
                .thenReturn(2);

        when(tracking.getStatus())
                .thenReturn("IN_PROGRESS");

        when(tracking.getProgress())
                .thenReturn(101);

        assertThrows(
                IllegalArgumentException.class,
                () -> ticketTrackingService
                        .createTracking(tracking)
        );

        verifyNoInteractions(ticketTrackingDAO);
    }
}