package handlers

import (
	"database/sql"
	"encoding/json"
	"github.com/gorilla/mux"
	"net/http"
	"time"
)

type Task struct {
	TaskID      int       `json:"task_id"`
	ProjectID   int       `json:"project_id"`
	AssignedTo  int       `json:"assigned_to"`
	Title       string    `json:"title"`
	Description string    `json:"description"`
	Status      int       `json:"status"`     // ID statusu
	Priority    int       `json:"priority"`   // ID priorytetu
	StartDate   time.Time `json:"start_date"` // Typ time.Time
	DueDate     time.Time `json:"due_date"`   // Typ time.Time
	CreatedBy   int       `json:"created_by"`
}

func CreateTaskHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var task Task
		err := json.NewDecoder(r.Body).Decode(&task)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		// Zapytanie SQL do wstawienia zadania
		query := `
			INSERT INTO Tasks (project_id, assigned_to, title, description, status, priority, start_date, due_date, created_by)
			VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`
		_, err = db.Exec(query,
			task.ProjectID,
			task.AssignedTo,
			task.Title,
			task.Description,
			task.Status,
			task.Priority,
			task.StartDate.Format("2006-01-02"), // Konwersja na format YYYY-MM-DD
			task.DueDate.Format("2006-01-02"),   // Konwersja na format YYYY-MM-DD
			task.CreatedBy,
		)
		if err != nil {
			http.Error(w, "Failed to create task", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusCreated)
		json.NewEncoder(w).Encode(map[string]string{
			"message": "Task created successfully",
		})
	}
}

func GetTasksHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		rows, err := db.Query(`
			SELECT task_id, project_id, assigned_to, title, description, status, priority, start_date, due_date, created_by 
			FROM Tasks`)
		if err != nil {
			http.Error(w, "Failed to fetch tasks", http.StatusInternalServerError)
			return
		}
		defer rows.Close()

		var tasks []Task
		for rows.Next() {
			var task Task
			err := rows.Scan(
				&task.TaskID,
				&task.ProjectID,
				&task.AssignedTo,
				&task.Title,
				&task.Description,
				&task.Status,
				&task.Priority,
				&task.StartDate,
				&task.DueDate,
				&task.CreatedBy,
			)
			if err != nil {
				http.Error(w, "Failed to scan task", http.StatusInternalServerError)
				return
			}
			tasks = append(tasks, task)
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(tasks)
	}
}

func GetTaskByIdHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		id := mux.Vars(r)["id"]

		var task Task
		query := `
			SELECT task_id, project_id, assigned_to, title, description, status, priority, start_date, due_date, created_by
			FROM Tasks WHERE task_id = ?`
		err := db.QueryRow(query, id).Scan(
			&task.TaskID,
			&task.ProjectID,
			&task.AssignedTo,
			&task.Title,
			&task.Description,
			&task.Status,
			&task.Priority,
			&task.StartDate, // Automatyczna konwersja na time.Time
			&task.DueDate,   // Automatyczna konwersja na time.Time
			&task.CreatedBy,
		)
		if err != nil {
			if err == sql.ErrNoRows {
				http.Error(w, "Task not found", http.StatusNotFound)
				return
			}
			http.Error(w, "Failed to fetch task", http.StatusInternalServerError)
			return
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(task)
	}
}

func UpdateTaskHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		id := mux.Vars(r)["id"]

		var task Task
		err := json.NewDecoder(r.Body).Decode(&task)
		if err != nil || task.Title == "" || task.Description == "" {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		query := `
			UPDATE Tasks 
			SET project_id = ?, assigned_to = ?, title = ?, description = ?, 
				status = ?, priority = ?, start_date = ?, due_date = ?, updated_at = CURRENT_TIMESTAMP 
			WHERE task_id = ?`
		result, err := db.Exec(query,
			task.ProjectID,
			task.AssignedTo,
			task.Title,
			task.Description,
			task.Status,
			task.Priority,
			task.StartDate.Format("2006-01-02"), // Konwersja na format YYYY-MM-DD
			task.DueDate.Format("2006-01-02"),   // Konwersja na format YYYY-MM-DD
			id,
		)
		if err != nil {
			http.Error(w, "Failed to update task", http.StatusInternalServerError)
			return
		}

		rowsAffected, _ := result.RowsAffected()
		if rowsAffected == 0 {
			http.Error(w, "Task not found", http.StatusNotFound)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{
			"message": "Task updated successfully",
		})
	}
}

func DeleteTaskHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		id := mux.Vars(r)["id"]

		query := "DELETE FROM Tasks WHERE task_id = ?"
		result, err := db.Exec(query, id)
		if err != nil {
			http.Error(w, "Failed to delete task", http.StatusInternalServerError)
			return
		}

		rowsAffected, _ := result.RowsAffected()
		if rowsAffected == 0 {
			http.Error(w, "Task not found", http.StatusNotFound)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{
			"message": "Task deleted successfully",
		})
	}
}
