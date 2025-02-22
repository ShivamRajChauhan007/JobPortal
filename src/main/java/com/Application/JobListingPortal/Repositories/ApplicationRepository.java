package com.Application.JobListingPortal.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Application.JobListingPortal.Entities.ApplicationEntity;
import com.Application.JobListingPortal.Entities.JobEntity;
import com.Application.JobListingPortal.Entities.UserEntity;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long>{
        boolean existsByJobAndApplicant(JobEntity job, UserEntity applicant);
        List<ApplicationEntity> findByApplicant(UserEntity applicant);
        List<ApplicationEntity> findByJob(JobEntity job);

}
