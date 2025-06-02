package com.documentqna.repository;


import com.documentqna.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
 
 @Query("SELECT d FROM Document d WHERE " +
        "(:author IS NULL OR LOWER(d.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
        "(:fileType IS NULL OR d.fileType = :fileType) AND " +
        "(:startDate IS NULL OR d.createdAt >= :startDate) AND " +
        "(:endDate IS NULL OR d.createdAt <= :endDate) " +
        "ORDER BY d.createdAt DESC")
 Page<Document> findWithFilters(
         @Param("author") String author,
         @Param("fileType") String fileType,
         @Param("startDate") LocalDateTime startDate,
         @Param("endDate") LocalDateTime endDate,
         Pageable pageable);
 
 @Query(value = "SELECT d.* FROM documents d WHERE " +
        "to_tsvector('english', d.content || ' ' || d.title || ' ' || COALESCE(d.keywords, '')) " +
        "@@ plainto_tsquery('english', :query) " +
        "ORDER BY ts_rank(to_tsvector('english', d.content), plainto_tsquery('english', :query)) DESC",
        nativeQuery = true)
 List<Document> fullTextSearch(@Param("query") String query, Pageable pageable);
 
 @Query("SELECT d FROM Document d WHERE " +
        "LOWER(d.content) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(d.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(d.keywords) LIKE LOWER(CONCAT('%', :query, '%')) " +
        "ORDER BY d.createdAt DESC")
 List<Document> keywordSearch(@Param("query") String query, Pageable pageable);
 
 @Modifying
 @Query(value = "UPDATE documents SET search_vector = " +
        "to_tsvector('english', content || ' ' || title || ' ' || COALESCE(keywords, '')) " +
        "WHERE id = :documentId", nativeQuery = true)
 void updateSearchVector(@Param("documentId") Long documentId);
}