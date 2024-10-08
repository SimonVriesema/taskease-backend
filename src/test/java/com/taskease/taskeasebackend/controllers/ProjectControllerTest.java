package com.taskease.taskeasebackend.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.taskease.taskeasebackend.dto.request.CreateProjectRequest;
import com.taskease.taskeasebackend.dto.response.ProjectDTO;
import com.taskease.taskeasebackend.dto.response.UserDTO;
import com.taskease.taskeasebackend.enums.Status;
import com.taskease.taskeasebackend.exceptions.ProjectLeaderNotFoundException;
import com.taskease.taskeasebackend.exceptions.ProjectNotFoundException;
import com.taskease.taskeasebackend.exceptions.UserAlreadyInProjectException;
import com.taskease.taskeasebackend.exceptions.UserNotFoundException;
import com.taskease.taskeasebackend.models.Project;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.services.ProjectService;
import com.taskease.taskeasebackend.services.UserService;
import com.taskease.taskeasebackend.utils.DTOConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProjectController projectController;

    private User projectLeader;
    private Project project;
    private ProjectDTO projectDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        projectLeader = new User();
        projectLeader.setId(1L);
        projectLeader.setEmail("Project Leader Email");

        project = new Project();
        project.setId(1L);
        project.setProjectLeader(projectLeader);
        project.getUsers().add(projectLeader);

        projectDTO = DTOConvertor.convertToDTO(project);
    }

    @Test
    void testGetProjects() {
        List<Project> projects = Collections.singletonList(project);
        List<ProjectDTO> projectDTOs = Collections.singletonList(projectDTO);

        when(projectService.getAllProjects()).thenReturn(projects);

        ResponseEntity<List<ProjectDTO>> response = projectController.getProjects();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(projectDTOs, response.getBody());
    }

    @Test
    void testGetProjectById() throws ProjectNotFoundException {
        when(projectService.findById(1L)).thenReturn(project);

        ResponseEntity<ProjectDTO> response = projectController.getProjectById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(projectDTO, response.getBody());
    }

    @Test
    void testCreateProject() throws ProjectLeaderNotFoundException {
        CreateProjectRequest request = new CreateProjectRequest();
        request.setProjectLeaderId(1L);

        when(userService.getUserEntityById(1L)).thenReturn(projectLeader);
        when(projectService.saveProject(any(Project.class))).thenReturn(project);

        ResponseEntity<ProjectDTO> response = projectController.createProject(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(projectDTO, response.getBody());
    }

    @Test
    void testDeleteProjectById() throws ProjectNotFoundException {
        doNothing().when(projectService).deleteProjectById(1L);

        ResponseEntity<Void> response = projectController.deleteProjectById(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testUpdateProjectById() throws ProjectNotFoundException {
        Project project = new Project();

        when(projectService.doesProjectExist(1L)).thenReturn(true);
        doAnswer(invocation -> {
            return null;
        }).when(projectService).updateProject(1L, project);

        ResponseEntity<Void> response = projectController.updateProjectById(1L, project);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetProjectsByUserId() throws ProjectNotFoundException, UserNotFoundException {
        when(projectService.getProjectsByUserId(1L)).thenReturn(Collections.singletonList(project));

        ResponseEntity<List<ProjectDTO>> response = projectController.getProjectsByUserId(1L);

        List<ProjectDTO> expectedProjectDTOs = Collections.singletonList(projectDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProjectDTOs, response.getBody());
    }

    @Test
    void testAddUserToProject() throws ProjectNotFoundException, UserNotFoundException, UserAlreadyInProjectException {
        ProjectDTO projectDTO = new ProjectDTO();

        when(projectService.addUserToProject(1L, 1L)).thenReturn(projectDTO);

        ResponseEntity<ProjectDTO> response = projectController.addUserToProject(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(projectDTO, response.getBody());
    }

    @Test
    void testGetStatusFromProject() throws ProjectNotFoundException {
        Project project = new Project();
        project.setStatus(Status.IN_PROGRESS);

        when(projectService.findById(1L)).thenReturn(project);

        ResponseEntity<Status> response = projectController.getStatusFromProject(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Status.IN_PROGRESS, response.getBody());
    }

    @Test
    void testRemoveUserFromProject() throws ProjectNotFoundException, UserNotFoundException {
        ProjectDTO projectDTO = new ProjectDTO();

        when(projectService.removeUserFromProject(1L, 1L)).thenReturn(projectDTO);

        ResponseEntity<ProjectDTO> response = projectController.removeUserFromProject(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(projectDTO, response.getBody());
    }

    @Test
    void testGetUsersByProjectId() throws ProjectNotFoundException {
        UserDTO userDTO = new UserDTO();
        List<UserDTO> userDTOs = Collections.singletonList(userDTO);

        when(projectService.getUsersByProjectId(1L)).thenReturn(userDTOs);

        ResponseEntity<List<UserDTO>> response = projectController.getUsersByProjectId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTOs, response.getBody());
    }
}