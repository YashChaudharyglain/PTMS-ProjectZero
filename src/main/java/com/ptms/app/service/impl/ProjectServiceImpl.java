package com.ptms.app.service.impl;

import com.ptms.app.dao.ProjectDAO;
import com.ptms.app.model.Project;
import com.ptms.app.service.ProjectService;
import com.ptms.app.util.LoggerUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger =
            LoggerUtil.getLogger(ProjectServiceImpl.class);

    private final ProjectDAO projectDAO;

    public ProjectServiceImpl(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @Override
    public void createProject(Project project)
            throws SQLException {

        validateProject(project);

        projectDAO.createProject(project);

        logger.info(
                "Project created successfully: "
                        + project.getName()
        );
    }

    @Override
    public Project getProjectById(int id)
            throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Project ID must be greater than zero"
            );
        }

        return projectDAO.getProjectById(id);
    }

    @Override
    public List<Project> getAllProjects()
            throws SQLException {

        return projectDAO.getAllProjects();
    }

    @Override
    public List<Project> searchProjects(String keyword)
            throws SQLException {

        if (keyword == null
                || keyword.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Search keyword cannot be empty"
            );
        }

        return projectDAO.searchProjects(
                keyword.trim()
        );
    }

    @Override
    public void updateProject(Project project)
            throws SQLException {

        if (project == null) {
            throw new IllegalArgumentException(
                    "Project cannot be null"
            );
        }

        if (project.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Project ID must be greater than zero"
            );
        }

        validateProject(project);

        projectDAO.updateProject(project);

        logger.info(
                "Project updated successfully. ID: "
                        + project.getId()
        );
    }

    @Override
    public void deleteProject(int id)
            throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Project ID must be greater than zero"
            );
        }

        projectDAO.deleteProject(id);

        logger.info(
                "Project deleted successfully. ID: "
                        + id
        );
    }

    private void validateProject(Project project) {

        if (project == null) {
            throw new IllegalArgumentException(
                    "Project cannot be null"
            );
        }

        if (project.getName() == null
                || project.getName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Project name cannot be empty"
            );
        }

        if (project.getRequirements() == null
                || project.getRequirements().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Project requirements cannot be empty"
            );
        }
    }
}