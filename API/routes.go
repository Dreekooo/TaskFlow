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

	return r
}
