package db

import "github.com/BuddhiLW/go-CMS-backend/auth/util"

// Create
func CreateUser(user *util.User) {
	// Connect to the database
	conn := ConnectDB()

	// Create a new user, and unmarshall the JSON data into it
	// user := util.CreateNewUser()
	// user.Unmashall(jsonData)

	// Create the user in the database
	conn.Create(user)
	// if err.Error != nil {
	// 	return err.Error
	// }
}

// Read
func GetUser(sub string) *util.User {
	// Connect to the database
	conn := ConnectDB()

	// Create a new user
	user := util.CreateNewUser()

	// Get the user from the database
	conn.Preload("AuthResult").Preload("Profile").Model(util.User{}).Where("sub = ?", sub).First(&user)
	// (value interface{}) .First(&user, "sub = ?", sub)

	return user
}

// Update
func UpdateUser(sub string, newUser *util.User) {
	// Connect to the database
	conn := ConnectDB()

	conn.Model(&util.User{}).Where("sub = ?", sub).Updates(newUser)
}

// Delete
func DeleteUser(sub string) {
	// Connect to the database
	conn := ConnectDB()

	// Create a new user
	user := util.CreateNewUser()

	// Get the user from the database
	conn.First(&user, "sub = ?", sub)

	// Delete the user from the database
	conn.Delete(&user)
}
