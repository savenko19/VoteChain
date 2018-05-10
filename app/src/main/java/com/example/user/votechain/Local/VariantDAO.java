package com.example.user.votechain.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.user.votechain.Model.VoteVariant;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface VariantDAO {
    @Query("SELECT * FROM variants WHERE vote_id = :userId")
    Flowable<List<VoteVariant>> getAllByVote(long userId);

    @Insert
    void insert(VoteVariant... variants);

    @Update
    void update(VoteVariant... variants);

    @Delete
    void  delete(VoteVariant variant);

    @Query("DELETE FROM variants WHERE vote_id = :userId")
    void deleteAllByVote(long userId);


}
