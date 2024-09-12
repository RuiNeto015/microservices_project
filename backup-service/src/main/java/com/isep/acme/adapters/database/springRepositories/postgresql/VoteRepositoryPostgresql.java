package com.isep.acme.adapters.database.springRepositories.postgresql;

import com.isep.acme.adapters.database.models.postgresql.VotePg;
import com.isep.acme.adapters.database.models.postgresql.VoteKeyPg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface VoteRepositoryPostgresql extends JpaRepository<VotePg, VoteKeyPg> {

    @Modifying
    @Query("DELETE FROM vote_postgresql v WHERE v.review.id = :id")
    void deleteAllByReview(String id);

    @Query("SELECT v FROM vote_postgresql v WHERE v.user.id = :userId and v.review.id = :reviewId")
    VotePg findByUserAndReview(String userId, String reviewId);

}
