package router

import (
	"bytes"
	"encoding/json"
	"io"
	"log"
	"net/http"
	"os"

	"github.com/BuddhiLW/go-CMS-backend/auth/db"
	"github.com/BuddhiLW/go-CMS-backend/auth/middleware"
	"github.com/BuddhiLW/go-CMS-backend/auth/util"
)

type IssuedToken struct {
	AccessToken string `json:"access_token"`
	TokenType   string `json:"token_type"`
	ExpiresIn   int    `json:"expires_in"`
}

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

func handlerIssuedToken(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Access-Control-Allow-Credentials", "false")
	w.Header().Add("Content-Type", "application/json")
	if r.URL.Path != "/v1/issue-token" {
		w.WriteHeader(http.StatusNotFound)
		return
	}

	// Call auth0 to get the token
	token, err := callAuth0TokenAPI()
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		w.Write([]byte(err.Error()))
	}

	w.WriteHeader(http.StatusOK)

	// Response:
	// Marshall data in response.
	var message []byte
	message, err = json.Marshal(
		Message{
			Text: "Token issued successfully.",
			Code: 200,
			Data: token,
		})
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		w.Write([]byte(err.Error()))
	}

	w.Write(message)
}

func handlerGetUser(w http.ResponseWriter, r *http.Request) {
	// CORS Headers.
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

	// Inspect if user exists in the database
	rawUrl := r.URL
	log.Println("Inspection - r.URL: ", rawUrl)
	user := db.GetUser(r.URL.Query().Get("sub"))
	log.Println("Inspection: ", user)
	log.Println("Inspection - user.Sub: ", user.Sub)

	if user.Sub != "" {
		log.Println("User already exists. Return Ok and move on.")
		w.WriteHeader(http.StatusOK)
		message, _ := json.Marshal(Message{
			Text: "User already exists.",
			Code: 204,
			Data: user,
		})
		w.Write(message)
		return
	}

	// Case user does not exist, proceed to create the user
	user = util.CreateNewUser()
	profile := util.CreateNewProfile()
	authResults := util.CreateNewAuthResult()

	// Unmarshall the JSON data profile and authResults
	incJson := util.IncJson{}
	err := json.NewDecoder(r.Body).Decode(&incJson)

	// fill the profile and authResults with the unmarshalled data
	profile = &incJson.Profile
	authResults = &incJson.AuthResult

	// User Struct: fill the user with the profile and authResults
	user.Profile = *profile
	user.AuthResults = append(user.AuthResults, *authResults)
	user.Sub = profile.Sub

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
	message, err = json.Marshal(
		Message{Text: "User created successfully.",
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
		// fmt.Println(path, handler)
		mux.Handle("/v1"+path, handler)
	}

	// fmt.Println("/v1/issue-token", http.HandlerFunc(handlerIssuedToken))
	mux.Handle("/v1/issue-token", http.HandlerFunc(handlerIssuedToken))

	return mux
}

type Auth0TokenCall struct {
	ClientID     string `json:"client_id"`
	ClientSecret string `json:"client_secret"`
	Audience     string `json:"audience"`
	GrantType    string `json:"grant_type"`
}

func callAuth0TokenAPI() (token IssuedToken, err error) {
	// log.Println("Calling Auth0 to get the token")
	url := "https://" + os.Getenv("AUTH0_DOMAIN") + "/oauth/token"
	auth0TokenCall := Auth0TokenCall{
		ClientID:     os.Getenv("AUTH0_CLIENT_ID"),
		ClientSecret: os.Getenv("AUTH0_CLIENT_SECRET"),
		Audience:     os.Getenv("AUTH0_AUDIENCE"),
		GrantType:    "client_credentials",
	}

	body, err := json.Marshal(auth0TokenCall)

	if err != nil {
		return IssuedToken{}, err
	}

	req, _ := http.NewRequest(http.MethodPost, url, bytes.NewBuffer(body))
	req.Header.Add("content-type", "application/json")
	res, err := http.DefaultClient.Do(req)
	if err != nil {
		return IssuedToken{}, err
	}
	defer res.Body.Close()

	out, err := io.ReadAll(res.Body)

	if err != nil {
		return IssuedToken{}, err
	}

	tokenResp := IssuedToken{}
	err = json.Unmarshal(out, &tokenResp)
	if err != nil {
		return IssuedToken{}, err
	}

	return tokenResp, nil
}
