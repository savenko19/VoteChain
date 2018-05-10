package com.example.user.votechain.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Vote.class,
        parentColumns = "id", childColumns = "vote_id",
        onDelete = CASCADE),
        tableName = "variants")
public class VoteVariant {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "variant_name")
    private String name;

    @ColumnInfo(name = "variant_score")
    private int variantScore;


    @ColumnInfo(name = "vote_id")
    private long voteId;

    public VoteVariant() {
    }

    @Ignore
    public VoteVariant(String name) {
        this.name = name;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVoteId() {
        return voteId;
    }

    public void setVoteId(long voteId) {
        this.voteId = voteId;
    }

    public int getVariantScore() {
        return variantScore;
    }

    public void setVariantScore(int variantScore) {
        this.variantScore = variantScore;
    }

    @Override
    public String toString() {
        return new StringBuilder(name).append(variantScore).toString();
    }
}
