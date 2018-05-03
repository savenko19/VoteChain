package com.example.user.votechain.Local;

import com.example.user.votechain.Database.IUserDataSource;
import com.example.user.votechain.Model.User;

import java.util.List;

import io.reactivex.Flowable;

public class UserDataSource implements IUserDataSource {
    private UserDAO userDAO;
    private static UserDataSource mInstance;

    public UserDataSource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public static UserDataSource getInstance(UserDAO userDAO) {
        if (mInstance == null) {
            mInstance = new UserDataSource(userDAO);
        }

        return mInstance;
    }

    @Override
    public Flowable<User> getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    @Override
    public Flowable<List<User>> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public void insert(User... users) {
        userDAO.insert(users);
    }

    @Override
    public void update(User... users) {
        userDAO.update(users);
    }

    @Override
    public void delete(User user) {
        userDAO.deleteAllUser();
    }

    @Override
    public void deleteAllUser() {

    }
}
