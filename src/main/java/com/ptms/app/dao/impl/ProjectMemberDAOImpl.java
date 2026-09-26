package com.ptms.app.dao.impl;

import com.ptms.app.dao.ProjectMemberDAO;
import com.ptms.app.model.ProjectMember;
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

public class ProjectMemberDAOImpl
        implements ProjectMemberDAO {

    private static final Logger logger =
            LoggerUtil.getLogger(ProjectMemberDAOImpl.class);

    private static final String ADD_MEMBER =
            """
            INSERT INTO project_members
            (project_id, user_id, joined_at, role_in_project)
            VALUES (?, ?, ?, ?)
            """;

    private static final String REMOVE_MEMBER =
            """
            DELETE FROM project_members
            WHERE project_id = ?
              AND user_id = ?
            """;

    private static final String IS_MEMBER =
            """
            SELECT 1
            FROM project_members
            WHERE project_id = ?
              AND user_id = ?
            """;

    private static final String FIND_BY_PROJECT =
            """
            SELECT project_id,
                   user_id,
                   joined_at,
                   role_in_project
            FROM project_members
            WHERE project_id = ?
            """;

    private static final String FIND_BY_USER =
            """
            SELECT project_id,
                   user_id,
                   joined_at,
                   role_in_project
            FROM project_members
            WHERE user_id = ?
            """;

    private static final String UPDATE_ROLE =
            """
            UPDATE project_members
            SET role_in_project = ?
            WHERE project_id = ?
              AND user_id = ?
            """;

    @Override
    public boolean addMember(ProjectMember member)
            throws SQLException {

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(ADD_MEMBER)) {

            statement.setInt(
                    1,
                    member.getProjectId()
            );

            statement.setInt(
                    2,
                    member.getUserId()
            );

            if (member.getJoinedAt() == null) {
                statement.setNull(
                        3,
                        java.sql.Types.DATE
                );
            } else {
                statement.setDate(
                        3,
                        java.sql.Date.valueOf(
                                member.getJoinedAt()
                        )
                );
            }

            statement.setString(
                    4,
                    member.getRoleInProject()
            );

            boolean result =
                    statement.executeUpdate() > 0;

            logger.info(
                    "Project member added successfully. Project ID: "
                            + member.getProjectId()
                            + ", User ID: "
                            + member.getUserId()
            );

            return result;

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while adding project member",
                    e
            );

            throw e;
        }
    }

    @Override
    public boolean removeMember(
            int projectId,
            int userId)
            throws SQLException {

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(REMOVE_MEMBER)) {

            statement.setInt(1, projectId);
            statement.setInt(2, userId);

            boolean result =
                    statement.executeUpdate() > 0;

            logger.info(
                    "Project member removed successfully. Project ID: "
                            + projectId
                            + ", User ID: "
                            + userId
            );

            return result;

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while removing project member",
                    e
            );

            throw e;
        }
    }

    @Override
    public boolean isMember(
            int projectId,
            int userId)
            throws SQLException {

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(IS_MEMBER)) {

            statement.setInt(1, projectId);
            statement.setInt(2, userId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                boolean result =
                        resultSet.next();

                logger.info(
                        "Project membership checked. Project ID: "
                                + projectId
                                + ", User ID: "
                                + userId
                                + ", Is Member: "
                                + result
                );

                return result;
            }

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while checking project membership",
                    e
            );

            throw e;
        }
    }

    @Override
    public List<ProjectMember> findByProject(
            int projectId)
            throws SQLException {

        List<ProjectMember> members =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_PROJECT)) {

            statement.setInt(1, projectId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    members.add(
                            mapRow(resultSet)
                    );
                }
            }

            logger.info(
                    "Project members fetched successfully. Project ID: "
                            + projectId
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while finding project members",
                    e
            );

            throw e;
        }

        return members;
    }

    @Override
    public List<ProjectMember> findByUser(
            int userId)
            throws SQLException {

        List<ProjectMember> members =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_USER)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    members.add(
                            mapRow(resultSet)
                    );
                }
            }

            logger.info(
                    "User project memberships fetched successfully. User ID: "
                            + userId
            );

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while finding user projects",
                    e
            );

            throw e;
        }

        return members;
    }

    @Override
    public boolean updateRole(
            int projectId,
            int userId,
            String roleInProject)
            throws SQLException {

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_ROLE)) {

            statement.setString(
                    1,
                    roleInProject
            );

            statement.setInt(
                    2,
                    projectId
            );

            statement.setInt(
                    3,
                    userId
            );

            boolean result =
                    statement.executeUpdate() > 0;

            logger.info(
                    "Project member role updated successfully. Project ID: "
                            + projectId
                            + ", User ID: "
                            + userId
            );

            return result;

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Error while updating project member role",
                    e
            );

            throw e;
        }
    }

    private ProjectMember mapRow(
            ResultSet resultSet)
            throws SQLException {

        return new ProjectMember(
                resultSet.getInt("project_id"),
                resultSet.getInt("user_id"),
                resultSet.getDate("joined_at") != null
                        ? resultSet.getDate("joined_at")
                        .toLocalDate()
                        : null,
                resultSet.getString("role_in_project")
        );
    }
}