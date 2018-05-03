package com.example.user.votechain.Database;

import com.example.user.votechain.Model.VoteVariant;

import java.util.List;

import io.reactivex.Flowable;

public class VariantRepository implements IVariantDataSource {
    private IVariantDataSource mLocalDataSource;
    private static VariantRepository mInstance;

    public VariantRepository(IVariantDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static VariantRepository getInstance(IVariantDataSource mLocalDataSource) {
        if (mInstance == null) {
            mInstance = new VariantRepository(mLocalDataSource);
        }

        return mInstance;
    }

    @Override
    public Flowable<List<VoteVariant>> getAllByVote(int userId) {
        return mLocalDataSource.getAllByVote(userId);
    }

    @Override
    public void insert(VoteVariant... variants) {
        mLocalDataSource.insert(variants);
    }

    @Override
    public void update(VoteVariant... variants) {
        mLocalDataSource.update(variants);
    }

    @Override
    public void delete(VoteVariant variant) {
        mLocalDataSource.delete(variant);
    }

    @Override
    public void deleteAllByVote(int userId) {
        mLocalDataSource.deleteAllByVote(userId);
    }
}
