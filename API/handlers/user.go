package handlers

import (
	"database/sql"
	"encoding/json"
	"net/http"
)

type User struct {
	UserID    int    `json:"user_id"`
	Email     string `json:"email"`
	Username  string `json:"username"`
	FirstName string `json:"first_name"`
	LastName  string `json:"last_name"`
	Password  string `json:"password"`
}

func CreateUserHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var user User
		err := json.NewDecoder(r.Body).Decode(&user)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		_, err = db.Exec("INSERT INTO Users (email, username, first_name, last_name, password_hash) VALUES (?, ?, ?, ?, ?)",
			user.Email, user.Username, user.FirstName, user.LastName, user.Password)
		if err != nil {
			http.Error(w, "Failed to create user", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusCreated)
		json.NewEncoder(w).Encode(map[string]string{"message": "User created successfully"})
	}
}

func GetUsersHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Zapytanie do bazy danych
		rows, err := db.Query("SELECT user_id, email, username, first_name, last_name FROM Users")
		if err != nil {
			http.Error(w, "Failed to fetch users", http.StatusInternalServerError)
			return
		}
		defer rows.Close()

		// Przetwarzanie wyników
		var users []User
		for rows.Next() {
			var user User
			if err := rows.Scan(&user.UserID, &user.Email, &user.Username, &user.FirstName, &user.LastName); err != nil {
				http.Error(w, "Failed to scan user", http.StatusInternalServerError)
				return
			}
			users = append(users, user)
		}

		// Wysyłanie odpowiedzi
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(users)
	}
}
