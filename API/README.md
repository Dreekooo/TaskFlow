
# TaskFlow API

TaskFlow API is a project management backend system designed to handle user accounts, projects, tasks, and comments efficiently. This documentation will guide you through setting up, running, and testing the API, as well as using the provided test database for quick exploration.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Folder Structure](#folder-structure)
3. [Prerequisites](#prerequisites)
4. [Installation](#installation)
5. [Building the API](#building-the-api)
6. [Running the API](#running-the-api)
7. [Test Database](#test-database)
8. [API Endpoints](#api-endpoints)
9. [Database Schema](#database-schema)
10. [Testing the API](#testing-the-api)
11. [Authorization with JWT](#authorization-with-jwt)

---

## Project Overview

TaskFlow API allows you to:
- Manage users, projects, tasks, and comments.
- Assign roles and priorities to users and tasks.
- Track project progress with task statuses.
- Quickly set up and explore the API using a provided test database.

---

## Folder Structure

```plaintext
API/
├── database-schema/
│   ├── drawSQL-image-ex/         # ERD diagram examples
│   ├── drawSQL-mysql-ex/         # SQL scripts or schemas
├── handlers/                     # Handlers for various endpoints
│   ├── priority.go
│   ├── project.go
│   ├── project_users.go
│   ├── roles.go
│   ├── status.go
│   ├── task.go
│   ├── task_comments.go
│   ├── user.go
├── test-files/
│   ├── taskflow.db               # Pre-loaded test SQLite database
│   ├── taskflow.exe              # Pre-loaded test executable file to run the server
├── middleware/                   # Middleware (e.g., JWT authentication)
│   ├── auth.go                   # JWT middleware
├── utils/                        # Utility files
│   ├── db.go                     # Database connection and setup
│   ├── hash_password.go          # Password hashing utility
│   ├── jwt.go                    # JWT generation and validation
├── go.mod                        # Go module dependencies
├── main.go                       # Application entry point
├── routes.go                     # Routing configuration
├── schema.sql                    # SQL schema for database initialization
├── README.md                     # Documentation (this file)
```

---

## Prerequisites

Before using this API, ensure the following:

1. **Go (Golang 1.23.3):**
    - Download from [https://go.dev/dl/](https://go.dev/dl/).
    - Verify installation:
      ```bash
      go version
      ```

2. **SQLite:**
    - Install SQLite from [https://sqlite.org/download.html](https://sqlite.org/download.html).

3. **Postman (or similar API testing tool):**
    - Download Postman from [https://www.postman.com/downloads/](https://www.postman.com/downloads/).

---

## Installation

1. Clone the repository:
   ```bash
   git clone -b API --single-branch https://github.com/Dreekooo/TaskFlow.git
   cd API
   ```

2. Install dependencies:
   ```bash
   go mod tidy
   ```

3. Set up the database:
    - Option 1: Use the provided **test database** (`test-files/taskflow.db`).
    - Option 2: Initialize a new database:
      ```bash
      go run main.go
      ```

---

## Building the API

To build the API into an executable:
```bash
go build -x
```

---

## Running the API

Run the API server:
```bash
./taskflow-api
```

By default, the server runs on `http://localhost:8080`.

---

## Test Database

A pre-filled SQLite test database is included in the repository: **`test-files/taskflow.db`**. Use it for quick testing without initializing new data.

### **To use the test database:**
1. Copy the `taskflow.db` file into the project root or the desired directory.
2. Ensure the `db.go` file points to this database:
   ```go
   db, err := sql.Open("sqlite3", "./test-files/taskflow.db")
   ```
3. Start the server

---

## API Endpoints

### **Users**
- `POST /register/` - Register a new user
- `POST /login/` - Authenticate and get a JWT token
- `POST /users/` - Create a user
- `GET /users/` - Get all users
- `GET /users/{id}/` - Get user by ID
- `PUT /users/{id}/` - Update user
- `DELETE /users/{id}/` - Delete user
- `GET /users/` - Get all users (requires JWT)

### **Projects**
- `POST /projects/` - Create a project
- `GET /projects/` - Get all projects
- `GET /projects/{id}/` - Get project by ID
- `PUT /projects/{id}/` - Update project
- `DELETE /projects/{id}/` - Delete project

### **Tasks**
- `POST /tasks/` - Create a task
- `GET /tasks/` - Get all tasks
- `GET /tasks/{id}/` - Get task by ID
- `PUT /tasks/{id}/` - Update task
- `DELETE /tasks/{id}/` - Delete task

### **Task Comments**
- `POST /task-comments/` - Create a comment
- `GET /task-comments/` - Get all comments
- `GET /task-comments/{id}/` - Get comment by ID
- `PUT /task-comments/{id}/` - Update comment
- `DELETE /task-comments/{id}/` - Delete comment

### **Project Users**
- `POST /project-users/` - Add a user to a project
- `GET /project-users/` - Get all project users
- `GET /project-users/{id}/` - Get project user by ID
- `PUT /project-users/{id}/` - Update project user
- `DELETE /project-users/{id}/` - Delete project user

---

### **Roles**
- `POST /roles/` - Create a new role
- `GET /roles/` - Get all roles
- `GET /roles/{id}/` - Get role by ID
- `PUT /roles/{id}/` - Update role by ID
- `DELETE /roles/{id}/` - Delete role by ID

---

### **Priorities**
- `POST /priorities/` - Create a new priority
- `GET /priorities/` - Get all priorities
- `GET /priorities/{id}/` - Get priority by ID
- `PUT /priorities/{id}/` - Update priority by ID
- `DELETE /priorities/{id}/` - Delete priority by ID

---

### **Status**
- `POST /statuses/` - Create a new status
- `GET /statuses/` - Get all statuses
- `GET /statuses/{id}/` - Get status by ID
- `PUT /statuses/{id}/` - Update status by ID
- `DELETE /statuses/{id}/` - Delete status by ID

---

## Database Schema

The database schema (`schema.sql`) initializes the following tables:
- `Users`
- `Projects`
- `Tasks`
- `TaskComments`
- `ProjectUsers`
- `Priorities`
- `Status`
- `Roles`

---

## Testing the API

1. Use **Postman**:
    - Import JSON examples or use manual input to test endpoints.
    - Example: POST a new user:
      ```json
      {
          "email": "example@example.com",
          "username": "exampleuser",
          "first_name": "Example",
          "last_name": "User",
          "password_hash": "examplepassword"
      }
      ```

2. Use **cURL**:
   Example command:
   ```bash
   curl -X POST http://localhost:8080/users/    -H "Content-Type: application/json"    -d '{"email":"example@example.com","username":"exampleuser","first_name":"Example","last_name":"User","password_hash":"examplepassword"}'
   ```
   
---

## Authorization with JWT

This API uses **JWT** (JSON Web Token) for authentication and authorization.

1. **Login to obtain a token**:
   - Endpoint: `POST /login`
   - Response:
     ```json
     {
         "message": "Login successful",
         "token": "<your-jwt-token>"
     }
     ```

2. **Send the token in the `Authorization` header**:
   ```http
   Authorization: Bearer <your-jwt-token>
   ```

3. **Example of a protected endpoint**:
   - Endpoint: `DELETE /api/users/delete/`
   - Description: Deletes the authenticated user's account.