package middleware

import (
	"net/http"
	"strings"

	"taskflow/utils"
)

func JWTMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		tokenString := r.Header.Get("Authorization")
		if tokenString == "" {
			http.Error(w, "Authorization header required", http.StatusUnauthorized)
			return
		}

		// Usuń prefiks "Bearer "
		if strings.HasPrefix(tokenString, "Bearer ") {
			tokenString = strings.TrimPrefix(tokenString, "Bearer ")
		} else {
			http.Error(w, "Invalid Authorization header format", http.StatusUnauthorized)
			return
		}

		// Walidacja tokena
		_, err := utils.ValidateToken(tokenString)
		if err != nil {
			http.Error(w, "Invalid or expired token: "+err.Error(), http.StatusUnauthorized)
			return
		}

		// Przejdź do następnego handlera
		next.ServeHTTP(w, r)
	})
}
