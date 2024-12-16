package handlers

import (
	"database/sql"
	"encoding/json"
	"github.com/gorilla/mux"
	"net/http"
	"strconv"
)

type Status struct {
	StatusID   int    `json:"status_id"`
	StatusName string `json:"status_name"`
}

func CreateStatusHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var status Status
		err := json.NewDecoder(r.Body).Decode(&status)
		if err != nil || status.StatusName == "" {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		_, err = db.Exec("INSERT INTO Status (status_name) VALUES (?)", status.StatusName)
		if err != nil {
			http.Error(w, "Failed to create status", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusCreated)
		json.NewEncoder(w).Encode(map[string]string{
			"message": "Status created successfully",
		})
	}
}

func GetStatusesHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		rows, err := db.Query("SELECT status_id, status_name FROM Status")
		if err != nil {
			http.Error(w, "Failed to fetch statuses", http.StatusInternalServerError)
			return
		}
		defer rows.Close()

		var statuses []Status
		for rows.Next() {
			var status Status
			if err := rows.Scan(&status.StatusID, &status.StatusName); err != nil {
				http.Error(w, "Failed to scan status", http.StatusInternalServerError)
				return
			}
			statuses = append(statuses, status)
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(statuses)
	}
}

func GetStatusByIDHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id, err := strconv.Atoi(vars["id"])
		if err != nil {
			http.Error(w, "Invalid status ID", http.StatusBadRequest)
			return
		}

		var status Status
		err = db.QueryRow("SELECT status_id, status_name FROM Status WHERE status_id = ?", id).Scan(&status.StatusID, &status.StatusName)
		if err != nil {
			if err == sql.ErrNoRows {
				http.Error(w, "Status not found", http.StatusNotFound)
				return
			}
			http.Error(w, "Failed to fetch status", http.StatusInternalServerError)
			return
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(status)
	}
}

func UpdateStatusHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id, err := strconv.Atoi(vars["id"])
		if err != nil {
			http.Error(w, "Invalid status ID", http.StatusBadRequest)
			return
		}

		var status Status
		err = json.NewDecoder(r.Body).Decode(&status)
		if err != nil || status.StatusName == "" {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		result, err := db.Exec("UPDATE Status SET status_name = ? WHERE status_id = ?", status.StatusName, id)
		if err != nil {
			http.Error(w, "Failed to update status", http.StatusInternalServerError)
			return
		}

		rowsAffected, _ := result.RowsAffected()
		if rowsAffected == 0 {
			http.Error(w, "Status not found", http.StatusNotFound)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{
			"message": "Status updated successfully",
		})
	}
}
func DeleteStatusHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id, err := strconv.Atoi(vars["id"])
		if err != nil {
			http.Error(w, "Invalid status ID", http.StatusBadRequest)
			return
		}

		result, err := db.Exec("DELETE FROM Status WHERE status_id = ?", id)
		if err != nil {
			http.Error(w, "Failed to delete status", http.StatusInternalServerError)
			return
		}

		rowsAffected, _ := result.RowsAffected()
		if rowsAffected == 0 {
			http.Error(w, "Status not found", http.StatusNotFound)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{
			"message": "Status deleted successfully",
		})
	}
}
