package com.documentqna.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@Getter
@Setter
public class QuestionDto {
 
 @NotBlank(message = "Question is required")
 private String question;
 
 public void setQuestion(String question) {
     this.question = question;
 }

 public String getQuestion() {
     return question;
 }
}