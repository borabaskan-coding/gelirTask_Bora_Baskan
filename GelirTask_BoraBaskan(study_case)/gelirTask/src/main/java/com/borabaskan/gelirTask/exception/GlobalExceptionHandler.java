package com.borabaskan.gelirTask.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	//spring validation firlatilan hatalari yonetmek icin yazildi
	
	private List<String> addMapValue(List<String> list, String newValue){
		list.add(newValue);
		return list;
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		Map<String, List<String>> errorsMap = new HashMap<>();
		for (ObjectError  objError : exception.getBindingResult().getAllErrors()) {
			
			String fieldName = ((FieldError)objError).getField();
			
			if(errorsMap.containsKey(fieldName)) {
				
				errorsMap.put(fieldName, addMapValue(errorsMap.get(fieldName), objError.getDefaultMessage()));
				
			}else {
				errorsMap.put(fieldName, addMapValue(new ArrayList<>(), objError.getDefaultMessage()));
			}
		}
		
		return ResponseEntity.badRequest().body(createApiError(errorsMap));
	}
	
	
	private <T> ApiError<T> createApiError (T errors) {//Generic yazilmasinin sebebi metot kopyalamanin onune gecmek
		ApiError<T> apiError = new ApiError<T>();
		apiError.setId(UUID.randomUUID().toString());
		apiError.setErrorTime(new Date());
		apiError.setErrors(errors);
		
		return apiError;
	}
	
}
