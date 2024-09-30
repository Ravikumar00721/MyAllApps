package msi.crool.roomdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Student Data")
class dataClass (
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    @ColumnInfo(name = "Name")
    val name:String="",
    @ColumnInfo(name="email-id")
    val email:String=""
)