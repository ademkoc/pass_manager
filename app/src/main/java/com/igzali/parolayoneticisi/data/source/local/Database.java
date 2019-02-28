package com.igzali.parolayoneticisi.data.source.local;

import android.content.Context;

import com.igzali.parolayoneticisi.data.Password;
import com.igzali.parolayoneticisi.utils.DatabaseConsts;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@androidx.room.Database(entities = {Password.class}, version = 1)
@TypeConverters({DatabaseTypeConverters.class})
public abstract class Database extends RoomDatabase {

    private static Database sDatabaseInstance;
    public abstract PasswordDao mPasswordDao();

    public static synchronized Database getInstance(Context context) {
        if (sDatabaseInstance == null) {
            sDatabaseInstance = Room.databaseBuilder(context, Database.class,
                    DatabaseConsts.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return sDatabaseInstance;
    }

}
