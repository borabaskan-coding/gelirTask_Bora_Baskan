package com.borabaskan.gelirTask.exception;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError<T> {

	//Response u standartlastirmak icin yazildi
	
	private String id;
	
	private Date errorTime;
	
	private T errors;
}
