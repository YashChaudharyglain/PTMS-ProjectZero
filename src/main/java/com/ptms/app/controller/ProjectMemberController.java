package com.ptms.app.controller;

import com.ptms.app.model.ProjectMember;
import com.ptms.app.service.ProjectMemberService;

import java.sql.SQLException;
import java.util.List;

public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    public ProjectMemberController(
            ProjectMemberService projectMemberService) {

        this.projectMemberService = projectMemberService;
    }

    public boolean addMember(ProjectMember member)
            throws SQLException {

        return projectMemberService.addMember(member);
    }

    public boolean removeMember(
            int projectId,
            int userId)
            throws SQLException {

        return projectMemberService.removeMember(
                projectId,
                userId
        );
    }

    public boolean isMember(
            int projectId,
            int userId)
            throws SQLException {

        return projectMemberService.isMember(
                projectId,
                userId
        );
    }

    public List<ProjectMember> findByProject(
            int projectId)
            throws SQLException {

        return projectMemberService.findByProject(
                projectId
        );
    }

    public List<ProjectMember> findByUser(
            int userId)
            throws SQLException {

        return projectMemberService.findByUser(
                userId
        );
    }

    public boolean updateRole(
            int projectId,
            int userId,
            String roleInProject)
            throws SQLException {

        return projectMemberService.updateRole(
                projectId,
                userId,
                roleInProject
        );
    }
}