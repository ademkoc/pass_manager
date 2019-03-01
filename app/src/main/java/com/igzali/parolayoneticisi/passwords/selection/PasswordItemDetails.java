package com.igzali.parolayoneticisi.passwords.selection;

import com.igzali.parolayoneticisi.data.Password;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

public class PasswordItemDetails extends ItemDetailsLookup.ItemDetails<Password> {

    private int mPosition;
    private Password mKey;

    public PasswordItemDetails(int position, Password key) {
        mPosition = position;
        mKey = key;
    }

    @Override
    public int getPosition() {
        return mPosition;
    }

    @Nullable
    @Override
    public Password getSelectionKey() {
        return mKey;
    }
}
