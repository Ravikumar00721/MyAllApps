package msi.crool.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EmployeEntity::class], version = 1)
abstract class EmpDataBase:RoomDatabase() {
    abstract fun employeeDao():DAO

    companion object{
        @Volatile
        private var INSTANCE:EmpDataBase?=null

        fun getInstance(context: Context):EmpDataBase{
            var instance= INSTANCE

            if(instance==null)
            {
                instance=Room.databaseBuilder(
                    context.applicationContext,
                    EmpDataBase::class.java,
                    "EMP_DB"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE=instance
            }
            return instance
        }
    }
}