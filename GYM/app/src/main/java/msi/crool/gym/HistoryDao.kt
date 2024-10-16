package msi.crool.gym

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(historyEntity:HistoryEntity)

    @Query("Select * from `History-table`")
    fun fetchAllDates(): Flow<List<HistoryEntity>>
}