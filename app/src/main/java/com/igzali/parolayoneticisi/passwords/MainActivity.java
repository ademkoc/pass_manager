package com.igzali.parolayoneticisi.passwords;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.google.android.material.snackbar.Snackbar;
import com.igzali.parolayoneticisi.R;
import com.igzali.parolayoneticisi.data.Password;
import com.igzali.parolayoneticisi.passwords.selection.PasswordItemKeyProvider;
import com.igzali.parolayoneticisi.passwords.selection.PasswordItemLookup;
import com.igzali.parolayoneticisi.utils.IntentConsts;

import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Observer<List<Password>> {

    private PasswordViewModel mPasswordViewModel;
    private PasswordAdapter mPasswordAdapter;
    private CoordinatorLayout mCoordinatorLayout;
    private ActionMode mActionMode;
    private SelectionTracker<Password> mSelectionTracker;
    private RecyclerViewActionModeCallBack mActionModeCallBack;
    private final static String TAG = MainActivity.class.getSimpleName();
    private static final String KEY_ACTION_MODE = "is_action_mode_open";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoordinatorLayout = findViewById(R.id.coordinator_main);

        initRecyclerView(findViewById(R.id.recycler_view));
        setupContextualActionBar(mSelectionTracker);

        if (savedInstanceState != null) {
            mSelectionTracker.onRestoreInstanceState(savedInstanceState);
            boolean wasActionModeOpen = savedInstanceState.getBoolean(KEY_ACTION_MODE);
            if (wasActionModeOpen) {
                startContextualActionBar(mSelectionTracker);
            }
        }

        mPasswordViewModel = ViewModelProviders.of(this).get(PasswordViewModel.class);
        mPasswordViewModel.getAllPasswords().observe(this, this);
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        recyclerView.setLayoutAnimation(animation);

        mPasswordAdapter = new PasswordAdapter();
        mPasswordAdapter.setHasStableIds(true);
        recyclerView.setAdapter(mPasswordAdapter);

        initSelectionTracker(recyclerView);

        mPasswordAdapter.setSelectionTracker(mSelectionTracker);
    }

    private void setupContextualActionBar(SelectionTracker<Password> tracker) {
        mActionModeCallBack = new RecyclerViewActionModeCallBack(tracker);
        mActionModeCallBack.setActionItemClickListener((actionMode, item) -> {
            switch (item.getItemId()) {
                case R.id.menu_delete:

                    actionMode.finish();
                    return true;
                case R.id.menu_edit:

                    actionMode.finish();
                    return true;
                default:
                    return false;
            }
        });

        tracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();
                if (tracker.hasSelection()) {
                    startContextualActionBar(tracker);
                } else {
                    mActionMode.finish();
                    mActionMode = null;
                }
            }
        });
    }

    private void startContextualActionBar(SelectionTracker tracker) {
        if (mActionMode == null) {
            mActionMode = startSupportActionMode(mActionModeCallBack);
        }
        mActionMode.setTitle(String.format("Selected item count: %d", tracker.getSelection().size()));
        mActionModeCallBack.onMultiItemSelected(mActionMode);
    }

    private void initSelectionTracker(RecyclerView recyclerView) {
        if (mSelectionTracker != null)
            return;

        mSelectionTracker = new SelectionTracker.Builder<>(
                getString(R.string.app_name),
                recyclerView,
                new PasswordItemKeyProvider(recyclerView),
                new PasswordItemLookup(recyclerView),
                StorageStrategy.createParcelableStorage(Password.class)
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
        ).build();

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mSelectionTracker.onSaveInstanceState(outState);
        outState.putBoolean(KEY_ACTION_MODE, mActionMode != null);
    }
}
