package main

import (
	"database/sql"
	"github.com/gorilla/mux"
	"taskflow/handlers" // Poprawiona ścieżka importu
)

func SetupRoutes(db *sql.DB) *mux.Router {
	r := mux.NewRouter()

	// Konfiguracja tras
	r.HandleFunc("/users/", handlers.CreateUserHandler(db)).Methods("POST")                // Dodawanie nowych User
	r.HandleFunc("/projects/", handlers.CreateProjectHandler(db)).Methods("POST")          // Dodawanie nowych Project
	r.HandleFunc("/tasks/", handlers.CreateTaskHandler(db)).Methods("POST")                // Dodawanie nowych Task
	r.HandleFunc("/project-users/", handlers.CreateProjectUserHandler(db)).Methods("POST") // Dodawanie nowych ProjectUser
	r.HandleFunc("/task-comments/", handlers.CreateTaskCommentHandler(db)).Methods("POST") // Dodawanie nowych TaskComment

	r.HandleFunc("/users/", handlers.GetUsersHandler(db)).Methods("GET")                // Pobieranie wszystkich Users
	r.HandleFunc("/projects/", handlers.GetProjectsHandler(db)).Methods("GET")          // Pobieranie wszystkich Projects
	r.HandleFunc("/tasks/", handlers.GetTasksHandler(db)).Methods("GET")                // Pobieranie wszystkich Tasks
	r.HandleFunc("/project-users/", handlers.GetProjectUsersHandler(db)).Methods("GET") // Pobieranie wszystkich ProjectUsers
	r.HandleFunc("/task-comments/", handlers.GetTaskCommentsHandler(db)).Methods("GET") // Pobieranie wszystkich TaskComments

	r.HandleFunc("/users/{id:[0-9]+}/", handlers.GetUserByIdHandler(db)).Methods("GET")                // Pobieranie pojedynczego User
	r.HandleFunc("/projects/{id:[0-9]+}/", handlers.GetProjectByIdHandler(db)).Methods("GET")          // Pobieranie pojedynczego Project
	r.HandleFunc("/tasks/{id:[0-9]+}/", handlers.GetTaskByIdHandler(db)).Methods("GET")                // Pobieranie pojedynczego Task
	r.HandleFunc("/project-users/{id:[0-9]+}/", handlers.GetProjectUserByIdHandler(db)).Methods("GET") // Pobieranie pojedynczego ProjectUser
	r.HandleFunc("/task-comments/{id:[0-9]+}/", handlers.GetTaskCommentByIdHandler(db)).Methods("GET") // Pobieranie pojedynczego TaskComment

	r.HandleFunc("/register/", handlers.RegisterUserHandler(db)).Methods("POST") // Rejestracja
	r.HandleFunc("/login/", handlers.LoginUserHandler(db)).Methods("POST")       // Logowanie

	r.HandleFunc("/users/{id:[0-9]+}/", handlers.UpdateUserHandler(db)).Methods("PUT")    // Aktualizacja User
	r.HandleFunc("/users/{id:[0-9]+}/", handlers.DeleteUserHandler(db)).Methods("DELETE") // Usuwanie User

	r.HandleFunc("/projects/{id:[0-9]+}/", handlers.UpdateProjectHandler(db)).Methods("PUT")    // Aktualizacja Project
	r.HandleFunc("/projects/{id:[0-9]+}/", handlers.DeleteProjectHandler(db)).Methods("DELETE") // Usuwanie Project

	r.HandleFunc("/tasks/{id:[0-9]+}/", handlers.UpdateTaskHandler(db)).Methods("PUT")    // Aktualizacja Task
	r.HandleFunc("/tasks/{id:[0-9]+}/", handlers.DeleteTaskHandler(db)).Methods("DELETE") // Usuwanie Task

	r.HandleFunc("/project-users/{id:[0-9]+}", handlers.UpdateProjectUserHandler(db)).Methods("PUT")    // Aktualizacja ProjectUser
	r.HandleFunc("/project-users/{id:[0-9]+}", handlers.DeleteProjectUserHandler(db)).Methods("DELETE") // Usuwanie ProjectUser

	r.HandleFunc("/task-comments/{id:[0-9]+}", handlers.UpdateTaskCommentHandler(db)).Methods("PUT")    // Aktualizacja TaskComment
	r.HandleFunc("/task-comments/{id:[0-9]+}", handlers.DeleteTaskCommentHandler(db)).Methods("DELETE") // Usuwanie TaskComment

	return r
}
