package com.isep.acme.adapters.database.springRepositories.postgresql;

import com.isep.acme.adapters.database.models.postgresql.ReviewPg;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "postgresql")
public interface ReviewRepositoryPostgresql extends CrudRepository<ReviewPg, String> {

    @Query("SELECT r FROM review_postgresql r WHERE r.approvalStatus='Pending'")
    List<ReviewPg> findPending();

    @Query("SELECT r FROM review_postgresql r WHERE r.approvalStatus='Approved'")
    List<ReviewPg> findApproved();
}
