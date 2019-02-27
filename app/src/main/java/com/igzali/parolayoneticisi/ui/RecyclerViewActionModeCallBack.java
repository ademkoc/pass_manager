package com.igzali.parolayoneticisi.ui;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.igzali.parolayoneticisi.R;

import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.selection.SelectionTracker;

public class RecyclerViewActionModeCallBack implements ActionMode.Callback {

    private static final String TAG = RecyclerViewActionModeCallBack.class.getSimpleName();
    private SelectionTracker<Long> mSelectionTracker;

    public RecyclerViewActionModeCallBack(SelectionTracker<Long> selectionTracker) {
        mSelectionTracker = selectionTracker;
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.menu_recycler_cab, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                Log.d(TAG, "delete");
                actionMode.finish();
                return true;
            case R.id.menu_edit:
                Log.d(TAG, "edit");
                actionMode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mSelectionTracker.clearSelection();
    }

    public void onMultiItemSelected(ActionMode actionMode) {
        MenuItem menuEditItem = actionMode.getMenu().findItem(R.id.menu_edit);
        if (mSelectionTracker.getSelection().size() > 1) {
            menuEditItem.setVisible(false);
        } else {
            menuEditItem.setVisible(true);
        }
    }

}
