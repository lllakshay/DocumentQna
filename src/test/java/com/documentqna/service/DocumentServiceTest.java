package com.documentqna.service;

import com.documentqna.dto.DocumentFilterDto;
import com.documentqna.dto.DocumentResponseDto;
import com.documentqna.dto.DocumentUploadDto;
import com.documentqna.entity.Document;
import com.documentqna.entity.User;
import com.documentqna.repository.DocumentRepository;
import com.documentqna.util.DocumentProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentProcessor documentProcessor;

    @Mock
    private SearchService searchService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private DocumentService documentService;

    private User testUser;
    private User adminUser;
    private Document testDocument;
    private DocumentUploadDto uploadDto;
    private DocumentFilterDto filterDto;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .isAdmin(false)
                .build();

        adminUser = User.builder()
                .id(2L)
                .username("admin")
                .email("admin@example.com")
                .isAdmin(true)
                .build();

        testDocument = Document.builder()
                .id(1L)
                .title("Test Document")
                .fileName("test.pdf")
                .fileType("application/pdf")
                .fileSize(1024L)
                .content("Test content")
                .keywords("test, document")
                .author("Test Author")
                .uploadedBy(testUser)
                .createdAt(LocalDateTime.now())
                .build();

        uploadDto = DocumentUploadDto.builder()
                .title("Test Document")
                .author("Test Author")
                .build();

        filterDto = DocumentFilterDto.builder()
                .author("Test Author")
                .fileType("application/pdf")
                .build();
    }

    @Test
    void uploadDocument_Success() throws ExecutionException, InterruptedException {
        // Arrange
        when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");
        when(multipartFile.getContentType()).thenReturn("application/pdf");
        when(multipartFile.getSize()).thenReturn(1024L);
        when(documentProcessor.extractContent(multipartFile)).thenReturn("Test content");
        when(documentProcessor.extractKeywords("Test content")).thenReturn("test, document");
        when(documentRepository.save(any(Document.class))).thenReturn(testDocument);
        doNothing().when(searchService).indexDocument(testDocument);

        // Act
        CompletableFuture<DocumentResponseDto> result = documentService.uploadDocument(multipartFile, uploadDto, testUser);
        DocumentResponseDto responseDto = result.get();

        // Assert
        assertNotNull(responseDto);
        assertEquals("Test Document", responseDto.getTitle());
        assertEquals("test.pdf", responseDto.getFileName());
        assertEquals("application/pdf", responseDto.getFileType());
        assertEquals(1024L, responseDto.getFileSize());

        verify(documentProcessor).extractContent(multipartFile);
        verify(documentProcessor).extractKeywords("Test content");
        verify(documentRepository).save(any(Document.class));
        verify(searchService).indexDocument(testDocument);
    }

    @Test
    void uploadDocument_DocumentProcessorThrowsException() {
        // Arrange
        when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");
        when(documentProcessor.extractContent(multipartFile))
                .thenThrow(new RuntimeException("Failed to extract content"));

        // Act & Assert
        CompletableFuture<DocumentResponseDto> result = documentService.uploadDocument(multipartFile, uploadDto, testUser);
        
        assertThrows(RuntimeException.class, () -> {
            try {
                result.get();
            } catch (ExecutionException e) {
                throw e.getCause();
            }
        });

        verify(documentProcessor).extractContent(multipartFile);
        verify(documentRepository, never()).save(any(Document.class));
        verify(searchService, never()).indexDocument(any(Document.class));
    }

    @Test
    void uploadDocument_RepositorySaveThrowsException() {
        // Arrange
        when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");
        when(multipartFile.getContentType()).thenReturn("application/pdf");
        when(multipartFile.getSize()).thenReturn(1024L);
        when(documentProcessor.extractContent(multipartFile)).thenReturn("Test content");
        when(documentProcessor.extractKeywords("Test content")).thenReturn("test, document");
        when(documentRepository.save(any(Document.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        CompletableFuture<DocumentResponseDto> result = documentService.uploadDocument(multipartFile, uploadDto, testUser);
        
        assertThrows(RuntimeException.class, () -> {
            try {
                result.get();
            } catch (ExecutionException e) {
                throw e.getCause();
            }
        });

        verify(documentRepository).save(any(Document.class));
        verify(searchService, never()).indexDocument(any(Document.class));
    }

    @Test
    void getDocuments_Success() {
        // Arrange
        List<Document> documents = Arrays.asList(testDocument);
        Page<Document> documentPage = new PageImpl<>(documents);
        Pageable pageable = PageRequest.of(0, 10);

        when(documentRepository.findWithFilters(
                eq("Test Author"),
                eq("application/pdf"),
                isNull(),
                isNull(),
                eq(pageable)
        )).thenReturn(documentPage);

        // Act
        Page<DocumentResponseDto> result = documentService.getDocuments(filterDto, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Document", result.getContent().get(0).getTitle());

        verify(documentRepository).findWithFilters(
                eq("Test Author"),
                eq("application/pdf"),
                isNull(),
                isNull(),
                eq(pageable)
        );
    }

    @Test
    void getDocuments_EmptyResult() {
        // Arrange
        Page<Document> emptyPage = new PageImpl<>(Arrays.asList());
        Pageable pageable = PageRequest.of(0, 10);

        when(documentRepository.findWithFilters(
                any(), any(), any(), any(), eq(pageable)
        )).thenReturn(emptyPage);

        // Act
        Page<DocumentResponseDto> result = documentService.getDocuments(filterDto, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void getDocumentById_Success() {
        // Arrange
        when(documentRepository.findById(1L)).thenReturn(Optional.of(testDocument));

        // Act
        DocumentResponseDto result = documentService.getDocumentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Document", result.getTitle());
        assertEquals("test.pdf", result.getFileName());

        verify(documentRepository).findById(1L);
    }

    @Test
    void getDocumentById_DocumentNotFound() {
        // Arrange
        when(documentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> documentService.getDocumentById(1L));
        
        assertEquals("Document not found", exception.getMessage());
        verify(documentRepository).findById(1L);
    }

    @Test
    void deleteDocument_Success_Owner() {
        // Arrange
        when(documentRepository.findById(1L)).thenReturn(Optional.of(testDocument));
        doNothing().when(documentRepository).delete(testDocument);
        doNothing().when(searchService).removeFromIndex(1L);

        // Act
        documentService.deleteDocument(1L, testUser);

        // Assert
        verify(documentRepository).findById(1L);
        verify(documentRepository).delete(testDocument);
        verify(searchService).removeFromIndex(1L);
    }

    @Test
    void deleteDocument_Success_Admin() {
        // Arrange
        Document documentByOtherUser = Document.builder()
                .id(1L)
                .title("Test Document")
                .uploadedBy(testUser)
                .build();

        when(documentRepository.findById(1L)).thenReturn(Optional.of(documentByOtherUser));
        doNothing().when(documentRepository).delete(documentByOtherUser);
        doNothing().when(searchService).removeFromIndex(1L);

        // Act
        documentService.deleteDocument(1L, adminUser);

        // Assert
        verify(documentRepository).findById(1L);
        verify(documentRepository).delete(documentByOtherUser);
        verify(searchService).removeFromIndex(1L);
    }

    @Test
    void deleteDocument_DocumentNotFound() {
        // Arrange
        when(documentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> documentService.deleteDocument(1L, testUser));

        assertEquals("Document not found", exception.getMessage());
        verify(documentRepository).findById(1L);
        verify(documentRepository, never()).delete(any(Document.class));
        verify(searchService, never()).removeFromIndex(anyLong());
    }

    @Test
    void deleteDocument_InsufficientPermissions() {
        // Arrange
        User otherUser = User.builder()
                .id(3L)
                .username("otheruser")
                .isAdmin(false)
                .build();

        when(documentRepository.findById(1L)).thenReturn(Optional.of(testDocument));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> documentService.deleteDocument(1L, otherUser));

        assertEquals("Insufficient permissions", exception.getMessage());
        verify(documentRepository).findById(1L);
        verify(documentRepository, never()).delete(any(Document.class));
        verify(searchService, never()).removeFromIndex(anyLong());
    }

    @Test
    void deleteDocument_RepositoryDeleteThrowsException() {
        // Arrange
        when(documentRepository.findById(1L)).thenReturn(Optional.of(testDocument));
        doThrow(new RuntimeException("Database error")).when(documentRepository).delete(testDocument);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> documentService.deleteDocument(1L, testUser));

        assertEquals("Database error", exception.getMessage());
        verify(documentRepository).findById(1L);
        verify(documentRepository).delete(testDocument);
        verify(searchService, never()).removeFromIndex(anyLong());
    }

    @Test
    void getDocuments_WithNullFilter() {
        // Arrange
        DocumentFilterDto nullFilter = new DocumentFilterDto();
        List<Document> documents = Arrays.asList(testDocument);
        Page<Document> documentPage = new PageImpl<>(documents);
        Pageable pageable = PageRequest.of(0, 10);

        when(documentRepository.findWithFilters(
                isNull(), isNull(), isNull(), isNull(), eq(pageable)
        )).thenReturn(documentPage);

        // Act
        Page<DocumentResponseDto> result = documentService.getDocuments(nullFilter, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(documentRepository).findWithFilters(
                isNull(), isNull(), isNull(), isNull(), eq(pageable)
        );
    }
}
