package com.documentqna.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResponseDto {
 private String query;
 private int totalResults;
 private List<DocumentSnippet> snippets;
 
 @Data
 @Builder
 public static class DocumentSnippet {
     private Long documentId;
     private String title;
     private String author;
     private String snippet;
     private double relevanceScore;
 }
}
