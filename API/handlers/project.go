package handlers

import (
	"database/sql"
	"encoding/json"
	"github.com/gorilla/mux"
	"net/http"
	"strconv"
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

func GetProjectByIdHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Pobranie parametru {id} z adresu URL
		vars := mux.Vars(r)
		id := vars["id"]

		ProjectID, err := strconv.Atoi(id)
		if err != nil {
			http.Error(w, "Invalid project ID", http.StatusBadRequest)
			return
		}

		var project Project
		err = db.QueryRow("SELECT project_id, name, description, created_by FROM Projects WHERE project_id = ?", ProjectID).
			Scan(&project.ProjectID, &project.Name, &project.Description, &project.CreatedBy)
		if err != nil {
			if err == sql.ErrNoRows {
				http.Error(w, "Project not found", http.StatusNotFound)
				return
			}
			http.Error(w, "Failed to fetch Project", http.StatusInternalServerError)
			return
		}

		// Wysyłanie odpowiedzi
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(project)
	}
}

func UpdateProjectHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Pobierz ID projektu z URL
		vars := mux.Vars(r)
		id := vars["id"]

		projectID, err := strconv.Atoi(id)
		if err != nil {
			http.Error(w, "Invalid project ID", http.StatusBadRequest)
			return
		}

		// Pobierz dane z żądania
		var project Project
		err = json.NewDecoder(r.Body).Decode(&project)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		// Aktualizuj projekt w bazie danych
		_, err = db.Exec(`
            UPDATE Projects
            SET name = ?, description = ?, created_by = ?
            WHERE project_id = ?
        `, project.Name, project.Description, project.CreatedBy, projectID)
		if err != nil {
			http.Error(w, "Failed to update project", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{"message": "Project updated successfully"})
	}
}

func DeleteProjectHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id := vars["id"]

		projectID, err := strconv.Atoi(id)
		if err != nil {
			http.Error(w, "Invalid project ID", http.StatusBadRequest)
			return
		}

		_, err = db.Exec("DELETE FROM Projects WHERE project_id = ?", projectID)
		if err != nil {
			http.Error(w, "Failed to delete project", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{"message": "Project deleted successfully"})
	}
}
