package handlers

import (
	"database/sql"
	"encoding/json"
	"net/http"
)

type Project struct {
	ProjectID   int    `json:"project_id"`
	Name        string `json:"name"`
	Description string `json:"description"`
	CreatedBy   int    `json:"created_by"`
}

func CreateProjectHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var project Project
		err := json.NewDecoder(r.Body).Decode(&project)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		_, err = db.Exec(
			"INSERT INTO Projects (name, description, created_by) VALUES (?, ?, ?)",
			project.Name, project.Description, project.CreatedBy,
		)
		if err != nil {
			http.Error(w, "Failed to create project", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusCreated)
		json.NewEncoder(w).Encode(map[string]string{"message": "Project created successfully"})
	}
}

func GetProjectsHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Zapytanie do bazy danych
		rows, err := db.Query("SELECT project_id, name, description, created_by FROM Projects")
		if err != nil {
			http.Error(w, "Failed to fetch projects", http.StatusInternalServerError)
			return
		}
		defer rows.Close()

		// Przetwarzanie wyników
		var projects []Project
		for rows.Next() {
			var project Project
			if err := rows.Scan(&project.ProjectID, &project.Name, &project.Description, &project.CreatedBy); err != nil {
				http.Error(w, "Failed to scan project", http.StatusInternalServerError)
				return
			}
			projects = append(projects, project)
		}

		// Wysyłanie odpowiedzi
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(projects)
	}
}
