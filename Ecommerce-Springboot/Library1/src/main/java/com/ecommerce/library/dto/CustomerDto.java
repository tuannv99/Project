package com.ecommerce.library.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
public class CustomerDto {
	@Size(min = 3, max = 8, message = "First name should have 3-8 characters!")
	private String firstName;
	
	@Size(min = 3, max = 8, message = "Last name should have 3-8 characters!")
	private String lastName;
	
	private String username;
	
	@Size(min = 5, max = 10, message = "Password should have 5-10 characters!")
	private String password;
	
	private String repeatPassword;
}
