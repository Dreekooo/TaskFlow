package handlers

import (
	"database/sql"
	"encoding/json"
	"net/http"
)

type Task struct {
	ProjectID   int    `json:"project_id"`
	AssignedTo  int    `json:"assigned_to"`
	Title       string `json:"title"`
	Description string `json:"description"`
	Status      int    `json:"status"`     // ID statusu
	Priority    int    `json:"priority"`   // ID priorytetu
	StartDate   string `json:"start_date"` // Format: "YYYY-MM-DD"
	DueDate     string `json:"due_date"`   // Format: "YYYY-MM-DD"
	CreatedBy   int    `json:"created_by"`
}

func CreateTaskHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var task Task
		err := json.NewDecoder(r.Body).Decode(&task)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		_, err = db.Exec(
			"INSERT INTO Tasks (project_id, assigned_to, title, description, status, priority, start_date, due_date, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
			task.ProjectID, task.AssignedTo, task.Title, task.Description, task.Status, task.Priority, task.StartDate, task.DueDate, task.CreatedBy,
		)
		if err != nil {
			http.Error(w, "Failed to create task", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusCreated)
		json.NewEncoder(w).Encode(map[string]string{"message": "Task created successfully"})
	}
}
