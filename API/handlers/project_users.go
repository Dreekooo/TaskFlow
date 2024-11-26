package handlers

import (
	"database/sql"
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
)

type ProjectUser struct {
	ProjectUserID int `json:"project_user_id"`
	ProjectID     int `json:"project_id"`
	UserID        int `json:"user_id"`
	Role          int `json:"role"`
}

// POST - Create a new ProjectUser
func CreateProjectUserHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var projectUser ProjectUser
		err := json.NewDecoder(r.Body).Decode(&projectUser)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		_, err = db.Exec("INSERT INTO ProjectUsers (project_id, user_id, role) VALUES (?, ?, ?)",
			projectUser.ProjectID, projectUser.UserID, projectUser.Role)
		if err != nil {
			http.Error(w, "Failed to create project user", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusCreated)
		json.NewEncoder(w).Encode(map[string]string{"message": "Project user created successfully"})
	}
}

// GET - Retrieve all ProjectUsers
func GetProjectUsersHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		rows, err := db.Query("SELECT project_user_id, project_id, user_id, role FROM ProjectUsers")
		if err != nil {
			http.Error(w, "Failed to fetch project users", http.StatusInternalServerError)
			return
		}
		defer rows.Close()

		var projectUsers []ProjectUser
		for rows.Next() {
			var projectUser ProjectUser
			err := rows.Scan(&projectUser.ProjectUserID, &projectUser.ProjectID, &projectUser.UserID, &projectUser.Role)
			if err != nil {
				http.Error(w, "Failed to scan project user", http.StatusInternalServerError)
				return
			}
			projectUsers = append(projectUsers, projectUser)
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(projectUsers)
	}
}

func GetProjectUserByIdHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Pobierz ID z URL
		vars := mux.Vars(r)
		id := vars["id"]

		projectUserID, err := strconv.Atoi(id)
		if err != nil {
			http.Error(w, "Invalid project user ID", http.StatusBadRequest)
			return
		}

		// Zapytanie do bazy danych
		var projectUser ProjectUser
		err = db.QueryRow("SELECT project_user_id, project_id, user_id, role FROM ProjectUsers WHERE project_user_id = ?",
			projectUserID).Scan(&projectUser.ProjectUserID, &projectUser.ProjectID, &projectUser.UserID, &projectUser.Role)

		if err != nil {
			if err == sql.ErrNoRows {
				http.Error(w, "Project user not found", http.StatusNotFound)
				return
			}
			http.Error(w, "Failed to fetch project user", http.StatusInternalServerError)
			return
		}

		// Zwróć dane w odpowiedzi
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(projectUser)
	}
}

// PUT - Update a ProjectUser
func UpdateProjectUserHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id := vars["id"]

		projectUserID, err := strconv.Atoi(id)
		if err != nil {
			http.Error(w, "Invalid project user ID", http.StatusBadRequest)
			return
		}

		var projectUser ProjectUser
		err = json.NewDecoder(r.Body).Decode(&projectUser)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		_, err = db.Exec("UPDATE ProjectUsers SET project_id = ?, user_id = ?, role = ? WHERE project_user_id = ?",
			projectUser.ProjectID, projectUser.UserID, projectUser.Role, projectUserID)
		if err != nil {
			http.Error(w, "Failed to update project user", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{"message": "Project user updated successfully"})
	}
}

// DELETE - Remove a ProjectUser
func DeleteProjectUserHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id := vars["id"]

		projectUserID, err := strconv.Atoi(id)
		if err != nil {
			http.Error(w, "Invalid project user ID", http.StatusBadRequest)
			return
		}

		_, err = db.Exec("DELETE FROM ProjectUsers WHERE project_user_id = ?", projectUserID)
		if err != nil {
			http.Error(w, "Failed to delete project user", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{"message": "Project user deleted successfully"})
	}
}
