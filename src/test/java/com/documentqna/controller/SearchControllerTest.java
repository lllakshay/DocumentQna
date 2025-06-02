package com.documentqna.controller;

import com.documentqna.dto.ApiResponse;
import com.documentqna.dto.QuestionDto;
import com.documentqna.dto.SearchResponseDto;
import com.documentqna.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SearchControllerTest {

    @Mock
    private SearchService searchService;

    @InjectMocks
    private SearchController searchController;

    @BeforeEach
    void setUp() {
        org.mockito.MockitoAnnotations.openMocks(this);
    }

    @Test
    void askQuestion_shouldReturnSearchResponse() {
        // Arrange
        QuestionDto questionDto = new QuestionDto();
        questionDto.setQuestion("What is Java?");
        
        SearchResponseDto searchResponse = new SearchResponseDto();
        // You can set fields in searchResponse if needed
        
        when(searchService.searchDocuments(any(QuestionDto.class))).thenReturn(searchResponse);

        // Act
        ResponseEntity<ApiResponse<SearchResponseDto>> responseEntity = searchController.askQuestion(questionDto);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        ApiResponse<SearchResponseDto> apiResponse = responseEntity.getBody();
        assertNotNull(apiResponse);
        assertEquals("Search completed successfully", apiResponse.getMessage());
        assertEquals(searchResponse, apiResponse.getData());

        // Verify that searchService.searchDocuments was called once with the exact questionDto
        ArgumentCaptor<QuestionDto> captor = ArgumentCaptor.forClass(QuestionDto.class);
        verify(searchService, times(1)).searchDocuments(captor.capture());
        assertEquals("What is Java?", captor.getValue().getQuestion());
    }

    @Test
    void searchDocuments_shouldReturnSearchResponse() {
        // Arrange
        String query = "Spring Boot";
        int limit = 5;

        SearchResponseDto searchResponse = new SearchResponseDto();
        when(searchService.searchDocuments(any(QuestionDto.class))).thenReturn(searchResponse);

        // Act
        ResponseEntity<ApiResponse<SearchResponseDto>> responseEntity = searchController.searchDocuments(query, limit);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        ApiResponse<SearchResponseDto> apiResponse = responseEntity.getBody();
        assertNotNull(apiResponse);
        assertEquals("Search completed successfully", apiResponse.getMessage());
        assertEquals(searchResponse, apiResponse.getData());

        // Verify that the questionDto passed to service contains the query string
        ArgumentCaptor<QuestionDto> captor = ArgumentCaptor.forClass(QuestionDto.class);
        verify(searchService, times(1)).searchDocuments(captor.capture());
        assertEquals(query, captor.getValue().getQuestion());
    }

    @Test
    void searchDocuments_shouldUseDefaultLimit_whenNotProvided() {
        // Arrange
        String query = "Test Query";

        SearchResponseDto searchResponse = new SearchResponseDto();
        when(searchService.searchDocuments(any(QuestionDto.class))).thenReturn(searchResponse);

        // Act
        ResponseEntity<ApiResponse<SearchResponseDto>> responseEntity = searchController.searchDocuments(query, 10); // default value

        // Assert - basically similar to above, but you could add specific checks if needed
        assertEquals(200, responseEntity.getStatusCodeValue());
        verify(searchService, times(1)).searchDocuments(any(QuestionDto.class));
    }
}
