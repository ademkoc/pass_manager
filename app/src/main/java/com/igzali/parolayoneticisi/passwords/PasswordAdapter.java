package com.igzali.parolayoneticisi.passwords;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.igzali.parolayoneticisi.R;
import com.igzali.parolayoneticisi.data.Password;
import com.igzali.parolayoneticisi.passwords.selection.PasswordItemDetails;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class PasswordAdapter extends ListAdapter<Password, PasswordAdapter.PasswordViewHolder> {

    static final DiffUtil.ItemCallback<Password> DIFF_CALLBACK = new DiffUtil.ItemCallback<Password>() {
        @Override
        public boolean areItemsTheSame(@NonNull Password oldItem, @NonNull Password newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Password oldItem, @NonNull Password newItem) {
            return oldItem.getLabel().equals(newItem.getLabel())
                    && oldItem.getUsername().equals(newItem.getUsername())
                    && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getEmail().equals(newItem.getEmail())
                    && oldItem.getPassword().equals(newItem.getPassword());
        }
    };
    private SelectionTracker<Password> mSelectionTracker;
    private static final String TAG = PasswordAdapter.class.getSimpleName();

    public PasswordAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_password, parent,false);
        return new PasswordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordViewHolder holder, int position) {
        Password currentNote = getItem(position);
        holder.emailTextView.setText(currentNote.getEmail());
        holder.labelTextView.setText(currentNote.getLabel());

        if (currentNote.getUsername().trim().isEmpty())
            holder.usernameTextView.setVisibility(View.GONE);
        else
            holder.usernameTextView.setText(currentNote.getUsername());

        if (currentNote.getDescription().trim().isEmpty())
            holder.descriptionTextView.setVisibility(View.GONE);
        else
            holder.descriptionTextView.setText(currentNote.getDescription());

        if (mSelectionTracker != null) {
            holder.itemView.setActivated(mSelectionTracker.isSelected(currentNote));
            holder.copyImage.setVisibility(mSelectionTracker.isSelected(currentNote) ? View.GONE : View.VISIBLE);
        }
    }

    public class PasswordViewHolder extends RecyclerView.ViewHolder {

        private TextView labelTextView;
        private TextView emailTextView;
        private TextView descriptionTextView;
        private TextView usernameTextView;
        private ImageView copyImage;

        PasswordViewHolder(@NonNull View itemView) {
            super(itemView);
            labelTextView = itemView.findViewById(R.id.text_label);
            emailTextView = itemView.findViewById(R.id.text_email);
            descriptionTextView = itemView.findViewById(R.id.text_description);
            usernameTextView = itemView.findViewById(R.id.text_username);
            copyImage = itemView.findViewById(R.id.img_copy);
        }

        public ItemDetailsLookup.ItemDetails<Password> getItemDetails() {
            return new PasswordItemDetails(getAdapterPosition(), getItem(getAdapterPosition()));
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    public Password getPassword(int position) {
        return getItem(position);
    }

    public void setSelectionTracker(SelectionTracker<Password> selectionTracker) {
        mSelectionTracker = selectionTracker;
    }

    public SelectionTracker<Password> getSelectionTracker() {
        return mSelectionTracker;
    }
}
