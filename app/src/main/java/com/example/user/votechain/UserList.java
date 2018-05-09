package com.example.user.votechain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.votechain.Database.AppDatabase;
import com.example.user.votechain.Database.UserRepository;
import com.example.user.votechain.Local.UserDataSource;
import com.example.user.votechain.Model.User;
import com.example.user.votechain.Model.Vote;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UserList extends AppCompatActivity {
    private ListView userList;

    List<User> users = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        compositeDisposable = new CompositeDisposable();
        final AppDatabase database = AppDatabase.getInstance(this);

        userRepository = UserRepository.getInstance(UserDataSource.getInstance(database.userDAO()));

        userList = findViewById(R.id.lvUserList);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, users);
        registerForContextMenu(userList);
        userList.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        Disposable disposable = userRepository.getAllUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) throws Exception {
                        onGetAllUserSuccess(users);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(UserList.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllUserSuccess(List<User> users) {
        users.clear();
        users.addAll(users);
        adapter.notifyDataSetChanged();
    }
}
