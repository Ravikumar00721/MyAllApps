package msi.crool.happyplaces.Databases

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import msi.crool.happyplaces.Models.HappyPlacesModel

// TODO (Step 3 : Creating a database handler class for local database operations like creating a table and inserting a Happy Place Detail.)
// START
//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1 // Database version
        private const val DATABASE_NAME = "HappyPlacesDatabase" // Database name
        private const val TABLE_HAPPY_PLACE = "HappyPlacesTable" // Table Name

        //All the Columns names
        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_HAPPY_PLACE_TABLE = ("CREATE TABLE " + TABLE_HAPPY_PLACE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(CREATE_HAPPY_PLACE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_HAPPY_PLACE")
        onCreate(db)
    }

    fun updateHappyPlace(happyPlace: HappyPlacesModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues().apply {
            put(KEY_TITLE, happyPlace.title)
            put(KEY_IMAGE, happyPlace.Image)
            put(KEY_DESCRIPTION, happyPlace.Description)
            put(KEY_DATE, happyPlace.Date)
            put(KEY_LOCATION, happyPlace.Location)
            put(KEY_LATITUDE, happyPlace.Lattitude)
            put(KEY_LONGITUDE, happyPlace.Longitude)
        }

        // Define the where clause and the corresponding argument
        val whereClause = "$KEY_ID = ?"
        val whereArgs = arrayOf(happyPlace.id.toString())

        // Update the row and return the number of affected rows
        val success = db.update(TABLE_HAPPY_PLACE, contentValues, whereClause, whereArgs)

        db.close() // Closing database connection
        return success
    }


    // TODO (Step 4 : After Creating a database handler class. Let us create an function to insert a happy place detail to respective table.)
    // START
    /**
     * Function to insert a Happy Place details to SQLite Database.
     */
    fun addHappyPlace(happyPlace:HappyPlacesModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, happyPlace.title) // HappyPlaceModelClass TITLE
        contentValues.put(KEY_IMAGE, happyPlace.Image) // HappyPlaceModelClass IMAGE
        contentValues.put(
            KEY_DESCRIPTION,
            happyPlace.Description
        ) // HappyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_DATE, happyPlace.Date) // HappyPlaceModelClass DATE
        contentValues.put(KEY_LOCATION, happyPlace.Location) // HappyPlaceModelClass LOCATION
        contentValues.put(KEY_LATITUDE, happyPlace.Lattitude) // HappyPlaceModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, happyPlace.Longitude) // HappyPlaceModelClass LONGITUDE

        // Inserting Row
        val result = db.insert(TABLE_HAPPY_PLACE, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return result
    }
    // END

    fun deleteHappyPlace(happyPlace: HappyPlacesModel):Int
    {
        val db=this.writableDatabase
        val success=db.delete(TABLE_HAPPY_PLACE, KEY_ID+ "=" + happyPlace.id,null)
        db.close()
        return success
    }

    @SuppressLint("Recycle", "Range")
    fun getHappyPlaceList():ArrayList<HappyPlacesModel>
    {
        val happyPlace=ArrayList<HappyPlacesModel>()
        val selectQuery="Select * from $TABLE_HAPPY_PLACE"
        val db=this.readableDatabase

        try {
           val cursor:Cursor=db.rawQuery(selectQuery,null)
            if(cursor.moveToFirst())
            {
                do {
                         val place=HappyPlacesModel(
                             cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                             cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                             cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                             cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                             cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                             cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                             cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                             cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))
                         )
                    happyPlace.add(place)
                }while (cursor.moveToNext())
            }
            cursor.close()
        }catch (e:Exception)
        {
            db.execSQL(selectQuery)
            return  ArrayList()
        }
        return happyPlace
    }
}
// END