package com.documentqna.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DocumentProcessor {
 
 private static final List<String> STOP_WORDS = Arrays.asList(
         "the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for", "of", "with", "by"
 );
 
 public String extractContent(MultipartFile file) throws IOException {
     String fileName = file.getOriginalFilename();
     String contentType = file.getContentType();
     
     if (fileName == null) {
         throw new IllegalArgumentException("File name cannot be null");
     }
     
     if (contentType != null && contentType.equals("application/pdf")) {
         return extractFromPdf(file);
     } else if (fileName.endsWith(".docx")) {
         return extractFromDocx(file);
     } else if (contentType != null && contentType.startsWith("text/")) {
         return new String(file.getBytes());
     } else {
         throw new IllegalArgumentException("Unsupported file type: " + contentType);
     }
 }
 
 private String extractFromPdf(MultipartFile file) throws IOException {
     try (PDDocument document = PDDocument.load(file.getInputStream())) {
         PDFTextStripper stripper = new PDFTextStripper();
         return stripper.getText(document);
     }
 }
 
 private String extractFromDocx(MultipartFile file) throws IOException {
     try (XWPFDocument document = new XWPFDocument(file.getInputStream());
          XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
         return extractor.getText();
     }
 }
 
 public String extractKeywords(String content) {
     if (content == null || content.isEmpty()) {
         return "";
     }
     
     return Arrays.stream(content.toLowerCase().split("\\W+"))
             .filter(word -> word.length() > 3)
             .filter(word -> !STOP_WORDS.contains(word))
             .distinct()
             .limit(50)
             .collect(Collectors.joining(", "));
 }
}