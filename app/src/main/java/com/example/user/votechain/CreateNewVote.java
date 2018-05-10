package com.example.user.votechain;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.votechain.Database.AppDatabase;
import com.example.user.votechain.Database.VariantRepository;
import com.example.user.votechain.Database.VoteRepository;
import com.example.user.votechain.Local.VariantDataSource;
import com.example.user.votechain.Local.VoteDataSource;
import com.example.user.votechain.Model.Vote;
import com.example.user.votechain.Model.VoteVariant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CreateNewVote extends AppCompatActivity {

    private static final String LOG_TAG = "mLog";
    private ListView listVotes;
    private FloatingActionButton fabAddNewVote, fabUserList;

    List<Vote> voteList = new ArrayList<>();
    ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private VoteRepository voteRepository;
    private VariantRepository variantRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_vote);

        compositeDisposable = new CompositeDisposable();

        listVotes = findViewById(R.id.listVote);
        fabAddNewVote = findViewById(R.id.fabAddNewVote);
        fabUserList = findViewById(R.id.fabUserList);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, voteList);
        registerForContextMenu(listVotes);
        listVotes.setAdapter(adapter);

        //Database
        final AppDatabase database = AppDatabase.getInstance(this);
        voteRepository = VoteRepository.getInstance(VoteDataSource.getInstance(database.voteDao()));
        variantRepository = VariantRepository.getInstance(VariantDataSource.getInstance(database.variantDAO()));

        //Event
        fabUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewVote.this, UserList.class);
                startActivity(intent);
            }
        });
        fabAddNewVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText newVote = new EditText(CreateNewVote.this);
                newVote.setHint("Введите название");
                new AlertDialog.Builder(CreateNewVote.this)
                        .setTitle("Новое голосование")
                        .setView(newVote)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<Object> e) throws Exception {
                                        Vote vote = new Vote(newVote.getText().toString(), "Активно");
                                        voteRepository.insert(vote);
                                        e.onComplete();
                                    }
                                })
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new Consumer() {
                                                       @Override
                                                       public void accept(Object o) throws Exception {
                                                           Toast.makeText(CreateNewVote.this, "Голосование добавленно", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }, new Consumer<Throwable>() {
                                                       @Override
                                                       public void accept(Throwable throwable) throws Exception {
                                                           Toast.makeText(CreateNewVote.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CreateNewVote.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                deleteAllVote();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllVote() {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                voteRepository.deleteAllVote();
                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {

                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(CreateNewVote.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                loadData();
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle("Выберете действие:");

        menu.add(Menu.NONE, 0, Menu.NONE, "Изменить");
        menu.add(Menu.NONE, 1, Menu.NONE, "Удалить");
        menu.add(Menu.NONE, 2, Menu.NONE, "Добавить вариант");
        menu.add(Menu.NONE, 3, Menu.NONE, "Просмотр");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Vote vote = voteList.get(info.position);

        switch (item.getItemId()) {
            case 0: {
                final EditText edtName = new EditText(CreateNewVote.this);
                edtName.setText(vote.getName());
                edtName.setHint("Enter vote name");
                new AlertDialog.Builder(CreateNewVote.this)
                        .setTitle("Edit")
                        .setMessage("Edit vote name")
                        .setView(edtName)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TextUtils.isEmpty(edtName.getText().toString()))
                                    return;
                                else {
                                    vote.setName(edtName.getText().toString());
                                    updateVote(vote);
                                }
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
            break;
            case 1: {
                new AlertDialog.Builder(CreateNewVote.this)
                        .setMessage("Вы хотите удалить " + vote.getName())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteVote(vote);
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
            break;
            case 2: {
                Intent intent = new Intent(CreateNewVote.this, VoteVariantListAdmin.class);
                intent.putExtra("voteId", vote.getId());
                startActivity(intent);
                /*final EditText edtVariantName = new EditText(CreateNewVote.this);
                edtVariantName.setHint("Введите название");
                new AlertDialog.Builder(CreateNewVote.this)
                        .setTitle("Новый вариант")
                        .setView(edtVariantName)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<Object> e) throws Exception {
                                        VoteVariant variant = new VoteVariant(edtVariantName.getText().toString());
                                        variant.setVoteId(vote.getId());
                                        variantRepository.insert(variant);
                                        e.onComplete();
                                    }
                                })
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new Consumer() {
                                                       @Override
                                                       public void accept(Object o) throws Exception {

                                                       }
                                                   }, new Consumer<Throwable>() {
                                                       @Override
                                                       public void accept(Throwable throwable) throws Exception {
                                                           Toast.makeText(CreateNewVote.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
                }).create().show();*/
            }
            break;
            case 3: {
                /*final TextView txvVoteName = new TextView(CreateNewVote.this);
                txvVoteName.setText(vote.getName());
                List<VoteVariant> variants = (List<VoteVariant>) variantRepository.getAllByVote((int) vote.getId());
                ListView listView = new ListView(CreateNewVote.this);
                ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, variants);
                registerForContextMenu(listView);
                listView.setAdapter(arrayAdapter);
                new AlertDialog.Builder(CreateNewVote.this)
                        .setView(listView)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();*/
            }
            break;
        }

        return true;
    }

    private void deleteVote(final Vote vote) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                voteRepository.delete(vote);
                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {

                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(CreateNewVote.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateVote(final Vote vote) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                voteRepository.update(vote);
                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {

                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(CreateNewVote.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                loadData();
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
