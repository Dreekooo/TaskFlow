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

		// Dekodowanie JSON
		err := json.NewDecoder(r.Body).Decode(&user)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		// Sprawdzenie wymaganych pól
		if user.Email == "" || user.Username == "" || user.PasswordHash == "" {
			http.Error(w, "Missing required fields", http.StatusBadRequest)
			return
		}

		// Sprawdzenie unikalności e-mail i username
		var exists bool
		err = db.QueryRow("SELECT EXISTS(SELECT 1 FROM Users WHERE email = ? OR username = ?)", user.Email, user.Username).Scan(&exists)
		if err != nil {
			http.Error(w, "Database error", http.StatusInternalServerError)
			return
		}
		if exists {
			http.Error(w, "Email or username already exists", http.StatusConflict)
			return
		}

		// Hashowanie hasła za pomocą Bcrypt
		hashedPassword, err := utils.HashPassword(user.PasswordHash)
		if err != nil {
			http.Error(w, "Failed to hash password", http.StatusInternalServerError)
			return
		}

		// Wstawienie użytkownika do bazy danych
		_, err = db.Exec("INSERT INTO Users (email, username, first_name, last_name, password_hash) VALUES (?, ?, ?, ?, ?)",
			user.Email, user.Username, user.FirstName, user.LastName, hashedPassword)
		if err != nil {
			http.Error(w, "Failed to register user", http.StatusInternalServerError)
			return
		}

		// Sukces
		w.WriteHeader(http.StatusCreated)
		json.NewEncoder(w).Encode(map[string]string{
			"message": "User registered successfully",
		})
	}
}

func LoginUserHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var credentials struct {
			Identifier string `json:"identifier"` // Może być email lub username
			Password   string `json:"password"`
		}

		err := json.NewDecoder(r.Body).Decode(&credentials)
		if err != nil {
			http.Error(w, "Invalid input", http.StatusBadRequest)
			return
		}

		// Pobierz hash hasła oraz ID użytkownika z bazy danych
		var storedHash string
		var userID int
		err = db.QueryRow(`
			SELECT user_id, password_hash 
			FROM Users 
			WHERE email = ? OR username = ?`,
			credentials.Identifier, credentials.Identifier,
		).Scan(&userID, &storedHash)

		if err != nil {
			if err == sql.ErrNoRows {
				http.Error(w, "Invalid email/username or password", http.StatusUnauthorized)
				return
			}
			http.Error(w, "Failed to fetch user", http.StatusInternalServerError)
			return
		}

		// Porównaj hasło za pomocą Bcrypt
		if err := utils.ComparePassword(storedHash, credentials.Password); err != nil {
			http.Error(w, "Invalid email/username or password", http.StatusUnauthorized)
			return
		}

		// Generowanie tokena JWT
		token, err := utils.GenerateToken(userID)
		if err != nil {
			http.Error(w, "Failed to generate token", http.StatusInternalServerError)
			return
		}

		// Zwróć token w odpowiedzi
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(map[string]string{
			"message": "Login successful",
			"token":   token,
		})
	}
}

func DeleteOwnAccountHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Pobierz token JWT z nagłówka Authorization
		tokenString := r.Header.Get("Authorization")
		if tokenString == "" {
			http.Error(w, "Authorization header required", http.StatusUnauthorized)
			return
		}

		// Walidacja tokena
		claims, err := utils.ValidateToken(tokenString)
		if err != nil {
			http.Error(w, "Invalid or expired token", http.StatusUnauthorized)
			return
		}

		// Pobierz user_id z tokena JWT
		userIDFloat, ok := claims["user_id"].(float64)
		if !ok {
			http.Error(w, "Invalid token data", http.StatusUnauthorized)
			return
		}
		userID := int(userIDFloat)

		// Usuń konto z bazy danych
		result, err := db.Exec("DELETE FROM Users WHERE user_id = ?", userID)
		if err != nil {
			http.Error(w, "Failed to delete account", http.StatusInternalServerError)
			return
		}

		// Sprawdź, czy użytkownik został usunięty
		rowsAffected, _ := result.RowsAffected()
		if rowsAffected == 0 {
			http.Error(w, "User not found", http.StatusNotFound)
			return
		}

		// Zwrot odpowiedzi
		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{
			"message": "Account deleted successfully",
		})
	}
}
