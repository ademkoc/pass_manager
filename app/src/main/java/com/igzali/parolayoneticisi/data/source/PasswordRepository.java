package com.igzali.parolayoneticisi.data.source;

import android.app.Application;

import com.igzali.parolayoneticisi.data.Password;
import com.igzali.parolayoneticisi.data.source.local.Database;
import com.igzali.parolayoneticisi.data.source.local.PasswordDao;
import com.igzali.parolayoneticisi.passwords.crypto.CipherWrapper;
import com.igzali.parolayoneticisi.passwords.crypto.KeyStoreWrapper;

import java.security.KeyPair;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

public class PasswordRepository {

    private PasswordDao mPasswordDao;
    private LiveData<List<Password>> mPasswordListLiveData;
    private KeyPair mKeyPair;
    private CipherWrapper mCipherWrapper;

    public PasswordRepository(Application application) {
        Database database = Database.getInstance(application);
        mPasswordDao = database.mPasswordDao();
        mPasswordListLiveData = mPasswordDao.getAllPasswords();

        initCrypto(application);
    }

    private void initCrypto(Application application) {
        KeyStoreWrapper keyStoreWrapper = new KeyStoreWrapper(application, "MASTER_KEY");
        mKeyPair = keyStoreWrapper.getAndroidKeyStoreAsymmetricKeyPair();

        mCipherWrapper = new CipherWrapper();
    }

    public void insert(Password password) {
        password.setCreatedDate(new Date(System.currentTimeMillis()));
        password.setPassword(encryptPassword(password.getPassword()));
        new Thread(() -> mPasswordDao.insert(password)).start();
    }

    public void update(Password password) {
        password.setUpdatedDate(new Date(System.currentTimeMillis()));
        password.setPassword(encryptPassword(password.getPassword()));
        new Thread(() -> mPasswordDao.update(password)).start();
    }

    public void delete(long id) {
        new Thread(() -> mPasswordDao.delete(id)).start();
    }

    public LiveData<List<Password>> getAllNotes() {
        return mPasswordListLiveData;
    }

    public String decryptedPassword(String passwordStr) {
        return mCipherWrapper.decrypt(passwordStr, mKeyPair.getPrivate());
    }

    public String encryptPassword(String plainPasswordText) {
        return mCipherWrapper.encrypt(plainPasswordText, mKeyPair.getPublic());
    }
}
