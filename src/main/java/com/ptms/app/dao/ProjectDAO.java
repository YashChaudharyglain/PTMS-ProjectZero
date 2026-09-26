package com.ptms.app.dao;

import com.ptms.app.model.Project;

import java.sql.SQLException;
import java.util.List;

public interface ProjectDAO {

    void createProject(Project project) throws SQLException;

    Project getProjectById(int id) throws SQLException;

    List<Project> getAllProjects() throws SQLException;

    List<Project> searchProjects(String keyword) throws SQLException;

    void updateProject(Project project) throws SQLException;

    void deleteProject(int id) throws SQLException;
}