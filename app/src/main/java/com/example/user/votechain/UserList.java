package com.example.user.votechain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.user.votechain.Database.UserRepository;
import com.example.user.votechain.Model.User;

import java.util.List;

public class UserList extends AppCompatActivity {
    private ListView userList;

    List<User> users;
    ArrayAdapter adapter;

    UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userList = findViewById(R.id.lvUserList);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, users);
        registerForContextMenu(userList);
        userList.setAdapter(adapter);

    }
}
