package util

import (
	"encoding/json"
)

// Mashall: User [Go] -> JSON
// func (u *User) Mashall() string {

// }

// Unmashall: JSON -> User [Go]
func (u *User) Unmashall(jsonByteData []byte) {
	json.Unmarshal(jsonByteData, &u)
}
