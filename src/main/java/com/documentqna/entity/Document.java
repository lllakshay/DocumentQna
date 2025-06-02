package com.documentqna.entity;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Document {
 
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @EqualsAndHashCode.Include
 private Long id;
 
 @Column(nullable = false)
 private String title;
 
 @Column(nullable = false)
 private String fileName;
 
 private String fileType;
 
 private Long fileSize;
 
 @Column(columnDefinition = "TEXT")
 private String content;
 
 @Column(columnDefinition = "TEXT")
 private String keywords;
 
 private String author;
 
 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "uploaded_by", nullable = false)
 private User uploadedBy;
 
 @CreatedDate
 @Column(nullable = false, updatable = false)
 private LocalDateTime createdAt;
 
 @LastModifiedDate
 private LocalDateTime updatedAt;

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

public String getContent() {
	return content;
}

public void setContent(String content) {
	this.content = content;
}

public String getKeywords() {
	return keywords;
}

public void setKeywords(String keywords) {
	this.keywords = keywords;
}

public String getAuthor() {
	return author;
}

public void setAuthor(String author) {
	this.author = author;
}

public User getUploadedBy() {
	return uploadedBy;
}

public void setUploadedBy(User uploadedBy) {
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