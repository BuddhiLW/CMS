package main

import (
	"log"
	"net/http"

	"github.com/BuddhiLW/go-CMS-backend/auth/db"
	"github.com/BuddhiLW/go-CMS-backend/auth/router"
	"github.com/joho/godotenv"
	"github.com/rs/cors"
)

func main() {
	if err := godotenv.Load(); err != nil {
		log.Fatalf("Error loading the .env file: %v", err)
	}

	c := cors.New(cors.Options{
		AllowedOrigins:   []string{"http://localhost:8020/", "http://localhost:8020", "localhost:8020"},
		AllowCredentials: true,
		AllowedHeaders:   []string{"Authorization", "Content-Type", "Origin", "X-Requested-With", "Accept"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		// Enable Debugging for testing, consider disabling in production
		Debug: true,
	})

	rtr := c.Handler(router.New())
	// router := cors.Default().Handler(mux)

	conn := db.ConnectDB()
	db.Migration(conn)

	log.Print("Server listening on http://localhost:3010")
	if err := http.ListenAndServe("0.0.0.0:3010", rtr); err != nil {
		log.Fatalf("There was an error with the http server: %v", err)
	}

}
