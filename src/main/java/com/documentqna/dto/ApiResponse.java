package com.documentqna.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class ApiResponse<T> {
 private boolean success;
 private String message;
 private T data;
 
 
 public static <T> ApiResponse<T> success(String message, T data) {
	    ApiResponse<T> response = new ApiResponse<>();
	    response.setSuccess(true);
	    response.setMessage(message);
	    response.setData(data);
	    return response;
	}

	public static <T> ApiResponse<T> error(String message, T data) {
	    ApiResponse<T> response = new ApiResponse<>();
	    response.setSuccess(false);
	    response.setMessage(message);
	    response.setData(data);
	    return response;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	
}