package com.igzali.parolayoneticisi.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.igzali.parolayoneticisi.PasswordAdapter;
import com.igzali.parolayoneticisi.PasswordViewModel;
import com.igzali.parolayoneticisi.R;
import com.igzali.parolayoneticisi.entities.Password;
import com.igzali.parolayoneticisi.utils.IntentConsts;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Observer<List<Password>> {

    private PasswordViewModel mPasswordViewModel;
    private PasswordAdapter mPasswordAdapter;
    private CoordinatorLayout mCoordinatorLayout;
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoordinatorLayout = findViewById(R.id.coordinator_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,
                false));
        recyclerView.setHasFixedSize(true);

        mPasswordAdapter = new PasswordAdapter();
        recyclerView.setAdapter(mPasswordAdapter);

        mPasswordViewModel = ViewModelProviders.of(this).get(PasswordViewModel.class);
        mPasswordViewModel.getAllPasswords().observe(this, this);
    }


    @Override
    public void onChanged(List<Password> passwords) {
        mPasswordAdapter.submitList(passwords);
    }

    public void fabOnClick(View view) {
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
        startActivityForResult(intent, IntentConsts.REQUEST_ADD_PASSWORD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentConsts.REQUEST_ADD_PASSWORD && resultCode == RESULT_OK) {
            savePassword(data);
        }
    }

    private void savePassword(Intent data) {
        if (data == null) {
            Snackbar.make(mCoordinatorLayout, "Parola kaydedilemedi", Snackbar.LENGTH_SHORT).show();
            Log.d(TAG, "savePassword intent data is coming null");
            return;
        }
        Password password = data.getParcelableExtra(IntentConsts.PASSWORD_EXTRA_NAME);
        mPasswordViewModel.insert(password);
    }
}
