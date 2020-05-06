package com.xworks.commonmodule.service;

import org.springframework.ui.Model;

import com.xworks.commonmodule.dto.RegisterDTO;

public interface ForgotPasswordService {

	public String validateUserForPasswordReset(RegisterDTO registerDTO, Model model);
	
	public String setNewPassword(RegisterDTO registerDTO,Model model);

}
