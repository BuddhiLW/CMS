package router

import (
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/BuddhiLW/go-CMS-backend/auth/db"
	"github.com/BuddhiLW/go-CMS-backend/auth/middleware"
	"github.com/BuddhiLW/go-CMS-backend/auth/util"
)

type ensuredRoutes map[string]http.Handler
type Message struct {
	Text string      `json:"message"`
	Code int         `json:"code"`
	Data interface{} `json:"data"`
}

var EnsuredRoutes = ensuredRoutes{
	"/create-user": middleware.EnsureValidToken()(http.HandlerFunc(handlerCreateUser)),
	"/get-user":    middleware.EnsureValidToken()(http.HandlerFunc(handlerGetUser)),
	"/update-user": middleware.EnsureValidToken()(http.HandlerFunc(handlerUpdateUser)),
	"/delete-user": middleware.EnsureValidToken()(http.HandlerFunc(handlerDeleteUser)),
}

func handlerGetUser(w http.ResponseWriter, r *http.Request) {
	// CORS Headers.
	w.Header().Add("Access-Control-Allow-Credentials", "true")
	w.Header().Add("Access-Control-Allow-Origin", "*") // *
	w.Header().Add("Access-Control-Allow-Headers", "Authorization")
	w.Header().Add("Content-Type", "application/json")
	if r.URL.Path != "/get-user" {
		w.WriteHeader(http.StatusNotFound)
		return
	}

	// Get the user from the database
	user := db.GetUser(r.URL.Query().Get("sub"))

	w.WriteHeader(http.StatusOK)
	// Response:
	// Marshall data in response.
	var message []byte
	var err error
	message, err = json.Marshal(Message{Text: "User retrieved successfully.",
		Code: 200,
		Data: user,
	})
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		w.Write([]byte(err.Error()))
	}

	w.Write(message)
}

func handlerUpdateUser(w http.ResponseWriter, r *http.Request) {
	// CORS Headers.
	w.Header().Add("Access-Control-Allow-Credentials", "true")
	w.Header().Add("Access-Control-Allow-Origin", "*")
	w.Header().Add("Access-Control-Allow-Headers", "Authorization")

	w.Header().Add("Content-Type", "application/json")
	if r.URL.Path != "/update-user" {
		w.WriteHeader(http.StatusNotFound)
		return
	}

	user := util.CreateNewUser()
	err := json.NewDecoder(r.Body).Decode(user)

	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("Invalid request body: " + err.Error()))
	}

	// Update the user in the database
	db.UpdateUser(r.URL.Query().Get("sub"), user)

	w.WriteHeader(http.StatusOK)
	// Response:
	// Updated the user, and marshall data in response.
	var message []byte
	message, err = json.Marshal(Message{Text: "User updated successfully.",
		Code: 200,
		Data: user,
	})
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		w.Write([]byte(err.Error()))
	}

	w.Write(message)
}

func handlerDeleteUser(w http.ResponseWriter, r *http.Request) {
	// CORS Headers.
	w.Header().Add("Access-Control-Allow-Credentials", "true")
	w.Header().Add("Access-Control-Allow-Origin", "*")
	w.Header().Add("Access-Control-Allow-Headers", "Authorization")

	w.Header().Add("Content-Type", "application/json")
	if r.URL.Path != "/delete-user" {
		w.WriteHeader(http.StatusNotFound)
		return
	}

	// Delete the user from the database
	db.DeleteUser(r.URL.Query().Get("sub"))

	w.WriteHeader(http.StatusOK)
	// Response:
	// Deleted the user, and marshall data in response.
	var message []byte
	var err error
	message, err = json.Marshal(Message{Text: "User deleted successfully.",
		Code: 200,
		Data: nil,
	})
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		w.Write([]byte(err.Error()))
	}

	w.Write(message)
}

func handlerCreateUser(w http.ResponseWriter, r *http.Request) {
	// CORS Headers.
	// w.Header().Add("Access-Control-Allow-Credentials", "true")
	// w.Header().Add("Access-Control-Allow-Origin", "*") // *
	// w.Header().Add("Access-Control-Allow-Headers", "Authorization")

	// w.Header().Add("Content-Type", "application/json")
	// if r.URL.Path != "/create-user" {
	// 	w.WriteHeader(http.StatusNotFound)
	// 	return
	// }

	// log.Println("Request Body:", r.Body)
	// log.Println(r.Body.Read([]byte{}))

	user := util.CreateNewUser()
	err := json.NewDecoder(r.Body).Decode(user)

	// log.Println("User:", user)

	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("Invalid request body: " + err.Error()))
	}

	// Create the user in the database
	db.CreateUser(user)

	w.WriteHeader(http.StatusOK)
	// Response:
	// Created the new user, and marshall data in response.
	var message []byte
	message, err = json.Marshal(Message{Text: "User created successfully.",
		Code: 200,
		Data: user,
	})
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		w.Write([]byte(err.Error()))
	}

	w.Write(message)
}

// New sets up our routes and returns a *http.ServeMux.
func New() *http.ServeMux {
	mux := http.NewServeMux()

	for path, handler := range EnsuredRoutes {
		fmt.Println(path, handler)
		mux.Handle("/v1"+path, handler)
	}

	// router := cors.Default().Handler(mux)
	return mux
}

// // This route is always accessible.
// router.Handle("/api/public", http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
// 	w.Header().Set("Content-Type", "application/json")
// 	w.WriteHeader(http.StatusOK)
// 	w.Write([]byte(`{"message":"Hello from a public endpoint! You don't need to be authenticated to see this."}`))
// }))

// // This route is only accessible if the user has a valid access_token.
// router.Handle("/api/private", middleware.EnsureValidToken()(
// 	http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
// 		// CORS Headers.
// 		w.Header().Set("Access-Control-Allow-Credentials", "true")
// 		w.Header().Set("Access-Control-Allow-Origin", "*")
// 		w.Header().Set("Access-Control-Allow-Headers", "Authorization")

// 		w.Header().Set("Content-Type", "application/json")
// 		w.WriteHeader(http.StatusOK)
// 		w.Write([]byte(`{"message":"Hello from a private endpoint! You need to be authenticated to see this."}`))
// 	}),
// ))

// // This route is only accessible if the user has a
// // valid access_token with the read:messages scope.
// router.Handle("/api/private-scoped", middleware.EnsureValidToken()(
// 	http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
// 		// CORS Headers.
// 		w.Header().Set("Access-Control-Allow-Credentials", "true")
// 		w.Header().Set("Access-Control-Allow-Origin", "http://localhost:3000")
// 		w.Header().Set("Access-Control-Allow-Headers", "Authorization")

// 		w.Header().Set("Content-Type", "application/json")

// 		token := r.Context().Value(jwtmiddleware.ContextKey{}).(*validator.ValidatedClaims)

// 		claims := token.CustomClaims.(*middleware.CustomClaims)
// 		if !claims.HasScope("read:messages") {
// 			w.WriteHeader(http.StatusForbidden)
// 			w.Write([]byte(`{"message":"Insufficient scope."}`))
// 			return
// 		}

// 		w.WriteHeader(http.StatusOK)
// 		w.Write([]byte(`{"message":"Hello from a private endpoint! You need to be authenticated to see this."}`))
// 	}),
// ))
