package handlers

import (
	"database/sql"
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
)

type TaskComment struct {
	CommentID int    `json:"comment_id"`
	TaskID    int    `json:"task_id"`
	UserID    int    `json:"user_id"`
	Content   string `json:"content"`
	CreatedAt string `json:"created_at,omitempty"`
}

// POST - Create a new TaskComment
func CreateTaskCommentHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var taskComment TaskComment
		err := json.NewDecoder(r.Body).Decode(&taskComment)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		_, err = db.Exec("INSERT INTO TaskComments (task_id, user_id, content) VALUES (?, ?, ?)",
			taskComment.TaskID, taskComment.UserID, taskComment.Content)
		if err != nil {
			http.Error(w, "Failed to create task comment", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusCreated)
		json.NewEncoder(w).Encode(map[string]string{"message": "Task comment created successfully"})
	}
}

// GET - Retrieve all TaskComments
func GetTaskCommentsHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		rows, err := db.Query("SELECT comment_id, task_id, user_id, content, created_at FROM TaskComments")
		if err != nil {
			http.Error(w, "Failed to fetch task comments", http.StatusInternalServerError)
			return
		}
		defer rows.Close()

		var taskComments []TaskComment
		for rows.Next() {
			var taskComment TaskComment
			err := rows.Scan(&taskComment.CommentID, &taskComment.TaskID, &taskComment.UserID, &taskComment.Content, &taskComment.CreatedAt)
			if err != nil {
				http.Error(w, "Failed to scan task comment", http.StatusInternalServerError)
				return
			}
			taskComments = append(taskComments, taskComment)
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(taskComments)
	}
}

func GetTaskCommentByIdHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Pobierz ID z URL
		vars := mux.Vars(r)
		id := vars["id"]

		commentID, err := strconv.Atoi(id)
		if err != nil {
			http.Error(w, "Invalid comment ID", http.StatusBadRequest)
			return
		}

		// Zapytanie do bazy danych
		var taskComment TaskComment
		err = db.QueryRow("SELECT comment_id, task_id, user_id, content, created_at FROM TaskComments WHERE comment_id = ?",
			commentID).Scan(&taskComment.CommentID, &taskComment.TaskID, &taskComment.UserID, &taskComment.Content, &taskComment.CreatedAt)

		if err != nil {
			if err == sql.ErrNoRows {
				http.Error(w, "Task comment not found", http.StatusNotFound)
				return
			}
			http.Error(w, "Failed to fetch task comment", http.StatusInternalServerError)
			return
		}

		// Zwróć dane w odpowiedzi
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(taskComment)
	}
}

// PUT - Update a TaskComment
func UpdateTaskCommentHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id := vars["id"]

		commentID, err := strconv.Atoi(id)
		if err != nil {
			http.Error(w, "Invalid comment ID", http.StatusBadRequest)
			return
		}

		var taskComment TaskComment
		err = json.NewDecoder(r.Body).Decode(&taskComment)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		_, err = db.Exec("UPDATE TaskComments SET task_id = ?, user_id = ?, content = ? WHERE comment_id = ?",
			taskComment.TaskID, taskComment.UserID, taskComment.Content, commentID)
		if err != nil {
			http.Error(w, "Failed to update task comment", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{"message": "Task comment updated successfully"})
	}
}

// DELETE - Remove a TaskComment
func DeleteTaskCommentHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id := vars["id"]

		commentID, err := strconv.Atoi(id)
		if err != nil {
			http.Error(w, "Invalid comment ID", http.StatusBadRequest)
			return
		}

		_, err = db.Exec("DELETE FROM TaskComments WHERE comment_id = ?", commentID)
		if err != nil {
			http.Error(w, "Failed to delete task comment", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{"message": "Task comment deleted successfully"})
	}
}
