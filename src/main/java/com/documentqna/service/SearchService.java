package com.documentqna.service;


import com.documentqna.dto.QuestionDto;
import com.documentqna.dto.SearchResponseDto;
import com.documentqna.entity.Document;
import com.documentqna.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SearchService {
 
 private final DocumentRepository documentRepository;
 
 @Cacheable(value = "searchResults", key = "#questionDto.question")
 public SearchResponseDto searchDocuments(QuestionDto questionDto) {
     String query = questionDto.getQuestion().toLowerCase();
     Pageable pageable = PageRequest.of(0, 10);
     
     // Full-text search using PostgreSQL tsvector
     List<Document> documents = documentRepository.fullTextSearch(query, pageable);
     
     if (documents.isEmpty()) {
         // Fallback to keyword search
         documents = documentRepository.keywordSearch(query, pageable);
     }
     
     List<SearchResponseDto.DocumentSnippet> snippets = documents.stream()
             .map(doc -> createSnippet(doc, query))
             .collect(Collectors.toList());
     
     log.info("Search completed for query: '{}', found {} results", query, snippets.size());
     
     return SearchResponseDto.builder()
             .query(questionDto.getQuestion())
             .totalResults(snippets.size())
             .snippets(snippets)
             .build();
 }
 
 public void indexDocument(Document document) {
     // Update search vectors for full-text search
     documentRepository.updateSearchVector(document.getId());
     log.debug("Document indexed for search: {}", document.getId());
 }
 
 public void removeFromIndex(Long documentId) {
     log.debug("Document removed from search index: {}", documentId);
 }
 
 private SearchResponseDto.DocumentSnippet createSnippet(Document document, String query) {
     String content = document.getContent();
     String snippet = extractRelevantSnippet(content, query);
     
     return SearchResponseDto.DocumentSnippet.builder()
             .documentId(document.getId())
             .title(document.getTitle())
             .author(document.getAuthor())
             .snippet(snippet)
             .relevanceScore(calculateRelevanceScore(content, query))
             .build();
 }
 
 private String extractRelevantSnippet(String content, String query) {
     int maxSnippetLength = 200;
     String lowerContent = content.toLowerCase();
     String lowerQuery = query.toLowerCase();
     
     int index = lowerContent.indexOf(lowerQuery);
     if (index == -1) return content.substring(0, Math.min(maxSnippetLength, content.length()));
     
     int start = Math.max(0, index - 50);
     int end = Math.min(content.length(), index + lowerQuery.length() + 100);
     
     return "..." + content.substring(start, end) + "...";
 }
 
 private double calculateRelevanceScore(String content, String query) {
     String lowerContent = content.toLowerCase();
     String lowerQuery = query.toLowerCase();
     
     long occurrences = lowerContent.split(lowerQuery, -1).length - 1;
     return (double) occurrences / content.length() * 1000;
 }
}