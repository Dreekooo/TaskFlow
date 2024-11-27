CREATE TABLE IF NOT EXISTS Users (
                       user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       first_name VARCHAR(50),
                       last_name VARCHAR(50),
                       password_hash VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Projects (
                          project_id INTEGER PRIMARY KEY AUTOINCREMENT,
                          name VARCHAR(100),
                          description TEXT,
                          created_by INTEGER,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (created_by) REFERENCES Users(user_id)
);

CREATE TABLE IF NOT EXISTS ProjectUsers (
                              project_user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                              project_id INTEGER,
                              user_id INTEGER,
                              role INTEGER,
                              FOREIGN KEY (project_id) REFERENCES Projects(project_id),
                              FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE IF NOT EXISTS Roles (
                       role_id INTEGER PRIMARY KEY AUTOINCREMENT,
                       role_name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS Tasks (
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

CREATE TABLE IF NOT EXISTS Status (
                        status_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        status_name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS Priorities (
                            priority_id INTEGER PRIMARY KEY AUTOINCREMENT,
                            priority_name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS TaskComments (
                              comment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                              task_id INTEGER,
                              user_id INTEGER,
                              content TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (task_id) REFERENCES Tasks(task_id),
                              FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Usunięcie istniejących danych
DELETE FROM Priorities;

-- Wstawienie wartości
INSERT INTO Priorities (priority_id, priority_name) VALUES
                                                        (1, 'Low'),
                                                        (2, 'Medium'),
                                                        (3, 'High'),
                                                        (4, 'Critical');

-- Usunięcie istniejących danych z tabeli Status
DELETE FROM Status;

INSERT INTO Status (status_id, status_name) VALUES
                                                (1, 'Backlog'),
                                                (2, 'Selected'),
                                                (3, 'Running'),
                                                (4, 'In Review'),
                                                (5, 'Done');

-- Usunięcie istniejących danych z tabeli Roles
DELETE FROM Roles;

INSERT INTO Roles (role_id, role_name) VALUES
                                           (1, 'Admin'),
                                           (2, 'Manager'),
                                           (3, 'Developer'),
                                           (4, 'Tester');
