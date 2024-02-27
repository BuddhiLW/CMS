package db

import (
	// _ "gorm.io/driver/mysql"

	"github.com/BuddhiLW/go-CMS-backend/auth/util"
	_ "github.com/go-sql-driver/mysql"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

func ConnectDB() *gorm.DB {
	dns := "admin:pass@tcp(localhost:3001)/auth?charset=utf8mb4&parseTime=True&loc=Local"
	db, err := gorm.Open(mysql.Open(dns), &gorm.Config{})
	if err != nil {
		panic(err)
	}
	return db
}

func Migration(db *gorm.DB) {
	db.AutoMigrate(&util.User{}, &util.Profile{}, &util.AuthResult{})
}
