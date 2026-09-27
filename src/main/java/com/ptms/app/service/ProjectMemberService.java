package com.ptms.app.service;

import com.ptms.app.model.ProjectMember;

import java.sql.SQLException;
import java.util.List;

public interface ProjectMemberService {

    boolean addMember(ProjectMember member) throws SQLException;

    boolean removeMember(int projectId, int userId) throws SQLException;

    boolean isMember(int projectId, int userId) throws SQLException;

    List<ProjectMember> findByProject(int projectId) throws SQLException;

    List<ProjectMember> findByUser(int userId) throws SQLException;

    boolean updateRole(int projectId, int userId, String roleInProject)
            throws SQLException;
}