package utils

import (
	"fmt"
	"time"

	"github.com/golang-jwt/jwt/v5"
)

var jwtSecret = []byte("secret") // Sekretny klucz JWT

// GenerateToken generates a JWT token for a user
func GenerateToken(userID int) (string, error) {
	// Tworzenie tokena
	claims := jwt.MapClaims{
		"user_id": userID,
		"exp":     time.Now().Add(time.Hour * 72).Unix(), // Token ważny przez 72 godziny
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)

	fmt.Println("Current time:", time.Now().Unix())
	fmt.Println("Token expiration time:", claims["exp"])

	// Podpisanie tokena
	signedToken, err := token.SignedString(jwtSecret)
	if err != nil {
		fmt.Println("Error generating token:", err)
		return "", err
	}
	fmt.Println("Generated token:", signedToken)
	return signedToken, nil
}

// ValidateToken validates a JWT token and extracts claims
func ValidateToken(tokenString string) (jwt.MapClaims, error) {
	fmt.Println("Validating token:", tokenString)

	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		// Sprawdź metodę podpisu
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return jwtSecret, nil
	})

	if err != nil {
		fmt.Println("Error parsing token:", err)
		return nil, fmt.Errorf("token parsing error: %v", err)
	}

	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok || !token.Valid {
		fmt.Println("Invalid token or claims")
		return nil, fmt.Errorf("invalid token or claims")
	}

	fmt.Println("Token validated successfully. Claims:", claims)
	return claims, nil
}
