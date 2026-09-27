package com.ptms.app.service;

import com.ptms.app.dao.UserDAO;
import com.ptms.app.model.User;
import com.ptms.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDAO);
    }

    @Test
    void createUserShouldCallDAO()
            throws SQLException {

        User user = mock(User.class);

        when(user.getUsername())
                .thenReturn("yash");

        when(user.getPassword())
                .thenReturn("password123");

        when(user.getFirstName())
                .thenReturn("Yash");

        when(user.getLastName())
                .thenReturn("Chaudhary");

        userService.createUser(user);

        verify(userDAO)
                .createUser(user);

        ArgumentCaptor<String> passwordCaptor =
                ArgumentCaptor.forClass(String.class);

        verify(user)
                .setPassword(passwordCaptor.capture());

        String hashedPassword =
                passwordCaptor.getValue();

        assertNotEquals(
                "password123",
                hashedPassword
        );

        assertTrue(
                BCrypt.checkpw(
                        "password123",
                        hashedPassword
                )
        );
    }

    @Test
    void getUserByIdShouldReturnUser()
            throws SQLException {

        User user = mock(User.class);

        when(userDAO.getUserById(1))
                .thenReturn(user);

        User result =
                userService.getUserById(1);

        assertSame(user, result);

        verify(userDAO)
                .getUserById(1);
    }

    @Test
    void getUserByUsernameShouldReturnUser()
            throws SQLException {

        User user = mock(User.class);

        when(userDAO.getUserByUsername("yash"))
                .thenReturn(user);

        User result =
                userService.getUserByUsername("yash");

        assertSame(user, result);

        verify(userDAO)
                .getUserByUsername("yash");
    }

    @Test
    void getAllUsersShouldReturnUsers()
            throws SQLException {

        List<User> users = List.of(
                mock(User.class),
                mock(User.class)
        );

        when(userDAO.getAllUsers())
                .thenReturn(users);

        List<User> result =
                userService.getAllUsers();

        assertEquals(2, result.size());

        verify(userDAO)
                .getAllUsers();
    }

    @Test
    void searchUsersShouldReturnUsers()
            throws SQLException {

        List<User> users =
                List.of(mock(User.class));

        when(userDAO.searchUsers("yash"))
                .thenReturn(users);

        List<User> result =
                userService.searchUsers("yash");

        assertEquals(1, result.size());

        verify(userDAO)
                .searchUsers("yash");
    }

    @Test
    void updateUserShouldCallDAO()
            throws SQLException {

        User user = mock(User.class);

        when(user.getId())
                .thenReturn(1);

        when(user.getUsername())
                .thenReturn("yash");

        when(user.getPassword())
                .thenReturn("password123");

        when(user.getFirstName())
                .thenReturn("Yash");

        when(user.getLastName())
                .thenReturn("Chaudhary");

        userService.updateUser(user);

        verify(userDAO)
                .updateUser(user);

        verify(user)
                .setPassword(anyString());
    }

    @Test
    void deleteUserShouldCallDAO()
            throws SQLException {

        userService.deleteUser(1);

        verify(userDAO)
                .deleteUser(1);
    }

    @Test
    void getUserByIdShouldRejectInvalidId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUserById(0)
        );

        verifyNoInteractions(userDAO);
    }

    @Test
    void searchUsersShouldRejectEmptyKeyword() {

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.searchUsers(" ")
        );

        verifyNoInteractions(userDAO);
    }

    @Test
    void loginShouldReturnUserForValidPassword()
            throws SQLException {

        User user = mock(User.class);

        String password = "password123";

        String hashedPassword =
                BCrypt.hashpw(
                        password,
                        BCrypt.gensalt()
                );

        when(user.getPassword())
                .thenReturn(hashedPassword);

        when(userDAO.getUserByUsername("yash"))
                .thenReturn(user);

        User result =
                userService.login(
                        "yash",
                        password
                );

        assertSame(user, result);

        verify(userDAO)
                .getUserByUsername("yash");
    }

    @Test
    void loginShouldReturnNullForInvalidPassword()
            throws SQLException {

        User user = mock(User.class);

        String hashedPassword =
                BCrypt.hashpw(
                        "correctPassword",
                        BCrypt.gensalt()
                );

        when(user.getPassword())
                .thenReturn(hashedPassword);

        when(userDAO.getUserByUsername("yash"))
                .thenReturn(user);

        User result =
                userService.login(
                        "yash",
                        "wrongPassword"
                );

        assertNull(result);
    }
}