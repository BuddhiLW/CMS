package util

import "time"

type AuthResult struct {
	AppState       string `json:"appState"`
	IDTokenPayload string `json:"idTokenPayload"`
	TokenType      string `json:"tokenType"`
	State          string `json:"state"`
	IDToken        string `json:"idToken"`
	Scope          string `json:"scope"`
	AccessToken    string `json:"accessToken"`
	ExpiresIn      int    `json:"expiresIn"`
	RefreshToken   string `json:"refreshToken"`
}

type Profile struct {
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
}

type User struct {
	ID         uint      `json:"id" gorm:"primaryKey"`
	CreatedAt  time.Time `json:"created_at"`
	UpdatedAt  time.Time `json:"updated_at"`
	DeletedAt  time.Time `json:"deleted_at"`
	AuthResult AuthResult
	Profile    Profile
}

func CreateNewUser() *User {
	return &User{}
}
