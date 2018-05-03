package com.example.user.votechain;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.votechain.Database.AppDatabase;
import com.example.user.votechain.Database.UserRepository;
import com.example.user.votechain.Local.UserDataSource;
import com.example.user.votechain.Model.User;
import com.example.user.votechain.Model.Vote;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Login extends AppCompatActivity {
    private static final String LOG_TAG = "mLog";

    private FloatingActionButton fabNewUser;

    private EditText edtUserName, edtPassword;
    private TextView txvLogin;

    List<User> users = new ArrayList<>();
    //ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        compositeDisposable = new CompositeDisposable();

        fabNewUser = findViewById(R.id.fabNewUser);

        edtUserName = findViewById(R.id.edtLoginName);
        edtPassword = findViewById(R.id.edtLoginPassword);

        txvLogin = findViewById(R.id.txvLogin);

        //adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, users);

        AppDatabase database = AppDatabase.getInstance(this);
        userRepository = UserRepository.getInstance(UserDataSource.getInstance(database.userDAO()));

        //Events
        txvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUserName.getText().toString().equals("admin") && edtPassword.getText().toString().equals("admin")) {
                    Intent intent = new Intent(Login.this, CreateNewVote.class);
                    startActivity(intent);
                }
            }
        });

        fabNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> e) throws Exception {
                        User user = new User("name", "password");
                        users.add(user);
                        userRepository.insert(user);
                        Log.d(LOG_TAG, "================================================================================");
                        e.onComplete();
                    }
                })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<Object>() {
                                       @Override
                                       public void accept(Object o) throws Exception {
                                           Toast.makeText(Login.this, "Успешно", Toast.LENGTH_SHORT).show();
                                       }
                                   }, new Consumer<Throwable>() {
                                       @Override
                                       public void accept(Throwable throwable) throws Exception {
                                           Toast.makeText(Login.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                       }
                                   },
                                new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        loadData();
                                    }
                                }
                        );
            }
        });
//        loadData();
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
                        Toast.makeText(Login.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllUserSuccess(List<User> users) {
        users.clear();
        users.addAll(users);
       // adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
