package com.igzali.parolayoneticisi.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.igzali.parolayoneticisi.utils.DatabaseConsts;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = DatabaseConsts.PASSWORDS_TABLE_NAME)
public class Password implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String label;
    private String email;
    private String username;
    private String password;
    private String description;
    private Date createdDate;
    private Date updatedDate;

    public Password(String label, String email, String password) {
        this.label = label;
        this.email = email;
        this.password = password;
    }

    @Ignore
    public Password(String label, String email, String password, String username, String description) {
        this.label = label;
        this.email = email;
        this.username = username;
        this.password = password;
        this.description = description;
    }

    protected Password(Parcel in) {
        id = in.readInt();
        label = in.readString();
        email = in.readString();
        username = in.readString();
        password = in.readString();
        description = in.readString();
    }

    public static final Creator<Password> CREATOR = new Creator<Password>() {
        @Override
        public Password createFromParcel(Parcel in) {
            return new Password(in);
        }

        @Override
        public Password[] newArray(int size) {
            return new Password[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(label);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(description);
    }
}

