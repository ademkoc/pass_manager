package com.igzali.parolayoneticisi.ui;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.igzali.parolayoneticisi.R;

import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.selection.SelectionTracker;

public class RecyclerViewActionModeCallBack implements ActionMode.Callback {

    public interface OnActionItemClickListener {
        boolean onActionItemClick(ActionMode actionMode, MenuItem item);
    }

    private static final String TAG = RecyclerViewActionModeCallBack.class.getSimpleName();
    private SelectionTracker<Long> mSelectionTracker;
    private OnActionItemClickListener mActionItemClickListener;

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
        return mActionItemClickListener.onActionItemClick(actionMode, item);
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

    public void setActionItemClickListener(OnActionItemClickListener actionItemClickListener) {
        mActionItemClickListener = actionItemClickListener;
    }
}
