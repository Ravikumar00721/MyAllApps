package msi.crool.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DataBAseNAme = "mydb"
val tablename = "users"
val col_name = "name"
val col_age = "age"
val col_id = "ID"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DataBAseNAme, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $tablename ($col_id INTEGER PRIMARY KEY AUTOINCREMENT, $col_name VARCHAR(256), $col_age INTEGER)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade
    }

    fun insertData(user: User) {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(col_name, user.name)
            put(col_age, user.age)
        }
        val result = db.insert(tablename, null, cv)
        if (result == -1L) {
            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("Range")
    fun readData(): MutableList<User> {
        val list = ArrayList<User>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $tablename"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val user = User().apply {
                    id = result.getInt(result.getColumnIndex(col_id))
                    name = result.getString(result.getColumnIndex(col_name))
                    age = result.getInt(result.getColumnIndex(col_age))
                }
                list.add(user)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun deleteData(id: Int) {
        val db = writableDatabase
        db.delete(tablename, "$col_id=?", arrayOf(id.toString()))
        db.close()
    }

    @SuppressLint("Range")
    fun updateData(id: Int) {
        val db = writableDatabase

        // Retrieve current age
        val cursor = db.query(
            tablename,
            arrayOf(col_age),
            "$col_id = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            val currentAge = cursor.getInt(cursor.getColumnIndex(col_age))
            val newAge = currentAge + 1

            // Update the age
            val values = ContentValues().apply {
                put(col_age, newAge)
            }
            val rowsAffected = db.update(tablename, values, "$col_id = ?", arrayOf(id.toString()))

            if (rowsAffected > 0) {
                Toast.makeText(context, "Record Updated", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Failed to Update Record", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Record Not Found", Toast.LENGTH_LONG).show()
        }

        cursor.close()
        db.close()
    }
}
