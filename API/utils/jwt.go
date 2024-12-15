package utils

import (
	"time"

	"github.com/golang-jwt/jwt/v5"
)

var jwtSecret = []byte("your-secret-key") // Sekretny klucz JWT

// GenerateToken generates a JWT token for a user
func GenerateToken(userID int) (string, error) {
	// Tworzenie tokena
	claims := jwt.MapClaims{
		"user_id": userID,
		"exp":     time.Now().Add(time.Hour * 72).Unix(), // Token ważny przez 72 godziny
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)

	// Podpisanie tokena
	return token.SignedString(jwtSecret)
}

// ValidateToken validates a JWT token and extracts claims
func ValidateToken(tokenString string) (jwt.MapClaims, error) {
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		return jwtSecret, nil
	})

	if claims, ok := token.Claims.(jwt.MapClaims); ok && token.Valid {
		return claims, nil
	}
	return nil, err
}
