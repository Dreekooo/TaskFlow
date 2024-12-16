package handlers

import (
	"database/sql"
	"encoding/json"
	"github.com/gorilla/mux"
	"net/http"
	"strconv"
)

type Priority struct {
	PriorityID   int    `json:"priority_id"`
	PriorityName string `json:"priority_name"`
}

func CreatePriorityHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var priority Priority
		err := json.NewDecoder(r.Body).Decode(&priority)
		if err != nil || priority.PriorityName == "" {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		_, err = db.Exec("INSERT INTO Priorities (priority_name) VALUES (?)", priority.PriorityName)
		if err != nil {
			http.Error(w, "Failed to create priority", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusCreated)
		json.NewEncoder(w).Encode(map[string]string{
			"message": "Priority created successfully",
		})
	}
}

func GetPrioritiesHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		rows, err := db.Query("SELECT priority_id, priority_name FROM Priorities")
		if err != nil {
			http.Error(w, "Failed to fetch priorities", http.StatusInternalServerError)
			return
		}
		defer rows.Close()

		var priorities []Priority
		for rows.Next() {
			var priority Priority
			if err := rows.Scan(&priority.PriorityID, &priority.PriorityName); err != nil {
				http.Error(w, "Failed to scan priority", http.StatusInternalServerError)
				return
			}
			priorities = append(priorities, priority)
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(priorities)
	}
}

func GetPriorityByIDHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id, err := strconv.Atoi(vars["id"])
		if err != nil {
			http.Error(w, "Invalid priority ID", http.StatusBadRequest)
			return
		}

		var priority Priority
		err = db.QueryRow("SELECT priority_id, priority_name FROM Priorities WHERE priority_id = ?", id).Scan(&priority.PriorityID, &priority.PriorityName)
		if err != nil {
			if err == sql.ErrNoRows {
				http.Error(w, "Priority not found", http.StatusNotFound)
				return
			}
			http.Error(w, "Failed to fetch priority", http.StatusInternalServerError)
			return
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(priority)
	}
}

func UpdatePriorityHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id, err := strconv.Atoi(vars["id"])
		if err != nil {
			http.Error(w, "Invalid priority ID", http.StatusBadRequest)
			return
		}

		var priority Priority
		err = json.NewDecoder(r.Body).Decode(&priority)
		if err != nil || priority.PriorityName == "" {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		result, err := db.Exec("UPDATE Priorities SET priority_name = ? WHERE priority_id = ?", priority.PriorityName, id)
		if err != nil {
			http.Error(w, "Failed to update priority", http.StatusInternalServerError)
			return
		}

		rowsAffected, _ := result.RowsAffected()
		if rowsAffected == 0 {
			http.Error(w, "Priority not found", http.StatusNotFound)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{
			"message": "Priority updated successfully",
		})
	}
}

func DeletePriorityHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id, err := strconv.Atoi(vars["id"])
		if err != nil {
			http.Error(w, "Invalid priority ID", http.StatusBadRequest)
			return
		}

		result, err := db.Exec("DELETE FROM Priorities WHERE priority_id = ?", id)
		if err != nil {
			http.Error(w, "Failed to delete priority", http.StatusInternalServerError)
			return
		}

		rowsAffected, _ := result.RowsAffected()
		if rowsAffected == 0 {
			http.Error(w, "Priority not found", http.StatusNotFound)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{
			"message": "Priority deleted successfully",
		})
	}
}
