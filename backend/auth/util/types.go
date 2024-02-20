package util

import "time"

type User struct {
	AuthResult struct {
		AppState       any    `json:"appState"`
		IDTokenPayload any    `json:"idTokenPayload"`
		TokenType      string `json:"tokenType"`
		State          string `json:"state"`
		IDToken        any    `json:"idToken"`
		Scope          string `json:"scope"`
		AccessToken    string `json:"accessToken"`
		ExpiresIn      int    `json:"expiresIn"`
		RefreshToken   any    `json:"refreshToken"`
	} `json:"auth-result"`
	Profile struct {
		GivenName     string    `json:"given_name"`
		Email         string    `json:"email"`
		Locale        string    `json:"locale"`
		Sub           string    `json:"sub"`
		Name          string    `json:"name"`
		Nickname      string    `json:"nickname"`
		EmailVerified bool      `json:"email_verified"`
		FamilyName    string    `json:"family_name"`
		UpdatedAt     time.Time `json:"updated_at"`
		Picture       string    `json:"picture"`
	} `json:"profile"`
}

func CreateNewUser() *User {
	return &User{}
}
