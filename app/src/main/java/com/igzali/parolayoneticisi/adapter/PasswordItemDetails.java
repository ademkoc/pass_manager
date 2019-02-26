package com.igzali.parolayoneticisi.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

public class PasswordItemDetails extends ItemDetailsLookup.ItemDetails<Long> {

    private int mPosition;
    private Long mId;

    public PasswordItemDetails(int position, Long id) {
        mPosition = position;
        mId = id;
    }

    @Override
    public int getPosition() {
        return mPosition;
    }

    @Nullable
    @Override
    public Long getSelectionKey() {
        return mId;
    }
}
