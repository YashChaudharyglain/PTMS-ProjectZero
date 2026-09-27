package com.ptms.app.service;

import com.ptms.app.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {

    void createUser(User user) throws SQLException;

    User getUserById(int id) throws SQLException;

    User getUserByUsername(String username) throws SQLException;

    List<User> getAllUsers() throws SQLException;

    List<User> searchUsers(String keyword) throws SQLException;

    void updateUser(User user) throws SQLException;

    void deleteUser(int id) throws SQLException;

    User login(String username, String password) throws SQLException;
}