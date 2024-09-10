package com.taskease.taskeasebackend.controllers;

import com.taskease.taskeasebackend.exceptions.ProjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.Optional;


@RestController
@RequestMapping("/projects")
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping()
    @ApiOperation(value = "Create a project and store in the database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created the project"),
            @ApiResponse(code = 400, message = "Invalid input, object invalid"),
            @ApiResponse(code = 409, message = "An existing project already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        logger.info("Received Project: {}", project);
        try {
            Project createdProject = projectService.saveProject(project);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
        } catch (Exception e) {
            e.printStackTrace();
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
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully updated the project"),
            @ApiResponse(code = 404, message = "The project you were trying to update is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Void> updateProjectById(@PathVariable Long id, @RequestBody Project project) {
        try {
            if (projectService.findById(id)) {
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

    @GetMapping("/{userId}")
    @ApiOperation(value = "Get projects by user ID", notes = "Retrieve projects where the user is either in the users list or is the project leader")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the projects"),
            @ApiResponse(code = 404, message = "Projects not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<List<Project>> getProjectsByUserId(@PathVariable Long userId) {
        try {
            List<Project> projects = projectService.getProjectsByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(projects);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @PostMapping("/{projectId}/users/{userId}")
//    @ApiOperation(value = "Add a user to a project", notes = "Add a user to the specified project by their IDs")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully added the user to the project"),
//            @ApiResponse(code = 404, message = "Project or user not found"),
//            @ApiResponse(code = 500, message = "Internal server error")
//    })
//    public ResponseEntity<Project> addUserToProject(@PathVariable Long projectId, @PathVariable Long userId) {
//        try {
//            Project updatedProject = projectService.addUserToProject(projectId, userId);
//            return ResponseEntity.status(HttpStatus.OK).body(updatedProject);
//        } catch (ProjectNotFoundException | IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
}
