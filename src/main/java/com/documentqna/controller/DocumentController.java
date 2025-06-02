package com.documentqna.controller;


import com.documentqna.dto.DocumentUploadDto;
import com.documentqna.dto.DocumentResponseDto;
import com.documentqna.dto.DocumentFilterDto;
import com.documentqna.dto.ApiResponse;
import com.documentqna.entity.User;
import com.documentqna.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Document Management", description = "Document upload, retrieval and management endpoints")
public class DocumentController {
 
 private  DocumentService documentService;
 
 @PostMapping("/upload")
 @Operation(summary = "Upload a new document")
 @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR')")
 public CompletableFuture<ResponseEntity<ApiResponse<DocumentResponseDto>>> uploadDocument(
         @RequestParam("file") MultipartFile file,
         @Valid @ModelAttribute DocumentUploadDto uploadDto,
         @AuthenticationPrincipal User user) {
     
     return documentService.uploadDocument(file, uploadDto, user)
             .thenApply(doc -> ResponseEntity.ok(ApiResponse.success("Document uploaded successfully", doc)));
 }
 
 @GetMapping
 @Operation(summary = "Get all documents with filtering and pagination")
 public ResponseEntity<ApiResponse<Page<DocumentResponseDto>>> getDocuments(
         @Valid DocumentFilterDto filterDto,
         @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
     
     Page<DocumentResponseDto> documents = documentService.getDocuments(filterDto, pageable);
     return ResponseEntity.ok(ApiResponse.success("Documents retrieved successfully", documents));
 }
 
 @GetMapping("/{id}")
 @Operation(summary = "Get document by ID")
 public ResponseEntity<ApiResponse<DocumentResponseDto>> getDocument(
         @Parameter(description = "Document ID") @PathVariable Long id) {
     
     DocumentResponseDto document = documentService.getDocumentById(id);
     return ResponseEntity.ok(ApiResponse.success("Document retrieved successfully", document));
 }
 
 @DeleteMapping("/{id}")
 @Operation(summary = "Delete a document")
 @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR')")
 public ResponseEntity<ApiResponse<Void>> deleteDocument(
         @Parameter(description = "Document ID") @PathVariable Long id,
         @AuthenticationPrincipal User user) {
     
     documentService.deleteDocument(id, user);
     return ResponseEntity.ok(ApiResponse.success("Document deleted successfully", null));
 }
}
