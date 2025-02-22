package com.Application.JobListingPortal.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Application.JobListingPortal.Entities.JobEntity;
import com.Application.JobListingPortal.Entities.JobEntity.JobType;
import com.Application.JobListingPortal.Entities.UserEntity;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long>{

    List<JobEntity> findByEmployer(UserEntity employer);

    @Query("SELECT j FROM JobEntity j WHERE " +
       "(:position IS NULL OR j.position LIKE %:position%) AND " +
       "(:location IS NULL OR j.location LIKE %:location%) AND " +
       "(:jobType IS NULL OR j.jobType = :jobType)")
Page<JobEntity> findJobs(@Param("position") String position,
                     @Param("location") String location,
                     @Param("jobType") JobEntity.JobType jobType,
                     Pageable pageable);


} 