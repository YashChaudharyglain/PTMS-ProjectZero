package com.ptms.app.service.impl;

import com.ptms.app.dao.UserDAO;
import com.ptms.app.model.User;
import com.ptms.app.service.UserService;
import com.ptms.app.util.LoggerUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    private static final Logger logger =
            LoggerUtil.getLogger(UserServiceImpl.class);

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void createUser(User user) throws SQLException {

        validateUser(user);

        String hashedPassword =
                BCrypt.hashpw(
                        user.getPassword(),
                        BCrypt.gensalt()
                );

        user.setPassword(hashedPassword);

        userDAO.createUser(user);

        logger.info(
                "User created successfully: "
                        + user.getUsername()
        );
    }

    @Override
    public User getUserById(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be greater than zero"
            );
        }

        return userDAO.getUserById(id);
    }

    @Override
    public User getUserByUsername(
            String username)
            throws SQLException {

        validateUsername(username);

        return userDAO.getUserByUsername(username);
    }

    @Override
    public List<User> getAllUsers()
            throws SQLException {

        return userDAO.getAllUsers();
    }

    @Override
    public List<User> searchUsers(
            String keyword)
            throws SQLException {

        if (keyword == null
                || keyword.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Search keyword cannot be empty"
            );
        }

        return userDAO.searchUsers(
                keyword.trim()
        );
    }

    @Override
    public void updateUser(User user)
            throws SQLException {

        if (user == null) {
            throw new IllegalArgumentException(
                    "User cannot be null"
            );
        }

        if (user.getId() <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be greater than zero"
            );
        }

        validateUser(user);

        String hashedPassword =
                BCrypt.hashpw(
                        user.getPassword(),
                        BCrypt.gensalt()
                );

        user.setPassword(hashedPassword);

        userDAO.updateUser(user);

        logger.info(
                "User updated successfully. ID: "
                        + user.getId()
        );
    }

    @Override
    public void deleteUser(int id)
            throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be greater than zero"
            );
        }

        userDAO.deleteUser(id);

        logger.info(
                "User deleted successfully. ID: "
                        + id
        );
    }

    @Override
    public User login(
            String username,
            String password)
            throws SQLException {

        validateUsername(username);

        if (password == null
                || password.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Password cannot be empty"
            );
        }

        User user =
                userDAO.getUserByUsername(username);

        if (user == null) {

            logger.warning(
                    "Login failed. User not found: "
                            + username
            );

            return null;
        }

        boolean passwordMatches =
                BCrypt.checkpw(
                        password,
                        user.getPassword()
                );

        if (!passwordMatches) {

            logger.warning(
                    "Login failed. Invalid password for user: "
                            + username
            );

            return null;
        }

        logger.info(
                "User login successful: "
                        + username
        );

        return user;
    }

    private void validateUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException(
                    "User cannot be null"
            );
        }

        validateUsername(user.getUsername());

        if (user.getPassword() == null
                || user.getPassword().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Password cannot be empty"
            );
        }

        if (user.getFirstName() == null
                || user.getFirstName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "First name cannot be empty"
            );
        }

        if (user.getLastName() == null
                || user.getLastName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Last name cannot be empty"
            );
        }
    }

    private void validateUsername(String username) {

        if (username == null
                || username.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Username cannot be empty"
            );
        }
    }
}