package com.example.user.votechain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.votechain.Database.AppDatabase;
import com.example.user.votechain.Database.VoteRepository;
import com.example.user.votechain.Local.VoteDataSource;
import com.example.user.votechain.Model.Vote;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UserProfile extends AppCompatActivity {
    private ListView listVotesUser;

    List<Vote> voteList = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private VoteRepository voteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        compositeDisposable = new CompositeDisposable();
        final AppDatabase database = AppDatabase.getInstance(this);

        voteRepository = VoteRepository.getInstance(VoteDataSource.getInstance(database.voteDao()));

        listVotesUser = findViewById(R.id.listVoteUser);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, voteList);
        registerForContextMenu(listVotesUser);
        listVotesUser.setAdapter(adapter);
        loadData();
    }

    private void loadData() {
        Disposable disposable = voteRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Vote>>() {
                    @Override
                    public void accept(List<Vote> votes) throws Exception {
                        onGetAllVoteSuccess(votes);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(UserProfile.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllVoteSuccess(List<Vote> votes) {
        voteList.clear();
        voteList.addAll(votes);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle("Выберете действие:");

        menu.add(Menu.NONE, 0, Menu.NONE, "Участвовать");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Vote vote = voteList.get(info.position);

        switch (item.getItemId()) {
            case 0: {
                Intent intent = new Intent(UserProfile.this, VoteVariantList.class);
                String voteName = vote.getName();
                long voteId = vote.getId();
                intent.putExtra("voteName", voteName);
                intent.putExtra("voteId", voteId);
                startActivity(intent);
                break;
            }
        }

        return true;
    }
}
