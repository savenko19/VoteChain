package com.example.user.votechain.Database;

import com.example.user.votechain.Model.Vote;

import java.util.List;

import io.reactivex.Flowable;

public class VoteRepository implements IVoteDataSource{
    private IVoteDataSource mLocalDataSource;
    private static VoteRepository mInstance;

    public VoteRepository(IVoteDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static VoteRepository getInstance(IVoteDataSource mLocalDataSource) {
        if (mInstance == null) {
            mInstance = new VoteRepository(mLocalDataSource);
        }

        return mInstance;
    }

    @Override
    public Flowable<List<Vote>> getAll() {
        return mLocalDataSource.getAll();
    }

    @Override
    public void insert(Vote... votes) {
        mLocalDataSource.insert(votes);
    }

    @Override
    public void update(Vote... votes) {
        mLocalDataSource.update(votes);
    }

    @Override
    public void delete(Vote vote) {
        mLocalDataSource.delete(vote);
    }

    @Override
    public void deleteAllVote() {
        mLocalDataSource.deleteAllVote();
    }

}
