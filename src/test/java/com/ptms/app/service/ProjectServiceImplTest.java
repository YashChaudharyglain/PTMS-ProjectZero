package com.ptms.app.service;

import com.ptms.app.dao.ProjectDAO;
import com.ptms.app.model.Project;
import com.ptms.app.service.impl.ProjectServiceImpl;
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
class ProjectServiceImplTest {

    @Mock
    private ProjectDAO projectDAO;

    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        projectService =
                new ProjectServiceImpl(projectDAO);
    }

    @Test
    void createProjectShouldCallDAO()
            throws SQLException {

        Project project = mock(Project.class);

        when(project.getName())
                .thenReturn("PTMS");

        when(project.getRequirements())
                .thenReturn("Project management system");

        projectService.createProject(project);

        verify(projectDAO)
                .createProject(project);
    }

    @Test
    void getProjectByIdShouldReturnProject()
            throws SQLException {

        Project project = mock(Project.class);

        when(projectDAO.getProjectById(1))
                .thenReturn(project);

        Project result =
                projectService.getProjectById(1);

        assertSame(project, result);

        verify(projectDAO)
                .getProjectById(1);
    }

    @Test
    void getAllProjectsShouldReturnProjects()
            throws SQLException {

        List<Project> projects =
                List.of(mock(Project.class));

        when(projectDAO.getAllProjects())
                .thenReturn(projects);

        List<Project> result =
                projectService.getAllProjects();

        assertEquals(1, result.size());

        verify(projectDAO)
                .getAllProjects();
    }

    @Test
    void searchProjectsShouldCallDAO()
            throws SQLException {

        when(projectDAO.searchProjects("PTMS"))
                .thenReturn(
                        List.of(mock(Project.class))
                );

        List<Project> result =
                projectService.searchProjects("PTMS");

        assertEquals(1, result.size());

        verify(projectDAO)
                .searchProjects("PTMS");
    }

    @Test
    void updateProjectShouldCallDAO()
            throws SQLException {

        Project project = mock(Project.class);

        when(project.getId()).thenReturn(1);
        when(project.getName()).thenReturn("PTMS");
        when(project.getRequirements())
                .thenReturn("Requirements");

        projectService.updateProject(project);

        verify(projectDAO)
                .updateProject(project);
    }

    @Test
    void deleteProjectShouldCallDAO()
            throws SQLException {

        projectService.deleteProject(1);

        verify(projectDAO)
                .deleteProject(1);
    }

    @Test
    void createProjectShouldRejectNull() {

        assertThrows(
                IllegalArgumentException.class,
                () -> projectService.createProject(null)
        );

        verifyNoInteractions(projectDAO);
    }

    @Test
    void getProjectByIdShouldRejectInvalidId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> projectService.getProjectById(0)
        );

        verifyNoInteractions(projectDAO);
    }
}