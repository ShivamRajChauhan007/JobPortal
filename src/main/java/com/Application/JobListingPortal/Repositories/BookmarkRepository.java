package com.Application.JobListingPortal.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Application.JobListingPortal.Entities.BookmarkEntity;
import com.Application.JobListingPortal.Entities.JobEntity;
import com.Application.JobListingPortal.Entities.UserEntity;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long>{
    Optional<BookmarkEntity> findByUserAndJob(UserEntity user, JobEntity job);
    
    List<BookmarkEntity> findByUser(UserEntity user);
}
