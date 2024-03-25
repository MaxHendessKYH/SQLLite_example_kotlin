package com.example.sqllite_example

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context?)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "user_db"
        const val TABLE_USERS = "users"
        const val KEY_ID ="id"
        const val KEY_NAME = "name"
        const val KEY_SCORE = "score"
        const val KEY_ISHUMAN = "is_human"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // no bools in sql lite apparently
        val tableQuerry = "CREATE TABLE $TABLE_USERS($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_NAME TEXT, $KEY_SCORE DOUBLE, $KEY_ISHUMAN BOOL)"
        db?.execSQL(tableQuerry)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }
    fun updateUser(user: User){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, user.name)
            put(KEY_SCORE, user.score)
            put(KEY_ISHUMAN, user.isHuman)
        }
        db.update(TABLE_USERS,values, "$KEY_ID == ?", arrayOf(user.id.toString()))
        db.close()
    }
fun deleteUser(user:User):Boolean{
    val db = this.writableDatabase
    val query = "DELETE FROM $TABLE_USERS WHERE $KEY_ID = ${user.id}"
    val cursor: Cursor = db.rawQuery(query, null)
    var result = cursor.moveToFirst()
    cursor.close()
    return  result
}
fun addUser(userObject:User):Boolean{
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(KEY_NAME, userObject.name)
        values.put(KEY_SCORE, userObject.score)
        values.put(KEY_ISHUMAN, userObject.isHuman)
        val insert = db.insert(TABLE_USERS,null, values)
        db.close()
        return insert != -1L
    }
fun getAllUsers(): List<User>{
        val users = ArrayList <User>()
        val query = "SELECT * FROM $TABLE_USERS"
        val db= this.readableDatabase
        val cursor: Cursor = db.rawQuery(query, null)

        if(cursor.moveToFirst()) {
            do {
                val userID = cursor.getInt(0)
                val userName = cursor.getString(1)
                val userScore = cursor.getDouble(2)
                val isHuman: Boolean = cursor.getInt(3) == 1
                val user = User(userID, userName, userScore, isHuman)
                users.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return users
    }
}