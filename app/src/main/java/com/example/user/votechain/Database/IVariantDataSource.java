package com.example.user.votechain.Database;

import com.example.user.votechain.Model.VoteVariant;

import java.util.List;

import io.reactivex.Flowable;

public interface IVariantDataSource {
    Flowable<List<VoteVariant>> getAllByVote(long userId);
    void insert(VoteVariant... variants);
    void update(VoteVariant... variants);
    void  delete(VoteVariant variant);
    void deleteAllByVote(long userId);
}
