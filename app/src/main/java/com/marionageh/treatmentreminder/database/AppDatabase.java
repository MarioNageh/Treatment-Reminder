package com.marionageh.treatmentreminder.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.marionageh.treatmentreminder.models.Treatment;

@Database(entities = {Treatment.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

      //Database Name
    private static final String DATABASE_NAME = "app-database.db";
    //Class Instatnt
    private static AppDatabase appDatabaseInstance;

    //Create Database With check if the Instatnt not null
    public static AppDatabase getInstance(Context context) {
        if (appDatabaseInstance == null) {
            //Create database
            appDatabaseInstance = Room.databaseBuilder(context,
                    AppDatabase.class, DATABASE_NAME).build();
        }
        return appDatabaseInstance;
    }

    public abstract TreatmentDao getTreatmentDao();


}