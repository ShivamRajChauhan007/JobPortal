package com.Application.JobListingPortal.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkDto {
    private Long jobId;
    private String position;
    private String location;
}
