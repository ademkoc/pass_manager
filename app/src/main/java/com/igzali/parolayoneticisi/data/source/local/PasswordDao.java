package com.igzali.parolayoneticisi.data.source.local;

import com.igzali.parolayoneticisi.data.Password;
import com.igzali.parolayoneticisi.utils.DatabaseConsts;

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

    @Query("DELETE FROM "+ DatabaseConsts.PASSWORDS_TABLE_NAME + " WHERE id = :id")
    void delete(long id);

    @Query("SELECT * FROM " + DatabaseConsts.PASSWORDS_TABLE_NAME)
    LiveData<List<Password>> getAllPasswords();
}
