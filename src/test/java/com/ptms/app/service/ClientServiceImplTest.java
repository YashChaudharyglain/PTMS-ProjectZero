package com.ptms.app.service;

import com.ptms.app.dao.ClientDAO;
import com.ptms.app.model.Client;
import com.ptms.app.service.impl.ClientServiceImpl;
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
class ClientServiceImplTest {

    @Mock
    private ClientDAO clientDAO;

    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        clientService =
                new ClientServiceImpl(clientDAO);
    }

    @Test
    void createClientShouldCallDAO()
            throws SQLException {

        Client client = mock(Client.class);

        when(client.getName())
                .thenReturn("ABC Company");

        when(client.getEmail())
                .thenReturn("abc@gmail.com");

        when(client.getPhone())
                .thenReturn("9876543210");

        clientService.createClient(client);

        verify(clientDAO)
                .createClient(client);
    }

    @Test
    void getClientByIdShouldReturnClient()
            throws SQLException {

        Client client = mock(Client.class);

        when(clientDAO.getClientById(1))
                .thenReturn(client);

        Client result =
                clientService.getClientById(1);

        assertSame(client, result);

        verify(clientDAO)
                .getClientById(1);
    }

    @Test
    void getAllClientsShouldReturnClients()
            throws SQLException {

        List<Client> clients =
                List.of(mock(Client.class));

        when(clientDAO.getAllClients())
                .thenReturn(clients);

        List<Client> result =
                clientService.getAllClients();

        assertEquals(1, result.size());

        verify(clientDAO)
                .getAllClients();
    }

    @Test
    void searchClientsShouldCallDAO()
            throws SQLException {

        when(clientDAO.searchClients("abc"))
                .thenReturn(
                        List.of(mock(Client.class))
                );

        List<Client> result =
                clientService.searchClients("abc");

        assertEquals(1, result.size());

        verify(clientDAO)
                .searchClients("abc");
    }

    @Test
    void updateClientShouldCallDAO()
            throws SQLException {

        Client client = mock(Client.class);

        when(client.getId())
                .thenReturn(1);

        when(client.getName())
                .thenReturn("ABC");

        when(client.getEmail())
                .thenReturn("abc@gmail.com");

        when(client.getPhone())
                .thenReturn("9876543210");

        clientService.updateClient(client);

        verify(clientDAO)
                .updateClient(client);
    }

    @Test
    void deleteClientShouldCallDAO()
            throws SQLException {

        clientService.deleteClient(1);

        verify(clientDAO)
                .deleteClient(1);
    }

    @Test
    void createClientShouldRejectNull() {

        assertThrows(
                IllegalArgumentException.class,
                () -> clientService.createClient(null)
        );

        verifyNoInteractions(clientDAO);
    }

    @Test
    void getClientByIdShouldRejectInvalidId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> clientService.getClientById(0)
        );

        verifyNoInteractions(clientDAO);
    }
}