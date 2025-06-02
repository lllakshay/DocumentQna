package com.documentqna.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class DocumentUploadDto {
 
 @NotBlank(message = "Title is required")
 private String title;
 
 private String author;
}