package com.example.user.votechain;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.votechain.Blockchain.Block;
import com.example.user.votechain.Blockchain.Transaction;
import com.example.user.votechain.Blockchain.VoteChain;
import com.example.user.votechain.Blockchain.Wallet;
import com.example.user.votechain.Database.AppDatabase;
import com.example.user.votechain.Database.VariantRepository;
import com.example.user.votechain.Database.VoteRepository;
import com.example.user.votechain.Local.VariantDataSource;
import com.example.user.votechain.Local.VoteDataSource;
import com.example.user.votechain.Model.VoteVariant;

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

public class VoteVariantList extends AppCompatActivity {
    private static final String LOG_TAG = "mLog";
    private ListView variantListViewUser;


    List<VoteVariant> variantList = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private VariantRepository variantRepository;
    private VoteRepository voteRepository;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_variant_list);

        variantListViewUser = findViewById(R.id.variantListUser);

        compositeDisposable = new CompositeDisposable();

        final AppDatabase database = AppDatabase.getInstance(this);
        variantRepository = VariantRepository.getInstance(VariantDataSource.getInstance(database.variantDAO()));
        voteRepository = VoteRepository.getInstance(VoteDataSource.getInstance(database.voteDao()));

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, variantList);
        registerForContextMenu(variantListViewUser);
        variantListViewUser.setAdapter(adapter);
        loadData();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle("Выберете действие:");

        menu.add(Menu.NONE, 0, Menu.NONE, "Проголосовать");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final VoteVariant variant = variantList.get(info.position);


        switch (item.getItemId()) {
            case 0: {
                Log.d(LOG_TAG, "======================");
                Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> e) throws Exception {
                        int currScore = variant.getVariantScore();
                        Log.d(LOG_TAG, currScore + " ");
                        variant.setVariantScore(currScore + 1);
                        variantRepository.update(variant);
                        if (VoteChain.blockchain.isEmpty()) {

                        }
                        e.onComplete();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer() {
                                       @Override
                                       public void accept(Object o) throws Exception {
                                           Toast.makeText(VoteVariantList.this, "Ok", Toast.LENGTH_SHORT).show();
                                       }
                                   }, new Consumer<Throwable>() {
                                       @Override
                                       public void accept(Throwable throwable) throws Exception {
                                           Toast.makeText(VoteVariantList.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                       }
                                   },
                                new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        loadData();
                                    }
                                }
                        );
                variantListViewUser.setClickable(false);
                break;
            }
        }

        return true;
    }

    private void loadData() {
        Intent intent = getIntent();
        Disposable disposable = variantRepository.getAllByVote(intent.getLongExtra("voteId", 0))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<VoteVariant>>() {
                    @Override
                    public void accept(List<VoteVariant> variants) throws Exception {
                        onGetAllVoteVariantSuccess(variants);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(VoteVariantList.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllVoteVariantSuccess(List<VoteVariant> variants) {
        variantList.clear();
        variantList.addAll(variants);
        adapter.notifyDataSetChanged();
    }


}
