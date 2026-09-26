package com.ptms.app.dao.impl;

import com.ptms.app.dao.ProjectDAO;
import com.ptms.app.model.Project;
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

public class ProjectDAOImpl implements ProjectDAO {

    private static final Logger logger =
            LoggerUtil.getLogger(ProjectDAOImpl.class);

    @Override
    public void createProject(Project project)
            throws SQLException {

        String sql = """
                INSERT INTO projects
                (name, requirements, manager_id, team_lead_id,
                 client_id, domain, cost, start_date, deadline,
                 priority, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, project.getName());
            statement.setString(2, project.getRequirements());
            statement.setInt(3, project.getManagerId());
            statement.setInt(4, project.getTeamLeadId());
            statement.setInt(5, project.getClientId());
            statement.setString(6, project.getDomain());
            statement.setBigDecimal(7, project.getCost());

            if (project.getStartDate() == null) {
                statement.setNull(
                        8,
                        java.sql.Types.DATE
                );
            } else {
                statement.setDate(
                        8,
                        java.sql.Date.valueOf(
                                project.getStartDate()
                        )
                );
            }

            if (project.getDeadline() == null) {
                statement.setNull(
                        9,
                        java.sql.Types.DATE
                );
            } else {
                statement.setDate(
                        9,
                        java.sql.Date.valueOf(
                                project.getDeadline()
                        )
                );
            }

            statement.setString(10, project.getPriority());
            statement.setString(11, project.getStatus());

            statement.executeUpdate();

            logger.info(
                    "Project created successfully: "
                            + project.getName()
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while creating project",
                    e
            );

            throw e;
        }
    }

    @Override
    public Project getProjectById(int id)
            throws SQLException {

        String sql =
                "SELECT * FROM projects WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    logger.info(
                            "Project found with ID: " + id
                    );

                    return mapProject(resultSet);
                }
            }

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while getting project with ID: "
                            + id,
                    e
            );

            throw e;
        }

        logger.warning(
                "Project not found with ID: " + id
        );

        return null;
    }

    @Override
    public List<Project> getAllProjects()
            throws SQLException {

        String sql =
                "SELECT * FROM projects";

        List<Project> projects = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                projects.add(
                        mapProject(resultSet)
                );
            }

            logger.info(
                    "All projects fetched successfully"
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while getting all projects",
                    e
            );

            throw e;
        }

        return projects;
    }

    @Override
    public List<Project> searchProjects(
            String keyword)
            throws SQLException {

        String sql = """
                SELECT * FROM projects
                WHERE name LIKE ?
                   OR requirements LIKE ?
                   OR domain LIKE ?
                   OR priority LIKE ?
                   OR status LIKE ?
                """;

        List<Project> projects = new ArrayList<>();

        String searchKeyword =
                "%" + keyword + "%";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, searchKeyword);
            statement.setString(2, searchKeyword);
            statement.setString(3, searchKeyword);
            statement.setString(4, searchKeyword);
            statement.setString(5, searchKeyword);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    projects.add(
                            mapProject(resultSet)
                    );
                }
            }

            logger.info(
                    "Project search completed for: "
                            + keyword
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while searching projects",
                    e
            );

            throw e;
        }

        return projects;
    }

    @Override
    public void updateProject(Project project)
            throws SQLException {

        String sql = """
                UPDATE projects
                SET name = ?,
                    requirements = ?,
                    manager_id = ?,
                    team_lead_id = ?,
                    client_id = ?,
                    domain = ?,
                    cost = ?,
                    start_date = ?,
                    deadline = ?,
                    priority = ?,
                    status = ?
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, project.getName());
            statement.setString(2, project.getRequirements());
            statement.setInt(3, project.getManagerId());
            statement.setInt(4, project.getTeamLeadId());
            statement.setInt(5, project.getClientId());
            statement.setString(6, project.getDomain());
            statement.setBigDecimal(7, project.getCost());

            if (project.getStartDate() == null) {
                statement.setNull(
                        8,
                        java.sql.Types.DATE
                );
            } else {
                statement.setDate(
                        8,
                        java.sql.Date.valueOf(
                                project.getStartDate()
                        )
                );
            }

            if (project.getDeadline() == null) {
                statement.setNull(
                        9,
                        java.sql.Types.DATE
                );
            } else {
                statement.setDate(
                        9,
                        java.sql.Date.valueOf(
                                project.getDeadline()
                        )
                );
            }

            statement.setString(10, project.getPriority());
            statement.setString(11, project.getStatus());
            statement.setInt(12, project.getId());

            statement.executeUpdate();

            logger.info(
                    "Project updated successfully with ID: "
                            + project.getId()
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while updating project: "
                            + project.getId(),
                    e
            );

            throw e;
        }
    }

    @Override
    public void deleteProject(int id)
            throws SQLException {

        String sql =
                "DELETE FROM projects WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

            logger.info(
                    "Project deleted successfully with ID: "
                            + id
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while deleting project: " + id,
                    e
            );

            throw e;
        }
    }

    private Project mapProject(
            ResultSet resultSet)
            throws SQLException {

        return new Project(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("requirements"),
                resultSet.getInt("manager_id"),
                resultSet.getInt("team_lead_id"),
                resultSet.getInt("client_id"),
                resultSet.getString("domain"),
                resultSet.getBigDecimal("cost"),
                resultSet.getDate("start_date")
                        != null
                        ? resultSet.getDate("start_date")
                        .toLocalDate()
                        : null,
                resultSet.getDate("deadline")
                        != null
                        ? resultSet.getDate("deadline")
                        .toLocalDate()
                        : null,
                resultSet.getString("priority"),
                resultSet.getString("status")
        );
    }
}