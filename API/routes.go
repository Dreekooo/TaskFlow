package main

import (
	"database/sql"
	"github.com/gorilla/mux"
	"taskflow/handlers" // Poprawiona ścieżka importu
)

func SetupRoutes(db *sql.DB) *mux.Router {
	r := mux.NewRouter()

	// Konfiguracja tras
	r.HandleFunc("/users", handlers.CreateUserHandler(db)).Methods("POST")
	r.HandleFunc("/projects", handlers.CreateProjectHandler(db)).Methods("POST")
	r.HandleFunc("/tasks", handlers.CreateTaskHandler(db)).Methods("POST")

	r.HandleFunc("/users", handlers.GetUsersHandler(db)).Methods("GET")
	r.HandleFunc("/projects", handlers.GetProjectsHandler(db)).Methods("GET")
	r.HandleFunc("/tasks", handlers.GetTasksHandler(db)).Methods("GET")

	return r
}
