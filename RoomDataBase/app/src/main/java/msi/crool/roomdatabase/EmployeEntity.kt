package msi.crool.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Employee")
data class EmployeEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    @ColumnInfo(name = "Name")
    val name:String="",
    @ColumnInfo(name="EmailId")
    val EmailID:String=""
)
