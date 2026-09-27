package com.ptms.app.service;

import com.ptms.app.dao.ProjectMemberDAO;
import com.ptms.app.model.ProjectMember;
import com.ptms.app.service.impl.ProjectMemberServiceImpl;
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
class ProjectMemberServiceImplTest {

    @Mock
    private ProjectMemberDAO projectMemberDAO;

    private ProjectMemberServiceImpl projectMemberService;

    @BeforeEach
    void setUp() {
        projectMemberService =
                new ProjectMemberServiceImpl(
                        projectMemberDAO
                );
    }

    @Test
    void addMemberShouldCallDAO()
            throws SQLException {

        ProjectMember member =
                mock(ProjectMember.class);

        when(member.getProjectId())
                .thenReturn(1);

        when(member.getUserId())
                .thenReturn(2);

        when(member.getRoleInProject())
                .thenReturn("Developer");

        when(projectMemberDAO.addMember(member))
                .thenReturn(true);

        boolean result =
                projectMemberService.addMember(member);

        assertTrue(result);

        verify(projectMemberDAO)
                .addMember(member);
    }

    @Test
    void removeMemberShouldCallDAO()
            throws SQLException {

        when(projectMemberDAO.removeMember(1, 2))
                .thenReturn(true);

        boolean result =
                projectMemberService.removeMember(
                        1,
                        2
                );

        assertTrue(result);

        verify(projectMemberDAO)
                .removeMember(1, 2);
    }

    @Test
    void isMemberShouldReturnResult()
            throws SQLException {

        when(projectMemberDAO.isMember(1, 2))
                .thenReturn(true);

        boolean result =
                projectMemberService.isMember(1, 2);

        assertTrue(result);
    }

    @Test
    void findByProjectShouldReturnMembers()
            throws SQLException {

        when(projectMemberDAO.findByProject(1))
                .thenReturn(
                        List.of(
                                mock(ProjectMember.class)
                        )
                );

        List<ProjectMember> result =
                projectMemberService.findByProject(1);

        assertEquals(1, result.size());
    }

    @Test
    void findByUserShouldReturnMembers()
            throws SQLException {

        when(projectMemberDAO.findByUser(2))
                .thenReturn(
                        List.of(
                                mock(ProjectMember.class)
                        )
                );

        List<ProjectMember> result =
                projectMemberService.findByUser(2);

        assertEquals(1, result.size());
    }

    @Test
    void updateRoleShouldCallDAO()
            throws SQLException {

        when(projectMemberDAO.updateRole(
                1,
                2,
                "Team Lead"
        )).thenReturn(true);

        boolean result =
                projectMemberService.updateRole(
                        1,
                        2,
                        "Team Lead"
                );

        assertTrue(result);

        verify(projectMemberDAO)
                .updateRole(
                        1,
                        2,
                        "Team Lead"
                );
    }

    @Test
    void addMemberShouldRejectNull() {

        assertThrows(
                IllegalArgumentException.class,
                () -> projectMemberService.addMember(null)
        );

        verifyNoInteractions(projectMemberDAO);
    }
}