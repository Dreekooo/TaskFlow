package main

import (
	"database/sql"
	"log"
	"os"

	_ "github.com/mattn/go-sqlite3"
)

func initDB() *sql.DB {
	// Sprawdź, czy baza danych istnieje
	if _, err := os.Stat("taskflow.db"); os.IsNotExist(err) {
		file, err := os.Create("taskflow.db")
		if err != nil {
			log.Fatal("Failed to create database file:", err)
		}
		file.Close()
		log.Println("Database file created successfully")
	}

	// Połącz się z bazą danych
	db, err := sql.Open("sqlite3", "taskflow.db")
	if err != nil {
		log.Fatal("Failed to connect to database:", err)
	}

	// Wczytaj schemat bazy danych z pliku schema.sql
	schema, err := os.ReadFile("schema.sql")
	if err != nil {
		log.Fatal("Failed to read schema file:", err)
	}
	_, err = db.Exec(string(schema))
	if err != nil {
		log.Fatal("Failed to execute schema:", err)
	}

	log.Println("Database initialized successfully")
	return db
}
