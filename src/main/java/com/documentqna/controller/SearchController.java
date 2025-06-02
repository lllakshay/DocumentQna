package com.documentqna.controller;


import com.documentqna.dto.QuestionDto;
import com.documentqna.dto.SearchResponseDto;
import com.documentqna.dto.ApiResponse;
import com.documentqna.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Document Search & Q&A", description = "Document search and question-answering endpoints")
public class SearchController {
 
 private SearchService searchService;
 
 @PostMapping("/ask")
 @Operation(summary = "Ask a question and get relevant document snippets")
 public ResponseEntity<ApiResponse<SearchResponseDto>> askQuestion(@Valid @RequestBody QuestionDto questionDto) {
     SearchResponseDto response = searchService.searchDocuments(questionDto);
     return ResponseEntity.ok(ApiResponse.success("Search completed successfully", response));
 }
 
 @GetMapping("/documents")
 @Operation(summary = "Search documents by keyword")
 public ResponseEntity<ApiResponse<SearchResponseDto>> searchDocuments(
         @RequestParam String query,
         @RequestParam(defaultValue = "10") int limit) {
     
     QuestionDto questionDto = new QuestionDto();
     questionDto.setQuestion(query);
     
     SearchResponseDto response = searchService.searchDocuments(questionDto);
     return ResponseEntity.ok(ApiResponse.success("Search completed successfully", response));
 }
}
