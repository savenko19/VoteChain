package com.example.user.votechain.Database;

import com.example.user.votechain.Model.Vote;

import java.util.List;

import io.reactivex.Flowable;

public interface IVoteDataSource {
    Flowable<List<Vote>> getAll();
    void insert(Vote... votes);
    void update(Vote... votes);
    void delete(Vote vote);
    void deleteAllVote();
}
