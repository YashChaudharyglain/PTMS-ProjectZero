package com.ptms.app.service.impl;

import com.ptms.app.dao.ProjectMemberDAO;
import com.ptms.app.model.ProjectMember;
import com.ptms.app.service.ProjectMemberService;
import com.ptms.app.util.LoggerUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ProjectMemberServiceImpl
        implements ProjectMemberService {

    private static final Logger logger =
            LoggerUtil.getLogger(ProjectMemberServiceImpl.class);

    private final ProjectMemberDAO projectMemberDAO;

    public ProjectMemberServiceImpl(
            ProjectMemberDAO projectMemberDAO) {

        this.projectMemberDAO = projectMemberDAO;
    }

    @Override
    public boolean addMember(ProjectMember member)
            throws SQLException {

        validateMember(member);

        boolean result =
                projectMemberDAO.addMember(member);

        if (result) {
            logger.info(
                    "Project member added successfully. Project ID: "
                            + member.getProjectId()
                            + ", User ID: "
                            + member.getUserId()
            );
        }

        return result;
    }

    @Override
    public boolean removeMember(
            int projectId,
            int userId)
            throws SQLException {

        validateIds(projectId, userId);

        boolean result =
                projectMemberDAO.removeMember(
                        projectId,
                        userId
                );

        if (result) {
            logger.info(
                    "Project member removed successfully. Project ID: "
                            + projectId
                            + ", User ID: "
                            + userId
            );
        }

        return result;
    }

    @Override
    public boolean isMember(
            int projectId,
            int userId)
            throws SQLException {

        validateIds(projectId, userId);

        return projectMemberDAO.isMember(
                projectId,
                userId
        );
    }

    @Override
    public List<ProjectMember> findByProject(
            int projectId)
            throws SQLException {

        if (projectId <= 0) {
            throw new IllegalArgumentException(
                    "Project ID must be greater than zero"
            );
        }

        return projectMemberDAO.findByProject(
                projectId
        );
    }

    @Override
    public List<ProjectMember> findByUser(
            int userId)
            throws SQLException {

        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be greater than zero"
            );
        }

        return projectMemberDAO.findByUser(
                userId
        );
    }

    @Override
    public boolean updateRole(
            int projectId,
            int userId,
            String roleInProject)
            throws SQLException {

        validateIds(projectId, userId);

        if (roleInProject == null
                || roleInProject.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Role in project cannot be empty"
            );
        }

        boolean result =
                projectMemberDAO.updateRole(
                        projectId,
                        userId,
                        roleInProject.trim()
                );

        if (result) {
            logger.info(
                    "Project member role updated successfully. Project ID: "
                            + projectId
                            + ", User ID: "
                            + userId
            );
        }

        return result;
    }

    private void validateMember(
            ProjectMember member) {

        if (member == null) {
            throw new IllegalArgumentException(
                    "Project member cannot be null"
            );
        }

        if (member.getProjectId() <= 0) {
            throw new IllegalArgumentException(
                    "Project ID must be greater than zero"
            );
        }

        if (member.getUserId() <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be greater than zero"
            );
        }

        if (member.getRoleInProject() == null
                || member.getRoleInProject().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Role in project cannot be empty"
            );
        }
    }

    private void validateIds(
            int projectId,
            int userId) {

        if (projectId <= 0) {
            throw new IllegalArgumentException(
                    "Project ID must be greater than zero"
            );
        }

        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be greater than zero"
            );
        }
    }
}