package msi.crool.roomdemo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [dataClass::class], version = 1)
abstract  class EmpDB: RoomDatabase() {
    abstract  fun employeDao():EmployeDAO

    companion object{
        @Volatile
        private var INSTANCE:EmpDB?=null

        fun getInstance(context: Context):EmpDB
        {
            synchronized(this)
            {
                var instance= INSTANCE
                if(instance==null)
                {
                    instance= Room.databaseBuilder(
                        context.applicationContext,
                        EmpDB::class.java,
                        "Student Data"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE=instance
                }
                return instance
            }
        }
    }

}