package com.example.user.votechain.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.design.widget.FloatingActionButton;

import com.example.user.votechain.Model.User;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM users WHERE id = :userId")
    Flowable<User> getUserById(int userId);

    @Query("SELECT * FROM users")
    Flowable<List<User>> getAllUsers();

    @Query("SELECT * FROM users WHERE user_name = :userName")
    Flowable<List<User>> getUserByName(String userName);

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User user);

    @Query("DELETE FROM users")
    void deleteAllUser();
}
