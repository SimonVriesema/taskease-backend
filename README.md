# TaskEase Backend

The TaskEase Web App is a comprehensive and feature-rich task management platform designed to boost productivity and streamline daily workflows. It provides users with a range of tools and features to effectively manage tasks, collaborate seamlessly, and stay organized. This repository serves as the backend component of the TaskEase project. 

## API Details

This backend API is built using Java and the Spring Boot framework. It provides RESTful endpoints for managing tasks, projects, and users. The API is secured using JWT authentication and supports CRUD operations for tasks, projects, and users. The API also includes features such as user registration, login, and password reset. The API is designed to be scalable, secure, and easy to use.

## Technologies Used

- Java
- Spring Boot
- Maven
- JWT for authentication
- PostgreSQL

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher

### Installation
### Running the Application

1. Clone the repository:
    ```sh
    git clone https://github.com/SimonVriesema/taskease-backend.git
    cd taskease-backend
    ```

2. Build the project:
    ```sh
    mvn clean package
    ```

3. Run the application:
    ```sh
    mvn spring-boot:run
    ```

### ProjectController Endpoints

#### Get all projects
- **Endpoint:** `GET /projects`
- **Description:** Retrieve a list of all projects
- **Responses:**
    - `200`: Successfully retrieved the projects
    - `404`: No projects found
    - `500`: Internal server error

#### Get project by ID
- **Endpoint:** `GET /projects/{id}`
- **Description:** Retrieve a project by its ID
- **Responses:**
    - `200`: Successfully retrieved the project
    - `404`: Project not found
    - `500`: Internal server error

#### Create a new project
- **Endpoint:** `POST /projects`
- **Description:** Create a new project
- **Request Body Example:**
  ```json
  {
    "title": "New Project",
    "description": "Project description",
    "projectLeaderId": 1,
    "status": "ACTIVE"
  }
  ```
- **Responses:**
    - `201`: Successfully created the project
    - `400`: Invalid input data
    - `500`: Internal server error

#### Delete project by ID
- **Endpoint:** `DELETE /projects/{id}`
- **Description:** Delete project by providing its ID
- **Responses:**
    - `204`: Successfully deleted the project
    - `404`: The project you were trying to delete is not found
    - `500`: Internal server error

#### Update project by ID
- **Endpoint:** `PUT /projects/{id}`
- **Description:** Update project by providing its ID
- **Request Body Example:**
  ```json
  {
    "title": "Updated Project",
    "description": "Updated description",
    "status": "COMPLETED"
  }
  ```
- **Responses:**
    - `204`: Successfully updated the project
    - `404`: The project you were trying to update is not found
    - `500`: Internal server error

#### Get projects by user ID
- **Endpoint:** `GET /projects/user/{userId}`
- **Description:** Retrieve projects where the user is either in the users list or is the project leader
- **Responses:**
    - `200`: Successfully retrieved the projects
    - `404`: Projects not found
    - `500`: Internal server error

#### Add an existing user to a project
- **Endpoint:** `POST /projects/{projectId}/addUser/{userId}`
- **Description:** Add an existing user to a project
- **Responses:**
    - `200`: Successfully added the user to the project
    - `404`: Project or user not found
    - `409`: User is already in the project
    - `500`: Internal server error

#### Get the status of a project
- **Endpoint:** `GET /projects/{projectId}/status`
- **Description:** Get the status of a project
- **Responses:**
    - `200`: Successfully retrieved the project status
    - `404`: Project not found
    - `500`: Internal server error

#### Remove a user from a project
- **Endpoint:** `DELETE /projects/{projectId}/{userId}`
- **Description:** Remove a user from a project
- **Responses:**
    - `200`: Successfully removed the user from the project
    - `404`: Project or user not found
    - `500`: Internal server error

#### Get all users from a project
- **Endpoint:** `GET /projects/{projectId}/users`
- **Description:** Get all users from a project
- **Responses:**
    - `200`: Successfully retrieved the users
    - `404`: Project not found
    - `500`: Internal server error

### TaskController Endpoints

#### Create a task
- **Endpoint:** `POST /tasks`
- **Description:** Create a task and store in the database
- **Request Body Example:**
  ```json
  {
    "title": "New Task",
    "description": "Task description",
    "status": "COMPLETED"
  }
  ```
