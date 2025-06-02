package com.documentqna.dto;



import com.documentqna.entity.Document;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentResponseDto {
 private Long id;
 private String title;
 private String fileName;
 private String fileType;
 private Long fileSize;
 private String author;
 private String uploadedBy;
 private LocalDateTime createdAt;
 private LocalDateTime updatedAt;
 
 public static DocumentResponseDto from(Document document) {
	    DocumentResponseDto dto = new DocumentResponseDto();
	    dto.setId(document.getId());
	    dto.setTitle(document.getTitle());
	    dto.setFileName(document.getFileName());
	    dto.setFileType(document.getFileType());
	    dto.setFileSize(document.getFileSize());
	    dto.setAuthor(document.getAuthor());
	    dto.setUploadedBy(document.getUploadedBy().getUsername());
	    dto.setCreatedAt(document.getCreatedAt());
	    dto.setUpdatedAt(document.getUpdatedAt());
	    return dto;
	}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public String getFileName() {
	return fileName;
}

public void setFileName(String fileName) {
	this.fileName = fileName;
}

public String getFileType() {
	return fileType;
}

public void setFileType(String fileType) {
	this.fileType = fileType;
}

public Long getFileSize() {
	return fileSize;
}

public void setFileSize(Long fileSize) {
	this.fileSize = fileSize;
}

public String getAuthor() {
	return author;
}

public void setAuthor(String author) {
	this.author = author;
}

public String getUploadedBy() {
	return uploadedBy;
}

public void setUploadedBy(String uploadedBy) {
	this.uploadedBy = uploadedBy;
}

public LocalDateTime getCreatedAt() {
	return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
	this.createdAt = createdAt;
}

public LocalDateTime getUpdatedAt() {
	return updatedAt;
}

public void setUpdatedAt(LocalDateTime updatedAt) {
	this.updatedAt = updatedAt;
}
 
 
}
