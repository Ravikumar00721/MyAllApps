package msi.crool.gym

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "History-table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String
)