package com.example.user.votechain.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.user.votechain.Local.UserDAO;
import com.example.user.votechain.Local.VariantDAO;
import com.example.user.votechain.Local.VoteDAO;
import com.example.user.votechain.Model.User;
import com.example.user.votechain.Model.Vote;
import com.example.user.votechain.Model.VoteVariant;

import static com.example.user.votechain.Database.AppDatabase.DATABASE_VERSION;

@Database(entities = {Vote.class, User.class, VoteVariant.class}, version = DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VoteDatabase";

    public abstract VoteDAO voteDao();
    public abstract UserDAO userDAO();
    public abstract VariantDAO variantDAO();

    private static AppDatabase mInstance;

    public static AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return mInstance;
    }
}
