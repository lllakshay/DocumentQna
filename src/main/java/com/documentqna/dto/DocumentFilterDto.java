package com.documentqna.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentFilterDto {
 private String author;
 private String fileType;
 
 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
 private LocalDateTime startDate;
 
 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
 private LocalDateTime endDate;
}
