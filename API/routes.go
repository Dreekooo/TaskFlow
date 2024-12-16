package main

import (
	"database/sql"
	"github.com/gorilla/mux"
	"taskflow/handlers" // Poprawiona ścieżka importu
	"taskflow/middleware"
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

	r.HandleFunc("/project-users/{id:[0-9]+}/", handlers.UpdateProjectUserHandler(db)).Methods("PUT")    // Aktualizacja ProjectUser
	r.HandleFunc("/project-users/{id:[0-9]+}/", handlers.DeleteProjectUserHandler(db)).Methods("DELETE") // Usuwanie ProjectUser

	r.HandleFunc("/task-comments/{id:[0-9]+}/", handlers.UpdateTaskCommentHandler(db)).Methods("PUT")    // Aktualizacja TaskComment
	r.HandleFunc("/task-comments/{id:[0-9]+}/", handlers.DeleteTaskCommentHandler(db)).Methods("DELETE") // Usuwanie TaskComment

	// Role routes
	r.HandleFunc("/roles/", handlers.CreateRoleHandler(db)).Methods("POST")               // Add a new role
	r.HandleFunc("/roles/", handlers.GetRolesHandler(db)).Methods("GET")                  // Get all roles
	r.HandleFunc("/roles/{id:[0-9]+}/", handlers.GetRoleByIDHandler(db)).Methods("GET")   // Get role by ID
	r.HandleFunc("/roles/{id:[0-9]+}/", handlers.UpdateRoleHandler(db)).Methods("PUT")    // Update role by ID
	r.HandleFunc("/roles/{id:[0-9]+}/", handlers.DeleteRoleHandler(db)).Methods("DELETE") // Delete role by ID

	// Priority routes
	r.HandleFunc("/priorities/", handlers.CreatePriorityHandler(db)).Methods("POST")               // Add a new priority
	r.HandleFunc("/priorities/", handlers.GetPrioritiesHandler(db)).Methods("GET")                 // Get all priorities
	r.HandleFunc("/priorities/{id:[0-9]+}/", handlers.GetPriorityByIDHandler(db)).Methods("GET")   // Get priority by ID
	r.HandleFunc("/priorities/{id:[0-9]+}/", handlers.UpdatePriorityHandler(db)).Methods("PUT")    // Update priority by ID
	r.HandleFunc("/priorities/{id:[0-9]+}/", handlers.DeletePriorityHandler(db)).Methods("DELETE") // Delete priority by ID

	// Status routes
	r.HandleFunc("/statuses/", handlers.CreateStatusHandler(db)).Methods("POST")               // Add a new status
	r.HandleFunc("/statuses/", handlers.GetStatusesHandler(db)).Methods("GET")                 // Get all statuses
	r.HandleFunc("/statuses/{id:[0-9]+}/", handlers.GetStatusByIDHandler(db)).Methods("GET")   // Get status by ID
	r.HandleFunc("/statuses/{id:[0-9]+}/", handlers.UpdateStatusHandler(db)).Methods("PUT")    // Update status by ID
	r.HandleFunc("/statuses/{id:[0-9]+}/", handlers.DeleteStatusHandler(db)).Methods("DELETE") // Delete status by ID

	// Protected routes
	protected := r.PathPrefix("/api").Subrouter()
	protected.Use(middleware.JWTMiddleware)

	protected.HandleFunc("/users/delete/", handlers.DeleteOwnAccountHandler(db)).Methods("DELETE") // Usuwanie własnego konta

	return r
}
