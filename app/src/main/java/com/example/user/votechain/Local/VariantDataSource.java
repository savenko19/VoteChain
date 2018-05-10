package com.example.user.votechain.Local;

import com.example.user.votechain.Database.IVariantDataSource;
import com.example.user.votechain.Model.VoteVariant;

import java.util.List;

import io.reactivex.Flowable;

public class VariantDataSource implements IVariantDataSource {
    private VariantDAO variantDAO;
    private static VariantDataSource mInstance;

    public VariantDataSource(VariantDAO variantDAO) {
        this.variantDAO = variantDAO;
    }

    public static VariantDataSource getInstance(VariantDAO variantDAO) {
        if (mInstance == null) {
            mInstance = new VariantDataSource(variantDAO);
        }

        return mInstance;
    }

    @Override
    public Flowable<List<VoteVariant>> getAllByVote(long userId) {
        return variantDAO.getAllByVote(userId);
    }

    @Override
    public void insert(VoteVariant... variants) {
        variantDAO.insert(variants);
    }

    @Override
    public void update(VoteVariant... variants) {
        variantDAO.update(variants);
    }

    @Override
    public void delete(VoteVariant variant) {
        variantDAO.delete(variant);
    }

    @Override
    public void deleteAllByVote(long userId) {
        variantDAO.deleteAllByVote(userId);
    }

}
