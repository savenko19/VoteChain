package com.example.user.votechain.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.user.votechain.Model.Vote;

import java.util.List;

import io.reactivex.Flowable;
@Dao
public interface VoteDAO {
    @Query("SELECT * FROM votes")
    Flowable<List<Vote>> getAll();

    /*@Query("SELECT * FROM votes WHERE id =:voteId")
    Vote getVoteById(long voteId);*/

    @Insert
    void insert(Vote... votes);

    @Update
    void update(Vote... votes);

    @Delete
    void delete(Vote vote);

    @Query("DELETE FROM votes")
    void deleteAllVote();
}
