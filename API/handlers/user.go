package handlers

import (
	"database/sql"
	"encoding/json"
	"github.com/gorilla/mux"
	"net/http"
	"strconv"
	"taskflow/utils"
)

type User struct {
	UserID       int    `json:"user_id"`
	Email        string `json:"email"`
	Username     string `json:"username"`
	FirstName    string `json:"first_name"`
	LastName     string `json:"last_name"`
	PasswordHash string `json:"password_hash,omitempty"` // Hasło w formie zahashowanej, nie zwracamy w odpowiedzi - omitempty
	CreatedAt    string `json:"created_at"`
}

func CreateUserHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var user User
		err := json.NewDecoder(r.Body).Decode(&user)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		// Wstaw użytkownika do bazy danych
		result, err := db.Exec("INSERT INTO Users (email, username, first_name, last_name, password_hash) VALUES (?, ?, ?, ?, ?)",
			user.Email, user.Username, user.FirstName, user.LastName, user.PasswordHash)
		if err != nil {
			http.Error(w, "Failed to create user", http.StatusInternalServerError)
			return
		}

		// Pobierz ID nowo utworzonego użytkownika
		userID, err := result.LastInsertId()
		if err != nil {
			http.Error(w, "Failed to fetch user ID", http.StatusInternalServerError)
			return
		}

		// odpowiedź
		w.WriteHeader(http.StatusCreated)
		json.NewEncoder(w).Encode(map[string]interface{}{
			"message": "User created successfully",
			"user_id": userID,
		})
	}
}

func GetUsersHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Zapytanie do bazy danych
		rows, err := db.Query("SELECT user_id, email, username, first_name, last_name, created_at FROM Users")
		if err != nil {
			http.Error(w, "Failed to fetch users", http.StatusInternalServerError)
			return
		}
		defer rows.Close()

		// Przetwarzanie wyników
		var users []User
		for rows.Next() {
			var user User
			if err := rows.Scan(&user.UserID, &user.Email, &user.Username, &user.FirstName, &user.LastName, &user.CreatedAt); err != nil {
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

func GetUserByIdHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Pobranie parametru {id} z adresu URL
		vars := mux.Vars(r)
		id := vars["id"]

		// Sprawdź, czy ID jest liczbą
		userID, err := strconv.Atoi(id)
		if err != nil {
			http.Error(w, "Invalid user ID", http.StatusBadRequest)
			return
		}

		// Zapytanie do bazy danych
		var user User
		err = db.QueryRow("SELECT user_id, email, username, first_name, last_name, created_at FROM Users WHERE user_id = ?", userID).
			Scan(&user.UserID, &user.Email, &user.Username, &user.FirstName, &user.LastName, &user.CreatedAt)
		if err != nil {
			if err == sql.ErrNoRows {
				http.Error(w, "User not found", http.StatusNotFound)
				return
			}
			http.Error(w, "Failed to fetch user", http.StatusInternalServerError)
			return
		}

		// Wysyłanie odpowiedzi
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(user)
	}
}

func UpdateUserHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Pobierz ID użytkownika z URL
		vars := mux.Vars(r)
		id := vars["id"]

		// Sprawdź, czy ID jest liczbą
		userID, err := strconv.Atoi(id)
		if err != nil {
			http.Error(w, "Invalid user ID", http.StatusBadRequest)
			return
		}

		// Pobierz dane z żądania
		var user User
		err = json.NewDecoder(r.Body).Decode(&user)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		// Aktualizuj użytkownika w bazie danych
		_, err = db.Exec(`
            UPDATE Users
            SET email = ?, username = ?, first_name = ?, last_name = ?
            WHERE user_id = ?
        `, user.Email, user.Username, user.FirstName, user.LastName, userID)
		if err != nil {
			http.Error(w, "Failed to update user", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{"message": "User updated successfully"})
	}
}

func DeleteUserHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		id := vars["id"]

		userID, err := strconv.Atoi(id)
		if err != nil {
			http.Error(w, "Invalid user ID", http.StatusBadRequest)
			return
		}

		_, err = db.Exec("DELETE FROM Users WHERE user_id = ?", userID)
		if err != nil {
			http.Error(w, "Failed to delete user", http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{"message": "User deleted successfully"})
	}
}

func RegisterUserHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var user User
		err := json.NewDecoder(r.Body).Decode(&user)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		// Hashowanie hasła
		hashedPassword := utils.HashPassword(user.PasswordHash)

		// Wstaw użytkownika do bazy danych
		_, err = db.Exec("INSERT INTO Users (email, username, first_name, last_name, password_hash) VALUES (?, ?, ?, ?, ?)",
			user.Email, user.Username, user.FirstName, user.LastName, hashedPassword)
		if err != nil {
			http.Error(w, "Failed to register user", http.StatusInternalServerError)
			return
		}

		// Przygotowanie odpowiedzi
		w.WriteHeader(http.StatusCreated)
		json.NewEncoder(w).Encode(map[string]string{"message": "User registered successfully"})
	}
}

func LoginUserHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var credentials struct {
			Identifier string `json:"identifier"` // Can be email or username
			Password   string `json:"password"`
		}

		err := json.NewDecoder(r.Body).Decode(&credentials)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		// Query the database for either email or username
		var storedHash string
		err = db.QueryRow(`
			SELECT password_hash 
			FROM Users 
			WHERE email = ? OR username = ?`,
			credentials.Identifier, credentials.Identifier,
		).Scan(&storedHash)

		if err != nil {
			if err == sql.ErrNoRows {
				http.Error(w, "Invalid email/username or password", http.StatusUnauthorized)
				return
			}
			http.Error(w, "Failed to fetch user", http.StatusInternalServerError)
			return
		}

		// Compare the password hash
		if utils.HashPassword(credentials.Password) != storedHash {
			http.Error(w, "Invalid email/username or password", http.StatusUnauthorized)
			return
		}

		// Login successful
		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{"message": "Login successful"})
	}
}
