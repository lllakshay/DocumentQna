package com.documentqna.service;

import com.documentqna.dto.QuestionDto;
import com.documentqna.dto.SearchResponseDto;
import com.documentqna.entity.Document;
import com.documentqna.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    @InjectMocks
    private SearchService searchService;

    @Mock
    private DocumentRepository documentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchDocuments_fullTextSearchReturnsResults() {
        String query = "java";

        Document doc = Document.builder()
                .id(1L)
                .title("Java Basics")
                .author("Author A")
                .content("Java is a programming language.")
                .build();

        when(documentRepository.fullTextSearch(eq(query), any(Pageable.class)))
                .thenReturn(List.of(doc));

        QuestionDto questionDto = QuestionDto.builder()
                .question(query)
                .build();

        SearchResponseDto result = searchService.searchDocuments(questionDto);

        assertThat(result).isNotNull();
        assertThat(result.getQuery()).isEqualTo(query);
        assertThat(result.getTotalResults()).isEqualTo(1);
        assertThat(result.getSnippets()).hasSize(1);

        SearchResponseDto.DocumentSnippet snippet = result.getSnippets().get(0);
        assertThat(snippet.getDocumentId()).isEqualTo(doc.getId());
        assertThat(snippet.getTitle()).isEqualTo(doc.getTitle());
        assertThat(snippet.getAuthor()).isEqualTo(doc.getAuthor());
        assertThat(snippet.getSnippet()).contains("Java");
        assertThat(snippet.getRelevanceScore()).isGreaterThan(0);
    }

    @Test
    void searchDocuments_fallbackToKeywordSearchWhenFullTextReturnsEmpty() {
        String query = "python";

        Document doc = Document.builder()
                .id(2L)
                .title("Python Guide")
                .author("Author B")
                .content("Python programming language.")
                .build();

        when(documentRepository.fullTextSearch(eq(query), any(Pageable.class)))
                .thenReturn(List.of());

        when(documentRepository.keywordSearch(eq(query), any(Pageable.class)))
                .thenReturn(List.of(doc));

        QuestionDto questionDto = QuestionDto.builder()
                .question(query)
                .build();

        SearchResponseDto result = searchService.searchDocuments(questionDto);

        assertThat(result.getTotalResults()).isEqualTo(1);
        assertThat(result.getSnippets().get(0).getDocumentId()).isEqualTo(doc.getId());

        verify(documentRepository).fullTextSearch(eq(query), any(Pageable.class));
        verify(documentRepository).keywordSearch(eq(query), any(Pageable.class));
    }

    @Test
    void indexDocument_callsUpdateSearchVector() {
        Long docId = 5L;
        Document document = Document.builder()
                .id(docId)
                .build();

        searchService.indexDocument(document);

        verify(documentRepository).updateSearchVector(docId);
    }

    @Test
    void removeFromIndex_logsRemoval() {
        Long docId = 10L;

        // Just verifying that calling the method doesn't throw
        searchService.removeFromIndex(docId);
    }

    @Test
    void createSnippet_returnsCorrectSnippet() throws Exception {
        Document document = Document.builder()
                .id(1L)
                .title("Test Doc")
                .author("Author")
                .content("This is a sample content with Java keyword appearing multiple times. Java is great.")
                .build();

        // Using reflection to test private method createSnippet
        var method = SearchService.class.getDeclaredMethod("createSnippet", Document.class, String.class);
        method.setAccessible(true);

        SearchResponseDto.DocumentSnippet snippet = (SearchResponseDto.DocumentSnippet) method.invoke(searchService, document, "java");

        assertThat(snippet.getDocumentId()).isEqualTo(document.getId());
        assertThat(snippet.getTitle()).isEqualTo(document.getTitle());
        assertThat(snippet.getAuthor()).isEqualTo(document.getAuthor());
        assertThat(snippet.getSnippet().toLowerCase()).contains("java");
        assertThat(snippet.getRelevanceScore()).isGreaterThan(0);
    }
}
