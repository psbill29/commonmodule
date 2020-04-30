package com.xworks.commonmodule.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@Validated
public class RegisterDTO {

	@NotEmpty(message = "user id cannot be empty")
	@Size(min = 3, max = 10, message = "Please enter min of 3 and max of 10 characters")
	private String user_id;

	@NotEmpty(message = "Email cannot be empty")
	@Email(message = "enter valid email id")
	private String email;

	@Pattern(regexp = "(^$|[0-9]{10})")
	@NotNull
	private String ph_no;

	private String course;
	private String entry;
	private String password;
	private Integer noOfAttempts;

	public RegisterDTO() {
		System.out.println("created :" + this.getClass().getSimpleName());
	}

}
