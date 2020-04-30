package com.xworks.commonmodule.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "register_table")
@Setter
@Getter
@Validated
@ToString
public class RegisterEntity implements Serializable {

	@Id
	@GenericGenerator(name = "reference", strategy = "increment")
	@GeneratedValue(generator = "reference")
	@Column(name = "id")
	@NotNull
	private int id;

	@Column(name = "user_id", unique = true)
	@NotNull
	private String user_id;

	@Column(name = "email")
	@NotNull
	private String email;

	@Column(name = "phNo")
	private String ph_no;

	@Column(name = "course")
	private String course;

	@Column(name = "entry")
	private String entry;

	@Column(name = "password")
	private String password;

	@Column(name = "decoded")
	private String decodedPass;

	@Column(name = "noOfAttempts")
	private Integer noOfAttempts;

	public RegisterEntity() {
		System.out.println("created :" + this.getClass().getSimpleName());
	}

}
