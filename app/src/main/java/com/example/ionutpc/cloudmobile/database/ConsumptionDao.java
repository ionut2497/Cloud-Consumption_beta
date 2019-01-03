package com.example.ionutpc.cloudmobile.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
@Dao
public interface ConsumptionDao {

    @Query("SELECT * FROM consumptions")
    LiveData<List<ConsumptionEntry>> loadAllConsumptions();
    @Query("SELECT * FROM consumptions")
    List<ConsumptionEntry> loadAllConsumptionsList();
    @Query("SELECT * FROM consumptions  ORDER BY id DESC LIMIT 1")
    ConsumptionEntry loadAllConsumptionsLast();
    @Query("SELECT * FROM consumptions  ORDER BY id DESC LIMIT 1")
    LiveData<ConsumptionEntry> loadAllConsumptionsLastLive();

    @Insert
    void insertConsumption(ConsumptionEntry consumptionEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateConsumption(ConsumptionEntry consumptionEntry);

    @Delete
    void deleteConsumption(ConsumptionEntry consumptionEntry);

    @Query("DELETE FROM consumptions")
    void deleteAllConsumption();


    @Query("SELECT * FROM consumptions WHERE id = :id")
    LiveData<ConsumptionEntry> loadConsumptionByID(int id);
}
