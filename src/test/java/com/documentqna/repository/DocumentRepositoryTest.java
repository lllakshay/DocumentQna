package com.documentqna.repository;

import com.documentqna.entity.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    private Document doc1, doc2, doc3;

    @BeforeEach
    void setUp() {
        doc1 = new Document();
        doc1.setAuthor("John Doe");
        doc1.setFileType("pdf");
        doc1.setTitle("Spring Data JPA Guide");
        doc1.setContent("This document explains how to use Spring Data JPA.");
        doc1.setKeywords("spring,jpa,database");
        doc1.setCreatedAt(LocalDateTime.now().minusDays(2));

        doc2 = new Document();
        doc2.setAuthor("Jane Smith");
        doc2.setFileType("docx");
        doc2.setTitle("Hibernate ORM Tutorial");
        doc2.setContent("Introduction to Hibernate ORM framework.");
        doc2.setKeywords("hibernate,orm,java");
        doc2.setCreatedAt(LocalDateTime.now().minusDays(1));

        doc3 = new Document();
        doc3.setAuthor("John Doe");
        doc3.setFileType("pdf");
        doc3.setTitle("Advanced Java Concurrency");
        doc3.setContent("This document covers Java concurrency topics.");
        doc3.setKeywords("java,concurrency,threads");
        doc3.setCreatedAt(LocalDateTime.now());

        documentRepository.saveAll(List.of(doc1, doc2, doc3));
    }

    @Test
    void testFindWithFilters_byAuthorAndFileType() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Document> result = documentRepository.findWithFilters("john doe", "pdf", null, null, pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting("author").allMatch(a -> ((String)a).equalsIgnoreCase("John Doe"));
        assertThat(result.getContent()).extracting("fileType").allMatch(ft -> ft.equals("pdf"));
    }

    @Test
    void testFindWithFilters_byDateRange() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime startDate = LocalDateTime.now().minusDays(1).minusHours(1);
        LocalDateTime endDate = LocalDateTime.now();

        Page<Document> result = documentRepository.findWithFilters(null, null, startDate, endDate, pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting("title").containsExactlyInAnyOrder(
            "Hibernate ORM Tutorial", "Advanced Java Concurrency"
        );
    }

    @Test
    void testFullTextSearch() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Document> results = documentRepository.fullTextSearch("hibernate", pageable);

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getTitle().toLowerCase()).contains("hibernate");
    }

    @Test
    void testKeywordSearch() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Document> results = documentRepository.keywordSearch("java", pageable);

        assertThat(results).isNotEmpty();
        assertThat(results).extracting("keywords").allMatch(k -> ((String)k).toLowerCase().contains("java"));
    }

    @Test
    void testUpdateSearchVector() {
        // This is a void native update query. We can only verify no exceptions occur here.
        documentRepository.updateSearchVector(doc1.getId());

        // Optional: reload and verify changes if you have the search_vector field mapped in entity (not shown here)
    }
}
