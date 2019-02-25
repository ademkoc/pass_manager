package com.igzali.parolayoneticisi.daos;

import com.igzali.parolayoneticisi.entities.Password;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PasswordDao {

    @Insert
    void insert(Password password);

    @Update
    void update(Password password);

    @Delete
    void delete(Password password);

    @Query("SELECT * FROM passwords")
    LiveData<List<Password>> getAllPasswords();
}
