package com.example.user.votechain.Database;

import com.example.user.votechain.Model.User;

import java.util.List;

import io.reactivex.Flowable;

public class UserRepository implements IUserDataSource {
    private IUserDataSource mLocalDataSource;
    private static UserRepository mInstance;

    public UserRepository(IUserDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static UserRepository getInstance(IUserDataSource mLocalDataSource) {
        if (mInstance == null) {
            mInstance = new UserRepository(mLocalDataSource);
        }

        return mInstance;
    }

    @Override
    public Flowable<User> getUserById(int userId) {
        return mLocalDataSource.getUserById(userId);
    }

    @Override
    public Flowable<List<User>> getAllUsers() {
        return mLocalDataSource.getAllUsers();
    }

    @Override
    public Flowable<List<User>> getUserByName(String userName) {
        return mLocalDataSource.getUserByName(userName);
    }

    @Override
    public void insert(User... users) {
        mLocalDataSource.insert(users);
    }

    @Override
    public void update(User... users) {
        mLocalDataSource.update(users);
    }

    @Override
    public void delete(User user) {
        mLocalDataSource.delete(user);
    }

    @Override
    public void deleteAllUser() {
        mLocalDataSource.deleteAllUser();
    }
}
