package com.example.user.votechain.Database;

import com.example.user.votechain.Model.User;

import java.util.List;

import io.reactivex.Flowable;

public interface IUserDataSource {
    Flowable<User> getUserById(int userId);
    Flowable<List<User>> getAllUsers();
    Flowable<List<User>> getUserByName(String userName);
    void insert(User... users);
    void update(User... users);
    void delete(User user);
    void deleteAllUser();
}
