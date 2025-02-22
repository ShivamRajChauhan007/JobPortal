package com.Application.JobListingPortal.Entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "jobs")
public class JobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity employer;

    @Column(nullable = false, length = 255)
    private String position;

    @Column(nullable = false, length = 255)
    private String organisation;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 1000)
    private String skills;

    @Column(nullable = false,length = 255)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    @Column(nullable = false)
    private Long noOfVacencies;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public enum JobType{
        FULL_TIME, INTERN, CONTRACT
    }

    public List<String> getSkills() {
        return skills == null || skills.isEmpty() ? new ArrayList<>() : Arrays.asList(skills.split(","));
    }

    public void setSkills(List<String> skills) {
        this.skills = String.join(",", skills);
    }
}
