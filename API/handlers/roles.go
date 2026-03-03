package handlers

import (
	"database/sql"
	"encoding/json"
	"github.com/gorilla/mux"
	"net/http"
	"strconv"
)

type Role struct {
	RoleID   int    `json:"role_id"`
	RoleName string `json:"role_name"`
}

func CreateRoleHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var role Role
		err := json.NewDecoder(r.Body).Decode(&role)
		if err != nil || role.RoleName == "" {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		_, err = db.Exec("INSERT INTO Roles (role_name) VALUES (?)", role.RoleName)
		if err != nil {
			http.Error(w, "Failed to create role", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusCreated)
		json.NewEncoder(w).Encode(map[string]string{
			"message": "Role created successfully",
		})
	}
}

func GetRolesHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		rows, err := db.Query("SELECT role_id, role_name FROM Roles")
		if err != nil {
			http.Error(w, "Failed to fetch roles", http.StatusInternalServerError)
			return
		}
		defer rows.Close()

		var roles []Role
		for rows.Next() {
			var role Role
			if err := rows.Scan(&role.RoleID, &role.RoleName); err != nil {
				http.Error(w, "Failed to scan role", http.StatusInternalServerError)
				return
			}
			roles = append(roles, role)
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(roles)
	}
}

func GetRoleByIDHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id, err := strconv.Atoi(vars["id"])
		if err != nil {
			http.Error(w, "Invalid role ID", http.StatusBadRequest)
			return
		}

		var role Role
		err = db.QueryRow("SELECT role_id, role_name FROM Roles WHERE role_id = ?", id).Scan(&role.RoleID, &role.RoleName)
		if err != nil {
			if err == sql.ErrNoRows {
				http.Error(w, "Role not found", http.StatusNotFound)
				return
			}
			http.Error(w, "Failed to fetch role", http.StatusInternalServerError)
			return
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(role)
	}
}

func UpdateRoleHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id, err := strconv.Atoi(vars["id"])
		if err != nil {
			http.Error(w, "Invalid role ID", http.StatusBadRequest)
			return
		}

		var role Role
		err = json.NewDecoder(r.Body).Decode(&role)
		if err != nil || role.RoleName == "" {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		result, err := db.Exec("UPDATE Roles SET role_name = ? WHERE role_id = ?", role.RoleName, id)
		if err != nil {
			http.Error(w, "Failed to update role", http.StatusInternalServerError)
			return
		}

		rowsAffected, _ := result.RowsAffected()
		if rowsAffected == 0 {
			http.Error(w, "Role not found", http.StatusNotFound)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{
			"message": "Role updated successfully",
		})
	}
}

func DeleteRoleHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id, err := strconv.Atoi(vars["id"])
		if err != nil {
			http.Error(w, "Invalid role ID", http.StatusBadRequest)
			return
		}

		result, err := db.Exec("DELETE FROM Roles WHERE role_id = ?", id)
		if err != nil {
			http.Error(w, "Failed to delete role", http.StatusInternalServerError)
			return
		}

		rowsAffected, _ := result.RowsAffected()
		if rowsAffected == 0 {
			http.Error(w, "Role not found", http.StatusNotFound)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{
			"message": "Role deleted successfully",
		})
	}
}
