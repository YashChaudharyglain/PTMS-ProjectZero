package com.ptms.app.dao.impl;

import com.ptms.app.dao.UserDAO;
import com.ptms.app.model.User;
import com.ptms.app.util.DBConnection;
import com.ptms.app.util.LoggerUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAOImpl implements UserDAO {

    private static final Logger logger =
            LoggerUtil.getLogger(UserDAOImpl.class);

    @Override
    public void createUser(User user) throws SQLException {

        String sql = """
                INSERT INTO users
                (first_name, last_name, username, email, password,
                 role_name, date_of_birth, mobile_number, gender)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getRoleName());

            if (user.getDateOfBirth() == null) {
                statement.setNull(7, java.sql.Types.DATE);
            } else {
                statement.setDate(
                        7,
                        java.sql.Date.valueOf(
                                user.getDateOfBirth()
                        )
                );
            }

            statement.setString(8, user.getMobileNumber());
            statement.setString(9, user.getGender());

            statement.executeUpdate();

            logger.info(
                    "User created successfully: "
                            + user.getUsername()
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while creating user",
                    e
            );

            throw e;
        }
    }

    @Override
    public User getUserById(int id) throws SQLException {

        String sql =
                "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    logger.info(
                            "User found with ID: " + id
                    );

                    return mapUser(resultSet);
                }
            }

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while getting user with ID: " + id,
                    e
            );

            throw e;
        }

        logger.warning(
                "User not found with ID: " + id
        );

        return null;
    }

    @Override
    public User getUserByUsername(
            String username) throws SQLException {

        String sql =
                "SELECT * FROM users WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    logger.info(
                            "User found with username: "
                                    + username
                    );

                    return mapUser(resultSet);
                }
            }

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while getting user by username",
                    e
            );

            throw e;
        }

        logger.warning(
                "User not found with username: "
                        + username
        );

        return null;
    }

    @Override
    public List<User> getAllUsers()
            throws SQLException {

        String sql =
                "SELECT * FROM users";

        List<User> users = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                users.add(
                        mapUser(resultSet)
                );
            }

            logger.info(
                    "All users fetched successfully"
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while getting all users",
                    e
            );

            throw e;
        }

        return users;
    }

    @Override
    public List<User> searchUsers(
            String keyword) throws SQLException {

        String sql = """
                SELECT * FROM users
                WHERE first_name LIKE ?
                   OR last_name LIKE ?
                   OR username LIKE ?
                   OR email LIKE ?
                """;

        List<User> users = new ArrayList<>();

        String searchKeyword =
                "%" + keyword + "%";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, searchKeyword);
            statement.setString(2, searchKeyword);
            statement.setString(3, searchKeyword);
            statement.setString(4, searchKeyword);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    users.add(
                            mapUser(resultSet)
                    );
                }
            }

            logger.info(
                    "User search completed for: "
                            + keyword
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while searching users",
                    e
            );

            throw e;
        }

        return users;
    }

    @Override
    public void updateUser(User user)
            throws SQLException {

        String sql = """
                UPDATE users
                SET first_name = ?,
                    last_name = ?,
                    username = ?,
                    email = ?,
                    password = ?,
                    role_name = ?,
                    date_of_birth = ?,
                    mobile_number = ?,
                    gender = ?
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getRoleName());

            if (user.getDateOfBirth() == null) {
                statement.setNull(7, java.sql.Types.DATE);
            } else {
                statement.setDate(
                        7,
                        java.sql.Date.valueOf(
                                user.getDateOfBirth()
                        )
                );
            }

            statement.setString(8, user.getMobileNumber());
            statement.setString(9, user.getGender());
            statement.setInt(10, user.getId());

            statement.executeUpdate();

            logger.info(
                    "User updated successfully with ID: "
                            + user.getId()
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while updating user: "
                            + user.getId(),
                    e
            );

            throw e;
        }
    }

    @Override
    public void deleteUser(int id)
            throws SQLException {

        String sql =
                "DELETE FROM users WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

            logger.info(
                    "User deleted successfully with ID: "
                            + id
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while deleting user: " + id,
                    e
            );

            throw e;
        }
    }

    private User mapUser(
            ResultSet resultSet)
            throws SQLException {

        return new User(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("username"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("role_name"),
                resultSet.getDate("date_of_birth")
                        != null
                        ? resultSet.getDate("date_of_birth")
                        .toLocalDate()
                        : null,
                resultSet.getString("mobile_number"),
                resultSet.getString("gender")
        );
    }
}