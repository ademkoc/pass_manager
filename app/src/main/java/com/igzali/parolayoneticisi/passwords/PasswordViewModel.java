package com.igzali.parolayoneticisi.passwords;

import android.app.Application;

import com.igzali.parolayoneticisi.data.Password;
import com.igzali.parolayoneticisi.data.source.PasswordRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PasswordViewModel extends AndroidViewModel {

    private PasswordRepository mPasswordRepository;
    private LiveData<List<Password>> mListLiveData;

    public PasswordViewModel(@NonNull Application application) {
        super(application);
        mPasswordRepository = new PasswordRepository(application);
        mListLiveData = mPasswordRepository.getAllNotes();
    }

    public void insert(Password password) {
        mPasswordRepository.insert(password);
    }

    public void update(Password password) {
        mPasswordRepository.update(password);
    }

    public void delete(long id) {
        mPasswordRepository.delete(id);
    }

    public LiveData<List<Password>> getAllPasswords() {
        return mListLiveData;
    }
}
