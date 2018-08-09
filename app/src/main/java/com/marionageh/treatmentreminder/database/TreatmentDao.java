package com.marionageh.treatmentreminder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.marionageh.treatmentreminder.models.Treatment;

import java.util.List;
@Dao
public interface TreatmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Treatment treatment);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    void update(Treatment treatment);

    @Query("SELECT * FROM Treatment")
    List<Treatment> getAll();

    @Delete
    void delete(Treatment Treatment);

    @Query("DELETE FROM Treatment WHERE id = :userId")
    abstract void deleteByUserId(long userId);

    @Query("SELECT * FROM Treatment WHERE id = :TreatmanetID")
    abstract Treatment SelectTreatmentId(long TreatmanetID);



}
