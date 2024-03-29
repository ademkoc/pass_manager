package com.igzali.parolayoneticisi.passwords.selection;

import android.view.MotionEvent;
import android.view.View;

import com.igzali.parolayoneticisi.data.Password;
import com.igzali.parolayoneticisi.passwords.PasswordAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

public class PasswordItemLookup extends ItemDetailsLookup<Password> {

    private final RecyclerView mRecyclerView;

    public PasswordItemLookup(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }


    @Nullable
    @Override
    public ItemDetails<Password> getItemDetails(@NonNull MotionEvent e) {
        View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
            if (holder instanceof PasswordAdapter.PasswordViewHolder) {
                return ((PasswordAdapter.PasswordViewHolder) holder).getItemDetails();
            }
        }
        return null;
    }
}
