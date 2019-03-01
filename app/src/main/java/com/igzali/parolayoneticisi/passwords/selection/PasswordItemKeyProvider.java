package com.igzali.parolayoneticisi.passwords.selection;

import com.igzali.parolayoneticisi.data.Password;
import com.igzali.parolayoneticisi.passwords.PasswordAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

public class PasswordItemKeyProvider extends ItemKeyProvider<Password> {

    private RecyclerView mRecyclerView;

    public PasswordItemKeyProvider(RecyclerView recyclerView) {
        super(SCOPE_CACHED);
        mRecyclerView = recyclerView;
    }

    @Nullable
    @Override
    public Password getKey(int position) {
        return ((PasswordAdapter) mRecyclerView.getAdapter()).getPassword(position);
    }

    @Override
    public int getPosition(@NonNull Password key) {
        RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForItemId(key.getId());
        return viewHolder == null ? RecyclerView.NO_POSITION : viewHolder.getLayoutPosition();
    }
}
