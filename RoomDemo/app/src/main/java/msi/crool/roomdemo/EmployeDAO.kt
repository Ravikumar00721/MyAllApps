package msi.crool.roomdemo

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface EmployeDAO {

    @Insert
    suspend fun insert(employeEntity:dataClass)

    @Update
    suspend fun update(employeEntity: dataClass)

    @Delete
    suspend fun delete(employeEntity: dataClass)

    @Query("Select * from  `student data`")
    fun fetchAllEmployees():Flow<List<dataClass>>

    @Query("Select * from  `student data` where id=:id")
    fun fetchAllEmployeesbyID(id:Int):Flow<List<dataClass>>
}