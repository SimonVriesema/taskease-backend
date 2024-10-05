package com.taskease.taskeasebackend.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.taskease.taskeasebackend.models.Project;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.repositories.ProjectRepository;
import com.taskease.taskeasebackend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import java.util.Collections;

public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveProject() {
        Project project = new Project();
        User user = new User();
        user.setId(1L);
        project.setProjectLeader(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepository.save(project)).thenReturn(project);

        Project savedProject = projectService.saveProject(project);

        assertNotNull(savedProject);
        assertEquals(user, savedProject.getProjectLeader());
    }

    @Test
    public void testDeleteProjectById() {
        Project project = new Project();
        project.setId(1L);

        when(projectRepository.existsById(1L)).thenReturn(true);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        doNothing().when(projectRepository).deleteById(1L);

        projectService.deleteProjectById(1L);

        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDoesProjectExist() {
        when(projectRepository.existsById(1L)).thenReturn(true);

        boolean exists = projectService.doesProjectExist(1L);

        assertTrue(exists);
    }

    @Test
    public void testFindById() {
        Project project = new Project();
        project.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project foundProject = projectService.findById(1L);

        assertNotNull(foundProject);
        assertEquals(1L, foundProject.getId());
    }

    @Test
    public void testGetAllProjects() {
        Project project = new Project();
        when(projectRepository.findAll()).thenReturn(Collections.singletonList(project));

        List<Project> projects = projectService.getAllProjects();

        assertNotNull(projects);
        assertFalse(projects.isEmpty());
    }

    @Test
    public void testGetProjectsByUserId() {
        Project project = new Project();
        when(projectRepository.findByProjectLeader_Id(1L)).thenReturn(Collections.singletonList(project));

        List<Project> projects = projectService.getProjectsByUserId(1L);

        assertNotNull(projects);
        assertFalse(projects.isEmpty());
    }

    @Test
    public void testUpdateProject() {
        Project project = new Project();
        project.setId(1L);

        when(projectRepository.existsById(1L)).thenReturn(true);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);

        Project updatedProject = projectService.updateProject(1L, project);

        assertNotNull(updatedProject);
        assertEquals(1L, updatedProject.getId());
    }

    @Test
    public void testRemoveUserFromProjectInternal() {
        Project project = new Project();
        User user = new User();
        user.setId(1L);
        project.getUsers().add(user);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Project updatedProject = projectService.removeUserFromProjectInternal(1L, 1L);

        assertNotNull(updatedProject);
        assertFalse(updatedProject.getUsers().contains(user));
    }

    @Test
    public void testAddUserToProjectInternal() {
        Project project = new Project();
        User user = new User();
        user.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Project updatedProject = projectService.addUserToProjectInternal(1L, 1L);

        assertNotNull(updatedProject);
        assertTrue(updatedProject.getUsers().contains(user));
    }
}