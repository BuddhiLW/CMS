package util

import (
	"time"

	"gorm.io/gorm"
)

type AuthResult struct {
	AppState       string `json:"appState"`
	IDTokenPayload string `json:"idTokenPayload"`
	TokenType      string `json:"tokenType"`
	State          string `json:"state"`
	IDToken        string `json:"idToken"`
	Scope          string `json:"scope"`
	AccessToken    string `json:"accessToken" gorm:"primaryKey"`
	ExpiresIn      int    `json:"expiresIn"`
	RefreshToken   string `json:"refreshToken"`
	gorm.Model
}

type Profile struct {
	GivenName     string    `json:"given_name"`
	Email         string    `json:"email"`
	Locale        string    `json:"locale"`
	Sub           string    `json:"sub" gorm:"primaryKey"`
	Name          string    `json:"name"`
	Nickname      string    `json:"nickname"`
	EmailVerified bool      `json:"email_verified"`
	FamilyName    string    `json:"family_name"`
	UpdatedAt     time.Time `json:"updated_at"`
	Picture       string    `json:"picture"`
	gorm.Model
}

type User struct {
	Sub         string       `json:"sub"`
	Profile     Profile      `gorm:"foreignKey:Sub"`
	AuthResults []AuthResult `gorm:"foreignKey:AccessToken;references:Sub"`
	gorm.Model
}

type IncJson struct {
	Profile    Profile    `json:"profile"`
	AuthResult AuthResult `json:"authResult"`
}

// ID         uint      `json:"id" gorm:"primaryKey"`
// CreatedAt   time.Time `json:"created_at"`
// UpdatedAt   time.Time `json:"updated_at"`
// DeletedAt   time.Time `json:"deleted_at"`
// Scope       string `json:"scope"`
// AccessToken string `json:"accessToken"`
// State       string `json:"state"`

func CreateNewUser() *User {
	return &User{}
}

func CreateNewProfile() *Profile {
	return &Profile{}
}

func CreateNewAuthResult() *AuthResult {
	return &AuthResult{}
}
