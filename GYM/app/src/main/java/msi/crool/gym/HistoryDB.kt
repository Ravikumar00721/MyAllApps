package msi.crool.gym

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1)
abstract class HistoryDB : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryDB? = null

        fun getInstance(context: Context): HistoryDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDB::class.java,
                    "history_database" // Changed the database name
                ).fallbackToDestructiveMigration() // Consider removing this for production
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
