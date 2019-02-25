package com.igzali.parolayoneticisi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import com.igzali.parolayoneticisi.entities.Password;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class PasswordAdapter extends ListAdapter<Password, PasswordAdapter.PasswordHolder> {

    public static final DiffUtil.ItemCallback<Password> DIFF_CALLBACK = new DiffUtil.ItemCallback<Password>() {
        @Override
        public boolean areItemsTheSame(@NonNull Password oldItem, @NonNull Password newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Password oldItem, @NonNull Password newItem) {
            return oldItem.getLabel().equals(newItem.getLabel())
                    && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getEmail().equals(newItem.getEmail())
                    && oldItem.getPassword().equals(newItem.getPassword());
        }
    };

    public PasswordAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public PasswordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_password, parent,false);
        return new PasswordHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordHolder holder, int position) {
        Password currentNote = getItem(position);
        holder.usernameTextView.setText(currentNote.getUsername());
        holder.emailTextView.setText(currentNote.getEmail());
        holder.passwordTextView.setText(currentNote.getPassword());
        holder.labelTextView.setText(currentNote.getLabel());
        holder.descriptionTextView.setText(currentNote.getDescription());
    }

    class PasswordHolder extends RecyclerView.ViewHolder {

        private TextView labelTextView;
        private TextView emailTextView;
        private TextView passwordTextView;
        private TextView descriptionTextView;
        private TextView usernameTextView;

        public PasswordHolder(@NonNull View itemView) {
            super(itemView);
            labelTextView = itemView.findViewById(R.id.text_label);
            emailTextView = itemView.findViewById(R.id.text_email);
            passwordTextView = itemView.findViewById(R.id.text_password);
            descriptionTextView = itemView.findViewById(R.id.text_description);
            usernameTextView = itemView.findViewById(R.id.text_username);
        }
    }
}
