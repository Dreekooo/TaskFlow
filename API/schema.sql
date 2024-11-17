CREATE TABLE Users (
                       user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       first_name VARCHAR(50),
                       last_name VARCHAR(50),
                       password_hash VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Projects (
                          project_id INTEGER PRIMARY KEY AUTOINCREMENT,
                          name VARCHAR(100),
                          description TEXT,
                          created_by INTEGER,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (created_by) REFERENCES Users(user_id)
);

CREATE TABLE ProjectUsers (
                              project_user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                              project_id INTEGER,
                              user_id INTEGER,
                              role INTEGER,
                              FOREIGN KEY (project_id) REFERENCES Projects(project_id),
                              FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE Roles (
                       role_id INTEGER PRIMARY KEY AUTOINCREMENT,
                       role_name VARCHAR(50)
);

CREATE TABLE Tasks (
                       task_id INTEGER PRIMARY KEY AUTOINCREMENT,
                       project_id INTEGER,
                       assigned_to INTEGER,
                       title VARCHAR(100),
                       description TEXT,
                       status INTEGER,
                       priority INTEGER,
                       start_date DATE,
                       due_date DATE,
                       created_by INTEGER,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (project_id) REFERENCES Projects(project_id),
                       FOREIGN KEY (assigned_to) REFERENCES Users(user_id),
                       FOREIGN KEY (status) REFERENCES Status(status_id),
                       FOREIGN KEY (priority) REFERENCES Priorities(priority_id)
);

CREATE TABLE Status (
                        status_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        status_name VARCHAR(50)
);

CREATE TABLE Priorities (
                            priority_id INTEGER PRIMARY KEY AUTOINCREMENT,
                            priority_name VARCHAR(50)
);

CREATE TABLE TaskComments (
                              comment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                              task_id INTEGER,
                              user_id INTEGER,
                              content TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (task_id) REFERENCES Tasks(task_id),
                              FOREIGN KEY (user_id) REFERENCES Users(user_id)
);
