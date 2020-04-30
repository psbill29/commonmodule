package com.xworks.commonmodule.service;

import org.springframework.ui.Model;

import com.xworks.commonmodule.dto.RegisterDTO;

public interface ServiceRegister {

	public String validateUser(RegisterDTO registerDTO, Model model);

	public String validateLogin(RegisterDTO registerDTO, Model model);

}
