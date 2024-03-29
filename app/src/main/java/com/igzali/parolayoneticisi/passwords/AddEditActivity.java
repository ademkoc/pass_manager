package com.igzali.parolayoneticisi.passwords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.textfield.TextInputLayout;
import com.igzali.parolayoneticisi.R;
import com.igzali.parolayoneticisi.data.Password;
import com.igzali.parolayoneticisi.utils.IntentConsts;

public class AddEditActivity extends AppCompatActivity {

    private TextInputLayout mUsernameTextInputLayout;
    private TextInputLayout mEmailTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;
    private TextInputLayout mDescriptionTextInputLayout;
    private TextInputLayout mLabelTextInputLayout;
    private Password mPassword;
    //private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsernameTextInputLayout = findViewById(R.id.text_input_account);
        mEmailTextInputLayout = findViewById(R.id.text_input_email);
        mPasswordTextInputLayout = findViewById(R.id.text_input_password);
        mDescriptionTextInputLayout = findViewById(R.id.text_input_description);
        mLabelTextInputLayout = findViewById(R.id.text_input_label);

        //mCoordinatorLayout = findViewById(R.id.coordinator_add_edit);

        if (getIntent().getIntExtra(IntentConsts.KEY_ACTIVITY_REQUEST_CODE, IntentConsts.REQUEST_DEFAULT_PASSWORD_ACTION) == IntentConsts.REQUEST_EDIT_PASSWORD) {
            mPassword = getIntent().getParcelableExtra(IntentConsts.KEY_PASSWORD_EXTRA);
            fillFieldWithData(mPassword);
        }
    }

    private void fillFieldWithData(Password password) {
        mUsernameTextInputLayout.getEditText().setText(password.getUsername());
        mEmailTextInputLayout.getEditText().setText(password.getEmail());
        mLabelTextInputLayout.getEditText().setText(password.getLabel());
        mDescriptionTextInputLayout.getEditText().setText(password.getDescription());
        //mPasswordTextInputLayout.getEditText().setText(password.getPassword());

        mPasswordTextInputLayout.setHint("New Password");
        mPasswordTextInputLayout.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_complete:
                sendPassword(); return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendPassword() {
        if (!isRequiredFieldsValid()) return;

        String label = mLabelTextInputLayout.getEditText().getText().toString();
        String email = mEmailTextInputLayout.getEditText().getText().toString();
        String password = mPasswordTextInputLayout.getEditText().getText().toString();
        String description = mDescriptionTextInputLayout.getEditText().getText().toString();
        String username = mUsernameTextInputLayout.getEditText().getText().toString();

        Password pass = new Password(label,email,password,username,description);
        if (getIntent().getIntExtra(IntentConsts.KEY_ACTIVITY_REQUEST_CODE, IntentConsts.REQUEST_DEFAULT_PASSWORD_ACTION) == IntentConsts.REQUEST_EDIT_PASSWORD) {
            pass.setId(mPassword.getId());
        }
        Intent data = new Intent();
        data.putExtra(IntentConsts.KEY_PASSWORD_EXTRA, pass);

        setResult(RESULT_OK, data);

        finish();
    }

    private boolean isRequiredFieldsValid() {

        boolean hasError = false;

        if (mLabelTextInputLayout.getEditText().getText().toString().isEmpty()) {
            mLabelTextInputLayout.setError("Boş bırakılamaz");
            hasError = true;
        } else {
            mLabelTextInputLayout.setErrorEnabled(false);
        }

        if (mEmailTextInputLayout.getEditText().getText().toString().isEmpty()) {
            mEmailTextInputLayout.setError("Boş bırakılamaz");
            hasError = true;
        } else {
            mEmailTextInputLayout.setErrorEnabled(false);
        }

        if (mPasswordTextInputLayout.getEditText().getText().toString().isEmpty()) {
            mPasswordTextInputLayout.setError("Boş bırakılamaz");
            hasError = true;
        } else {
            mPasswordTextInputLayout.setErrorEnabled(false);
        }

        if (hasError) return false;
        return true;
    }
}
