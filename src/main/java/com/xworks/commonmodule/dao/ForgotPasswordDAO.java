package com.xworks.commonmodule.dao;

import org.springframework.ui.Model;

import com.xworks.commonmodule.entity.RegisterEntity;

public interface ForgotPasswordDAO {

	public RegisterEntity checkUserDetails(String email, Model model);

	public boolean resetPasswrod(RegisterEntity registerEntity);

	public boolean resetNoOfAttempts(String email, Model model);

}
