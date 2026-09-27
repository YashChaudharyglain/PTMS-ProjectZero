package com.ptms.app.controller;

import com.ptms.app.model.Project;
import com.ptms.app.service.ProjectService;

import java.sql.SQLException;
import java.util.List;

public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void createProject(Project project)
            throws SQLException {

        projectService.createProject(project);
    }

    public Project getProjectById(int id)
            throws SQLException {

        return projectService.getProjectById(id);
    }

    public List<Project> getAllProjects()
            throws SQLException {

        return projectService.getAllProjects();
    }

    public List<Project> searchProjects(String keyword)
            throws SQLException {

        return projectService.searchProjects(keyword);
    }

    public void updateProject(Project project)
            throws SQLException {

        projectService.updateProject(project);
    }

    public void deleteProject(int id)
            throws SQLException {

        projectService.deleteProject(id);
    }
}