package com.igzali.parolayoneticisi.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
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
import com.igzali.parolayoneticisi.adapter.PasswordAdapter;
import com.igzali.parolayoneticisi.PasswordViewModel;
import com.igzali.parolayoneticisi.R;
import com.igzali.parolayoneticisi.adapter.PasswordItemLookup;
import com.igzali.parolayoneticisi.entities.Password;
import com.igzali.parolayoneticisi.utils.IntentConsts;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Observer<List<Password>> {

    private PasswordViewModel mPasswordViewModel;
    private PasswordAdapter mPasswordAdapter;
    private CoordinatorLayout mCoordinatorLayout;
    private ActionMode mActionMode;
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoordinatorLayout = findViewById(R.id.coordinator_main);

        initializeRecyclerView(findViewById(R.id.recycler_view));

        mPasswordViewModel = ViewModelProviders.of(this).get(PasswordViewModel.class);
        mPasswordViewModel.getAllPasswords().observe(this, this);
    }

    private void initializeRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);

        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        recyclerView.setLayoutAnimation(animation);

        mPasswordAdapter = new PasswordAdapter();
        mPasswordAdapter.setHasStableIds(true);
        recyclerView.setAdapter(mPasswordAdapter);

        SelectionTracker<Long> selectionTracker = new SelectionTracker.Builder<>(
                getString(R.string.app_name),
                recyclerView,
                new StableIdKeyProvider(recyclerView),
                new PasswordItemLookup(recyclerView),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
        ).build();

        mPasswordAdapter.setSelectionTracker(selectionTracker);

        setupContextualActionBar(selectionTracker);
    }

    private void setupContextualActionBar(SelectionTracker selectionTracker) {
        RecyclerViewActionModeCallBack callback = new RecyclerViewActionModeCallBack(selectionTracker);
        callback.setActionItemClickListener((actionMode, item) -> {
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

        selectionTracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();
                if (selectionTracker.hasSelection()) {
                    if (mActionMode == null) {
                        mActionMode = startSupportActionMode(callback);
                    }
                    mActionMode.setTitle(String.format("Selected item count: %d", selectionTracker.getSelection().size()));
                    callback.onMultiItemSelected(mActionMode);
                } else {
                    mActionMode.finish();
                    mActionMode = null;
                }
            }
        });
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
