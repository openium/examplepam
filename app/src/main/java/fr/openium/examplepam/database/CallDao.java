package fr.openium.examplepam.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import fr.openium.examplepam.model.Call;

@Dao
public interface CallDao {
    @Query("SELECT * FROM call ORDER BY startDate DESC")
    List<Call> getAll();

    @Query("SELECT * FROM call ORDER BY startDate DESC")
    LiveData<List<Call>> getAllSync();

    @Insert
    void insertAll(Call... calls);
}
