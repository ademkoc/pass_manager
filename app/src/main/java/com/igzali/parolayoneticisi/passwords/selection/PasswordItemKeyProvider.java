package com.igzali.parolayoneticisi.passwords.selection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

public class PasswordItemKeyProvider extends ItemKeyProvider<Long> {

    private RecyclerView mRecyclerView;

    public PasswordItemKeyProvider(RecyclerView recyclerView) {
        super(SCOPE_CACHED);
        mRecyclerView = recyclerView;
    }

    @Nullable
    @Override
    public Long getKey(int position) {
        return mRecyclerView.getAdapter().getItemId(position);
    }

    @Override
    public int getPosition(@NonNull Long key) {
        RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForItemId(key);
        return viewHolder == null ? RecyclerView.NO_POSITION : viewHolder.getLayoutPosition();
    }
}
