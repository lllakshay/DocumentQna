package com.documentqna.service;

import com.documentqna.dto.DocumentUploadDto;
import com.documentqna.dto.DocumentResponseDto;
import com.documentqna.dto.DocumentFilterDto;
import com.documentqna.entity.Document;
import com.documentqna.entity.User;
import com.documentqna.repository.DocumentRepository;
import com.documentqna.util.DocumentProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DocumentService {
    
    private final DocumentRepository documentRepository;
    private final DocumentProcessor documentProcessor;
    private final SearchService searchService;
    
    @Async
    public CompletableFuture<DocumentResponseDto> uploadDocument(
            MultipartFile file, DocumentUploadDto uploadDto, User user) {
        
        try {
            String extractedContent = documentProcessor.extractContent(file);
            String keywords = documentProcessor.extractKeywords(extractedContent);
            
            Document document = Document.builder()
                    .title(uploadDto.getTitle())
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .content(extractedContent)
                    .keywords(keywords)
                    .author(uploadDto.getAuthor())
                    .uploadedBy(user)
                    .build();
            
            Document savedDocument = documentRepository.save(document);
            searchService.indexDocument(savedDocument);
            
            log.info("Document uploaded successfully: {}", savedDocument.getTitle());
            return CompletableFuture.completedFuture(DocumentResponseDto.from(savedDocument));
            
        } catch (Exception e) {
            log.error("Error uploading document: {}", e.getMessage());
            throw new RuntimeException("Document upload failed", e);
        }
    }
    
    @Transactional(readOnly = true)
    public Page<DocumentResponseDto> getDocuments(DocumentFilterDto filterDto, Pageable pageable) {
        Page<Document> documents = documentRepository.findWithFilters(
            filterDto.getAuthor(), 
            filterDto.getFileType(), 
            filterDto.getStartDate(), 
            filterDto.getEndDate(),
            pageable
        );
        return documents.map(DocumentResponseDto::from);
    }
    
    @Transactional(readOnly = true)
    public DocumentResponseDto getDocumentById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        return DocumentResponseDto.from(document);
    }
    
    public void deleteDocument(Long id, User user) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        
        // Check if user has permission to delete
        if (!document.getUploadedBy().equals(user) && !user.isAdmin()) {
            throw new RuntimeException("Insufficient permissions");
        }
        
        documentRepository.delete(document);
        searchService.removeFromIndex(id);
        log.info("Document deleted: {}", document.getTitle());
    }
}
