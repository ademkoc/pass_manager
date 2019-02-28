package com.igzali.parolayoneticisi.data.source;

import android.app.Application;

import com.igzali.parolayoneticisi.data.Password;
import com.igzali.parolayoneticisi.data.source.local.Database;
import com.igzali.parolayoneticisi.data.source.local.PasswordDao;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

public class PasswordRepository {

    private PasswordDao mPasswordDao;
    private LiveData<List<Password>> mPasswordListLiveData;

    public PasswordRepository(Application application) {
        Database database = Database.getInstance(application);
        mPasswordDao = database.mPasswordDao();
        mPasswordListLiveData = mPasswordDao.getAllPasswords();
    }

    public void insert(final Password password) {
        password.setCreatedDate(new Date(System.currentTimeMillis()));
        new Thread(() -> mPasswordDao.insert(password)).start();
    }

    public void update(Password password) {
        password.setUpdatedDate(new Date(System.currentTimeMillis()));
        new Thread(() -> mPasswordDao.update(password)).start();
    }

    public void delete(long id) {
        new Thread(() -> mPasswordDao.delete(id)).start();
    }

    public LiveData<List<Password>> getAllNotes() {
        return mPasswordListLiveData;
    }

}
