package main

import (
	"log"
	"net/http"
)

func main() {
	// Inicjalizacja bazy danych
	db := initDB()
	defer db.Close()

	// Konfiguracja tras
	r := SetupRoutes(db)

	// Start serwera
	log.Println("Starting server on :8080...")
	log.Fatal(http.ListenAndServe(":8080", r))
}
