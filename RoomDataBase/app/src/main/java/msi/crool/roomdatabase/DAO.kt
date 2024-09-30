package msi.crool.roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {
    @Insert
    suspend fun insert(employeEntity: EmployeEntity)

    @Update
    suspend fun update(employeEntity: EmployeEntity)

    @Delete
    suspend fun delete(employeEntity: EmployeEntity)

    @Query("SELECT * FROM `Employee`")
    fun fetchAllEmployees(): Flow<List<EmployeEntity>>

    @Query("Select * from `Employee` where id=:id LIMIT 1")
    fun fetchEmployeeByID(id: Int): Flow<EmployeEntity>

}
