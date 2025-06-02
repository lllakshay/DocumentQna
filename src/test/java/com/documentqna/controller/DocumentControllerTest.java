package com.documentqna.controller;

import com.documentqna.dto.*;
import com.documentqna.entity.User;
import com.documentqna.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DocumentControllerTest {

    private DocumentService documentService;
    private DocumentController documentController;

    @BeforeEach
    void setUp() throws Exception {
        documentService = mock(DocumentService.class);
        documentController = new DocumentController();

        // Manually inject mock into non-final field
        Field serviceField = DocumentController.class.getDeclaredField("documentService");
        serviceField.setAccessible(true);
        serviceField.set(documentController, documentService);
    }

    @Test
    void testUploadDocument() throws Exception {
        // Given
        MultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "dummy content".getBytes());
        DocumentUploadDto uploadDto = new DocumentUploadDto();
        User user = new User();
        DocumentResponseDto responseDto = new DocumentResponseDto();

        when(documentService.uploadDocument(mockFile, uploadDto, user))
                .thenReturn(CompletableFuture.completedFuture(responseDto));

        // When
        CompletableFuture<ResponseEntity<ApiResponse<DocumentResponseDto>>> future =
                documentController.uploadDocument(mockFile, uploadDto, user);

        ResponseEntity<ApiResponse<DocumentResponseDto>> response = future.get();

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Document uploaded successfully", response.getBody().getMessage());
        assertEquals(responseDto, response.getBody().getData());
        verify(documentService, times(1)).uploadDocument(mockFile, uploadDto, user);
    }

    @Test
    void testGetDocuments() {
        // Given
        DocumentFilterDto filterDto = new DocumentFilterDto();
        Pageable pageable = PageRequest.of(0, 20, Sort.Direction.DESC, "createdAt");
        DocumentResponseDto doc1 = new DocumentResponseDto();
        Page<DocumentResponseDto> page = new PageImpl<>(List.of(doc1));

        when(documentService.getDocuments(filterDto, pageable)).thenReturn(page);

        // When
        ResponseEntity<ApiResponse<Page<DocumentResponseDto>>> response = documentController.getDocuments(filterDto, pageable);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Documents retrieved successfully", response.getBody().getMessage());
        assertEquals(1, response.getBody().getData().getTotalElements());
        verify(documentService, times(1)).getDocuments(filterDto, pageable);
    }

    @Test
    void testGetDocumentById() {
        // Given
        Long docId = 1L;
        DocumentResponseDto document = new DocumentResponseDto();
        when(documentService.getDocumentById(docId)).thenReturn(document);

        // When
        ResponseEntity<ApiResponse<DocumentResponseDto>> response = documentController.getDocument(docId);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Document retrieved successfully", response.getBody().getMessage());
        assertEquals(document, response.getBody().getData());
        verify(documentService, times(1)).getDocumentById(docId);
    }

    @Test
    void testDeleteDocument() {
        // Given
        Long docId = 1L;
        User user = new User();

        // When
        ResponseEntity<ApiResponse<Void>> response = documentController.deleteDocument(docId, user);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Document deleted successfully", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(documentService, times(1)).deleteDocument(docId, user);
    }
}
