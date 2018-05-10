package com.example.user.votechain;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.votechain.Database.AppDatabase;
import com.example.user.votechain.Database.VariantRepository;
import com.example.user.votechain.Local.VariantDataSource;
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

public class VoteVariantListAdmin extends AppCompatActivity{
    private ListView variantListView;
    private FloatingActionButton fabAddNewVariant;

    List<VoteVariant> variantList = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private VariantRepository variantRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_variant_list_admin);

        variantListView = findViewById(R.id.variantList);
        fabAddNewVariant = findViewById(R.id.fabAddNewVariant);

        compositeDisposable = new CompositeDisposable();

        final AppDatabase database = AppDatabase.getInstance(this);
        variantRepository = VariantRepository.getInstance(VariantDataSource.getInstance(database.variantDAO()));

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, variantList);
        registerForContextMenu(variantListView);
        variantListView.setAdapter(adapter);

        fabAddNewVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText newVariant = new EditText(VoteVariantListAdmin.this);
                newVariant.setHint("Введите название");

                new AlertDialog.Builder(VoteVariantListAdmin.this)
                        .setTitle("Новый вариант")
                        .setView(newVariant)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<Object> e) throws Exception {
                                        VoteVariant variant = new VoteVariant(newVariant.getText().toString());
                                        Intent intent = getIntent();
                                        variant.setVoteId(intent.getLongExtra("voteId", 0));
                                        variantRepository.insert(variant);
                                        e.onComplete();
                                    }
                                }).observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new Consumer() {
                                                       @Override
                                                       public void accept(Object o) throws Exception {
                                                           Toast.makeText(VoteVariantListAdmin.this, "Голосование добавленно", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }, new Consumer<Throwable>() {
                                                       @Override
                                                       public void accept(Throwable throwable) throws Exception {
                                                           Toast.makeText(VoteVariantListAdmin.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

            }
        });
        loadData();
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
                        Toast.makeText(VoteVariantListAdmin.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
