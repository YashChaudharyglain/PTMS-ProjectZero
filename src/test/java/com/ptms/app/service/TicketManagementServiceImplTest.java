package com.ptms.app.service;

import com.ptms.app.dao.TicketManagementDAO;
import com.ptms.app.model.TicketManagement;
import com.ptms.app.service.impl.TicketManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketManagementServiceImplTest {

    @Mock
    private TicketManagementDAO ticketManagementDAO;

    private TicketManagementServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        ticketService =
                new TicketManagementServiceImpl(
                        ticketManagementDAO
                );
    }

    @Test
    void createTicketShouldCallDAO()
            throws SQLException {

        TicketManagement ticket =
                mock(TicketManagement.class);

        when(ticket.getProjectId())
                .thenReturn(1);

        when(ticket.getTitle())
                .thenReturn("Login issue");

        when(ticket.getDescription())
                .thenReturn("Login is not working");

        when(ticket.getAssignedTo())
                .thenReturn(2);

        ticketService.createTicket(ticket);

        verify(ticketManagementDAO)
                .createTicket(ticket);
    }

    @Test
    void getTicketByIdShouldReturnTicket()
            throws SQLException {

        TicketManagement ticket =
                mock(TicketManagement.class);

        when(ticketManagementDAO.getTicketById(1))
                .thenReturn(ticket);

        TicketManagement result =
                ticketService.getTicketById(1);

        assertSame(ticket, result);

        verify(ticketManagementDAO)
                .getTicketById(1);
    }

    @Test
    void getAllTicketsShouldReturnTickets()
            throws SQLException {

        when(ticketManagementDAO.getAllTickets())
                .thenReturn(
                        List.of(
                                mock(TicketManagement.class)
                        )
                );

        List<TicketManagement> result =
                ticketService.getAllTickets();

        assertEquals(1, result.size());

        verify(ticketManagementDAO)
                .getAllTickets();
    }

    @Test
    void getTicketsByProjectShouldCallDAO()
            throws SQLException {

        ticketService.getTicketsByProject(1);

        verify(ticketManagementDAO)
                .getTicketsByProject(1);
    }

    @Test
    void getTicketsByUserShouldCallDAO()
            throws SQLException {

        ticketService.getTicketsByUser(2);

        verify(ticketManagementDAO)
                .getTicketsByUser(2);
    }

    @Test
    void searchTicketsShouldCallDAO()
            throws SQLException {

        ticketService.searchTickets("login");

        verify(ticketManagementDAO)
                .searchTickets("login");
    }

    @Test
    void updateTicketShouldCallDAO()
            throws SQLException {

        TicketManagement ticket =
                mock(TicketManagement.class);

        when(ticket.getId()).thenReturn(1);
        when(ticket.getProjectId()).thenReturn(1);
        when(ticket.getTitle())
                .thenReturn("Login issue");
        when(ticket.getDescription())
                .thenReturn("Login problem");
        when(ticket.getAssignedTo())
                .thenReturn(2);

        ticketService.updateTicket(ticket);

        verify(ticketManagementDAO)
                .updateTicket(ticket);
    }

    @Test
    void deleteTicketShouldCallDAO()
            throws SQLException {

        ticketService.deleteTicket(1);

        verify(ticketManagementDAO)
                .deleteTicket(1);
    }

    @Test
    void getTicketByIdShouldRejectInvalidId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> ticketService.getTicketById(0)
        );
    }
}