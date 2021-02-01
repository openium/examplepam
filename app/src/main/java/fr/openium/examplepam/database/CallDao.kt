package fr.openium.examplepam.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.openium.examplepam.model.Call

@Dao
interface CallDao {
    @Query("SELECT * FROM call ORDER BY startDate DESC")
    fun getCalls(): LiveData<List<Call>>

    @Insert
    suspend fun insertAll(vararg calls: Call?)
}