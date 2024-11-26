package handlers

import (
	"database/sql"
	"encoding/json"
	"github.com/gorilla/mux"
	"net/http"
	"strconv"
)

type Task struct {
	TaskID      int    `json:"task_id"`
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

func GetTasksHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Zapytanie do bazy danych
		rows, err := db.Query("SELECT task_id, project_id, assigned_to, title, description, status, priority, start_date, due_date, created_by FROM Tasks")
		if err != nil {
			http.Error(w, "Failed to fetch tasks", http.StatusInternalServerError)
			return
		}
		defer rows.Close()

		// Przetwarzanie wyników
		var tasks []Task
		for rows.Next() {
			var task Task
			if err := rows.Scan(&task.TaskID, &task.ProjectID, &task.AssignedTo, &task.Title, &task.Description, &task.Status, &task.Priority, &task.StartDate, &task.DueDate, &task.CreatedBy); err != nil {
				http.Error(w, "Failed to scan task", http.StatusInternalServerError)
				return
			}
			tasks = append(tasks, task)
		}

		// Wysyłanie odpowiedzi
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(tasks)
	}
}

func GetTaskByIdHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Pobranie parametru {id} z adresu URL
		vars := mux.Vars(r)
		id := vars["id"]

		// Sprawdź, czy ID jest liczbą
		taskID, err := strconv.Atoi(id)
		if err != nil {
			http.Error(w, "Invalid task ID", http.StatusBadRequest)
			return
		}

		// Zapytanie do bazy danych
		var task Task
		err = db.QueryRow("SELECT task_id, project_id, assigned_to, title, description, status, priority, start_date, due_date, created_by FROM Tasks WHERE task_id = ?", taskID).
			Scan(&task.TaskID, &task.ProjectID, &task.AssignedTo, &task.Title, &task.Description, &task.Status, &task.Priority, &task.StartDate, &task.DueDate, &task.CreatedBy)
		if err != nil {
			if err == sql.ErrNoRows {
				http.Error(w, "Task not found", http.StatusNotFound)
				return
			}
			http.Error(w, "Failed to fetch task", http.StatusInternalServerError)
			return
		}

		// Wysyłanie odpowiedzi
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(task)
	}
}

func UpdateTaskHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Pobierz ID zadania z URL
		vars := mux.Vars(r)
		id := vars["id"]

		taskID, err := strconv.Atoi(id)
		if err != nil {
			http.Error(w, "Invalid task ID", http.StatusBadRequest)
			return
		}

		// Pobierz dane z żądania
		var task Task
		err = json.NewDecoder(r.Body).Decode(&task)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		// Aktualizuj zadanie w bazie danych
		_, err = db.Exec(`
            UPDATE Tasks
            SET project_id = ?, assigned_to = ?, title = ?, description = ?, status = ?, priority = ?, start_date = ?, due_date = ?
            WHERE task_id = ?
        `, task.ProjectID, task.AssignedTo, task.Title, task.Description, task.Status, task.Priority, task.StartDate, task.DueDate, taskID)
		if err != nil {
			http.Error(w, "Failed to update task", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{"message": "Task updated successfully"})
	}
}

func DeleteTaskHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id := vars["id"]

		taskID, err := strconv.Atoi(id)
		if err != nil {
			http.Error(w, "Invalid task ID", http.StatusBadRequest)
			return
		}

		_, err = db.Exec("DELETE FROM Tasks WHERE task_id = ?", taskID)
		if err != nil {
			http.Error(w, "Failed to delete task", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{"message": "Task deleted successfully"})
	}
}
