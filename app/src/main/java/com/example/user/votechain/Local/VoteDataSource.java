package com.example.user.votechain.Local;

import com.example.user.votechain.Database.IVoteDataSource;
import com.example.user.votechain.Model.Vote;

import java.util.List;

import io.reactivex.Flowable;

public class VoteDataSource implements IVoteDataSource {
    private VoteDAO voteDao;
    private static VoteDataSource mInstance;

    public VoteDataSource(VoteDAO voteDao) {
        this.voteDao = voteDao;
    }

    public static VoteDataSource getInstance(VoteDAO voteDao){
        if (mInstance == null) {
            mInstance = new VoteDataSource(voteDao);
        }
        return mInstance;
    }

    @Override
    public Flowable<List<Vote>> getAll() {
        return voteDao.getAll();
    }

    @Override
    public void insert(Vote... votes) {
        voteDao.insert(votes);
    }

    @Override
    public void update(Vote... votes) {
        voteDao.update(votes);
    }

    @Override
    public void delete(Vote vote) {
        voteDao.delete(vote);
    }

    @Override
    public void deleteAllVote() {
        voteDao.deleteAllVote();
    }
}