- **Responses:**
    - `201`: Successfully created the task
    - `400`: Invalid input data
    - `500`: Internal server error

#### Create a task for an existing project
- **Endpoint:** `POST /tasks/project/{projectId}`
- **Description:** Create a task for an existing project and store in the database
- **Request Body Example:**
  ```json
  {
    "title": "New Task",
    "description": "Task description",
    "status": "PENDING"
  }
  ```
- **Responses:**
    - `201`: Successfully created the task
    - `400`: Invalid input data
    - `404`: User or project not found
    - `500`: Internal server error

#### Delete task by ID
- **Endpoint:** `DELETE /tasks/{id}`
- **Description:** Delete task by providing its ID
- **Responses:**
    - `204`: Successfully deleted the task
    - `404`: The task you were trying to delete is not found

#### Update task by ID
- **Endpoint:** `PUT /tasks/{id}`
- **Description:** Update task by providing its ID and new data
- **Request Body Example:**
  ```json
  {
    "title": "Updated Task",
    "description": "Updated description",
    "status": "COMPLETED"
  }
  ```
- **Responses:**
    - `200`: Successfully updated the task
    - `404`: The task you were trying to update is not found
    - `400`: Invalid input data
    - `500`: Internal server error

#### Get tasks by user ID
- **Endpoint:** `GET /tasks/user/{id}`
- **Description:** Retrieve tasks assigned to a user by providing their ID
- **Responses:**
    - `200`: Successfully retrieved the tasks
    - `404`: The user you were trying to reach is not found
    - `500`: Internal server error

#### Get tasks from a project by project ID
- **Endpoint:** `GET /tasks/project/{id}`
- **Description:** Retrieve tasks associated with a project by providing the project ID
- **Responses:**
    - `200`: Successfully retrieved the tasks
    - `404`: Project not found
    - `500`: Internal server error

### UserController Endpoints

#### Sign in user
- **Endpoint:** `POST /users/login`
- **Description:** Sign in user by providing their email and password
- **Request Body Example:**
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **Responses:**
    - `200`: Successfully signed in the user
    - `404`: The user you were trying to sign in is not found
    - `500`: Internal server error

#### Create a user
- **Endpoint:** `POST /users`
- **Description:** Create a user and store in the database
- **Request Body Example:**
  ```json
  {
    "username": "newuser",
    "email": "newuser@example.com",
    "password": "password123"
  }
  ```
- **Responses:**
    - `201`: Successfully created the user
    - `500`: Internal server error

#### Get all users
- **Endpoint:** `GET /users`
- **Description:** Retrieve a list of all users
- **Responses:**
    - `200`: Successfully retrieved the users
    - `500`: Internal server error

#### Find user by ID
- **Endpoint:** `GET /users/{id}`
- **Description:** Retrieve user details by providing their ID
- **Responses:**
    - `200`: Successfully retrieved the user
    - `404`: The user you were trying to reach is not found
    - `500`: Internal server error

#### Delete user by ID
- **Endpoint:** `DELETE /users/{id}`
- **Description:** Delete user by providing their ID
- **Responses:**
    - `204`: Successfully deleted the user
    - `404`: The user you were trying to delete is not found
    - `500`: Internal server error

#### Find user by email
- **Endpoint:** `GET /users/email/{email}`
- **Description:** Retrieve user details by providing their email
- **Responses:**
    - `200`: Successfully retrieved the user
    - `404`: The user you were trying to reach is not found
    - `500`: Internal server error

#### Find user by username
- **Endpoint:** `GET /users/username/{username}`
- **Description:** Retrieve user details by providing their username
- **Responses:**
    - `200`: Successfully retrieved the user
    - `404`: The user you were trying to reach is not found
    - `500`: Internal server error

#### Update user by ID
- **Endpoint:** `PUT /users/{id}`
- **Description:** Update user details by providing their ID
- **Request Body Example:**
  ```json
  {
    "username": "updateduser",
    "email": "updateduser@example.com",
    "password": "newpassword123"
  }
  ```
- **Responses:**
    - `200`: Successfully updated the user
    - `404`: The user you were trying to update is not found
    - `500`: Internal server error