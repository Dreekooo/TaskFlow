package handlers

import (
	"database/sql"
	"encoding/json"
	"net/http"
)

type Project struct {
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
