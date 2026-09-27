package com.ptms.app.controller;

import com.ptms.app.model.User;
import com.ptms.app.service.UserService;

import java.sql.SQLException;
import java.util.List;

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void createUser(User user) throws SQLException {
        userService.createUser(user);
    }

    public User getUserById(int id) throws SQLException {
        return userService.getUserById(id);
    }

    public User getUserByUsername(String username)
            throws SQLException {

        return userService.getUserByUsername(username);
    }

    public List<User> getAllUsers() throws SQLException {
        return userService.getAllUsers();
    }

    public List<User> searchUsers(String keyword)
            throws SQLException {

        return userService.searchUsers(keyword);
    }

    public void updateUser(User user) throws SQLException {
        userService.updateUser(user);
    }

    public void deleteUser(int id) throws SQLException {
        userService.deleteUser(id);
    }

    public User login(String username, String password)
            throws SQLException {

        return userService.login(username, password);
    }
}