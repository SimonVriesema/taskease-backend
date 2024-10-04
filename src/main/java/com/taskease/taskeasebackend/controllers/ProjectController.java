package com.taskease.taskeasebackend.controllers;

import com.taskease.taskeasebackend.dto.request.CreateProjectRequest;
import com.taskease.taskeasebackend.dto.response.ProjectDTO;
import com.taskease.taskeasebackend.dto.response.UserDTO;
import com.taskease.taskeasebackend.enums.Status;
import com.taskease.taskeasebackend.exceptions.ProjectLeaderNotFoundException;
import com.taskease.taskeasebackend.exceptions.ProjectNotFoundException;
import com.taskease.taskeasebackend.exceptions.UserAlreadyInProjectException;
import com.taskease.taskeasebackend.exceptions.UserNotFoundException;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.services.UserService;
import com.taskease.taskeasebackend.utils.DTOConvertor;
import jakarta.validation.Valid;


import com.taskease.taskeasebackend.models.Project;
import com.taskease.taskeasebackend.services.ProjectService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping()
    @ApiOperation(value = "Get all projects", notes = "Retrieve a list of all projects")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the projects"),
            @ApiResponse(code = 404, message = "No projects found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<List<ProjectDTO>> getProjects() {
        try {
            List<Project> projects = projectService.getAllProjects();
            List<ProjectDTO> projectDTOs =
                    projects.stream().map(DTOConvertor::convertToDTO).collect(Collectors.toList());
            return ResponseEntity.ok(projectDTOs);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get project by ID", notes = "Retrieve a project by its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the project"),
            @ApiResponse(code = 404, message = "Project not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable long id) {
        try {
            Project project = projectService.findById(id);
            ProjectDTO projectDTO = DTOConvertor.convertToDTO(project);
            return ResponseEntity.ok(projectDTO);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping()
    @ApiOperation(value = "Create a new project")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created the project"),
            @ApiResponse(code = 400, message = "Invalid input data"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<ProjectDTO> createProject(@RequestBody @Valid CreateProjectRequest createProjectRequest) {
        try {
            User projectLeader = userService.getUserEntityById(createProjectRequest.getProjectLeaderId());
            if (projectLeader == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            Project project = new Project();
            project.setTitle(createProjectRequest.getTitle());
            project.setDescription(createProjectRequest.getDescription());
            project.setProjectLeader(projectLeader);
            project.setStatus(createProjectRequest.getStatus());
            project.getUsers().add(projectLeader);
            Project createdProject = projectService.saveProject(project);
            ProjectDTO projectDTO = DTOConvertor.convertToDTO(createdProject);
            return ResponseEntity.status(HttpStatus.CREATED).body(projectDTO);
        } catch (ProjectLeaderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete project by ID", notes = "Delete project by providing its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the project"),
            @ApiResponse(code = 404, message = "The project you were trying to delete is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Void> deleteProjectById(@PathVariable Long id) {
        try {
            projectService.deleteProjectById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update project by ID", notes = "Update project by providing its ID")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Successfully updated the project"),
            @ApiResponse(code = 404, message = "The project you were trying to update is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Void> updateProjectById(@PathVariable Long id, @RequestBody Project project) {
        try {
            if (projectService.doesProjectExist(id)) {
                projectService.updateProject(id, project);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}")
    @ApiOperation(value = "Get projects by user ID", notes = "Retrieve projects where the user is either in the " +
            "users" + " list" + " or is the project leader")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the projects"),
            @ApiResponse(code = 404, message = "Projects not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<List<ProjectDTO>> getProjectsByUserId(@PathVariable Long userId) {
        try {
            List<Project> projects = projectService.getProjectsByUserId(userId);
            List<ProjectDTO> projectDTOs =
                    projects.stream().map(DTOConvertor::convertToDTO).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(projectDTOs);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{projectId}/addUser/{userId}")
    @ApiOperation(value = "Add an existing user to a project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added the user to the project"),
            @ApiResponse(code = 404, message = "Project or user not found"),
            @ApiResponse(code = 409, message = "User is already in the project"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<ProjectDTO> addUserToProject(@PathVariable Long projectId, @PathVariable Long userId) {
        try {
            ProjectDTO updatedProject = projectService.addUserToProject(projectId, userId);
            return ResponseEntity.ok(updatedProject);
        } catch (UserAlreadyInProjectException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (ProjectNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{projectId}/status")
    @ApiOperation(value = "Get the status of a project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the project status"),
            @ApiResponse(code = 404, message = "Project not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Status> getStatusFromProject(@PathVariable Long projectId) {
        try {
            Project project = projectService.findById(projectId);
            return ResponseEntity.ok(project.getStatus());
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{projectId}/{userId}")
    @ApiOperation(value = "Remove a user from a project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully removed the user from the project"),
            @ApiResponse(code = 404, message = "Project or user not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<ProjectDTO> removeUserFromProject(@PathVariable Long projectId, @PathVariable Long userId) {
        try {
            ProjectDTO project = projectService.removeUserFromProject(projectId, userId);
            return ResponseEntity.ok(project);
        } catch (ProjectNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{projectId}/users")
    @ApiOperation(value = "Get all users from a project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the users"),
            @ApiResponse(code = 404, message = "Project not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<List<UserDTO>> getUsersByProjectId(@PathVariable Long projectId) {
        try {
            List<UserDTO> users = projectService.getUsersByProjectId(projectId);
            return ResponseEntity.ok(users);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
