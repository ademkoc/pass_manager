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

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.content.ClipboardManager;
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

    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String PASSWORD_CLIP_LABEL = "pass_clip";
    private final static String KEY_ACTION_MODE = "is_action_mode_open";
    private PasswordViewModel mPasswordViewModel;
    private PasswordAdapter mPasswordAdapter;
    private CoordinatorLayout mCoordinatorLayout;
    private ActionMode mActionMode;
    private RecyclerViewActionModeCallBack mActionModeCallBack;
    private ClipboardManager mClipboardService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoordinatorLayout = findViewById(R.id.coordinator_main);

        initRecyclerView(findViewById(R.id.recycler_view));
        setupContextualActionBar();
        mClipboardService = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        if (savedInstanceState != null) {
            SelectionTracker<Password> tracker = mPasswordAdapter.getSelectionTracker();
            tracker.onRestoreInstanceState(savedInstanceState);
            boolean wasActionModeOpen = savedInstanceState.getBoolean(KEY_ACTION_MODE);
            if (wasActionModeOpen) {
                startContextualActionBar(tracker);
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
        mPasswordAdapter.setOnCopyButtonClickListener((Password password) -> {
            String decryptedPass = mPasswordViewModel.getDecryptedPassword(password.getPassword());
            ClipData clipData = ClipData.newPlainText(PASSWORD_CLIP_LABEL, decryptedPass);
            mClipboardService.setPrimaryClip(clipData);
            Snackbar.make(mCoordinatorLayout, String.format("%s şifreniz kopyalandı",
                    password.getLabel()), Snackbar.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(mPasswordAdapter);

        mPasswordAdapter.setSelectionTracker(initSelectionTracker(recyclerView));
    }

    private void setupContextualActionBar() {
        SelectionTracker<Password> tracker = mPasswordAdapter.getSelectionTracker();
        mActionModeCallBack = new RecyclerViewActionModeCallBack(tracker);
        mActionModeCallBack.setActionItemClickListener((actionMode, item) -> {
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    menuItemDeletePassword(tracker);
                    actionMode.finish();
                    return true;
                case R.id.menu_edit:
                    menuItemEditPassword(tracker);
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
                } else if (mActionMode != null){
                    mActionMode.finish();
                    mActionMode = null;
                }
            }
        });
    }

    private void menuItemDeletePassword(SelectionTracker<Password> tracker) {
        Iterator<Password> iterator = tracker.getSelection().iterator();
        while (iterator.hasNext()) {
            Password password = iterator.next();
            mPasswordViewModel.delete(password.getId());
        }
    }

    private void menuItemEditPassword(SelectionTracker<Password> tracker) {
        Iterator<Password> iterator = tracker.getSelection().iterator();
        Password password = iterator.next();
        password.setPassword(mPasswordViewModel.getDecryptedPassword(password.getPassword()));
        Intent intent = new Intent(getApplicationContext(), AddEditActivity.class);
        intent.putExtra(IntentConsts.KEY_PASSWORD_EXTRA, password);
        startActivityForResult(intent, IntentConsts.REQUEST_EDIT_PASSWORD);
    }

    private void startContextualActionBar(SelectionTracker tracker) {
        if (mActionMode == null) {
            mActionMode = startSupportActionMode(mActionModeCallBack);
        }
        mActionMode.setTitle(String.format("Selected item count: %d", tracker.getSelection().size()));
        mActionModeCallBack.onMultiItemSelected(mActionMode);
    }

    private SelectionTracker<Password> initSelectionTracker(RecyclerView recyclerView) {
        SelectionTracker<Password> tracker = new SelectionTracker.Builder<>(
                getString(R.string.app_name),
                recyclerView,
                new PasswordItemKeyProvider(recyclerView),
                new PasswordItemLookup(recyclerView),
                StorageStrategy.createParcelableStorage(Password.class)
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
        ).build();

        return tracker;
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
        if (resultCode == RESULT_OK) {
            savePassword(requestCode, data);
        }
    }

    private void savePassword(int requestCode, Intent data) {
        if (data == null) {
            Snackbar.make(mCoordinatorLayout, "Parola kaydedilemedi", Snackbar.LENGTH_SHORT).show();
            Log.d(TAG, "savePassword intent data is coming null");
            return;
        }
        Password password = data.getParcelableExtra(IntentConsts.KEY_PASSWORD_EXTRA);
        if (requestCode == IntentConsts.REQUEST_ADD_PASSWORD)
            mPasswordViewModel.insert(password);
        else if (requestCode == IntentConsts.REQUEST_EDIT_PASSWORD)
            mPasswordViewModel.update(password);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SelectionTracker<Password> tracker = mPasswordAdapter.getSelectionTracker();
        tracker.onSaveInstanceState(outState);
        outState.putBoolean(KEY_ACTION_MODE, mActionMode != null);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra(IntentConsts.KEY_ACTIVITY_REQUEST_CODE, requestCode);
        super.startActivityForResult(intent, requestCode);
    }
}
