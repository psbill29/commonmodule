package com.xworks.commonmodule.service;

import java.util.Objects;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.xworks.commonmodule.dao.RegisterDAO;
import com.xworks.commonmodule.dto.RegisterDTO;
import com.xworks.commonmodule.entity.RegisterEntity;

@Component
public class ServiceRegisterImpl implements ServiceRegister {

	@Autowired
	private RegisterDAO registerDAO;

	public ServiceRegisterImpl() {
		System.out.println("created: " + this.getClass().getSimpleName());
	}

	public String validateUser(RegisterDTO registerDTO, Model model) {

		System.out.println("under validateUser method :" + this.getClass().getSimpleName());

		RegisterEntity registerEntity = new RegisterEntity();
		System.out.println("here registerDTO objects are send to registerEntity");
		BeanUtils.copyProperties(registerDTO, registerEntity);

		if (registerDTO.getEntry().equals("no")) {
			model.addAttribute("agree", "please agree Terms to proceed");
			System.out.println("please agree to terms and conditions and proceed:");
			return "register";
		}

		else if (registerDAO.validateUserID(registerDTO, model) != null) {
			System.out.println("THIS IS TO CHECK WHETHER UserID EXISTANCE SAME OR NO");
			model.addAttribute("existingUser", "user ID already exist try with another");
			return "register";
		} else if (registerDAO.validateEmail(registerDTO, model) != null) {
			System.out.println("THIS IS TO CHECK WHETHER EMAIL EXISTANCE SAME OR NO");
			model.addAttribute("existingEmail", "email already exist try with another");
			return "register";

		} else

			System.out.println("all the feilds are valid and ready to save in DB: ");

		String chars = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
		String password = "";
		int length = 8;

		Random random = new Random();
		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = chars.charAt(random.nextInt(chars.length()));
			password += text[i];
		}
		System.out.println("system generated password is ..." + password);

		System.out.println();
		System.out.println("Password saved to DB :   " + password);

		BCryptPasswordEncoder passEncoded = new BCryptPasswordEncoder();
		String hashedPass = passEncoded.encode(password);

		System.out.println("encoded pass :   " + hashedPass);

		registerEntity.setPassword(hashedPass);
		registerEntity.setNoOfAttempts(0);
		registerEntity.setDecodedPass(password);

		model.addAttribute("user_id", registerEntity.getUser_id());
		model.addAttribute("email", registerEntity.getEmail());
		model.addAttribute("ph_no", registerEntity.getPh_no());
		model.addAttribute("course", registerEntity.getCourse());
		model.addAttribute("password", hashedPass);
		System.out.println();
		System.out.println("passed to entity :" + password);

		this.registerDAO.saveUser(registerEntity);
		return "userDetails";
	}

	public String validateLogin(RegisterDTO registerDTO, Model model) {

		System.out.println("inside validateLogin under Service :" + this.getClass().getSimpleName());
		RegisterEntity registerEntity = null;
		boolean isPassMatch = false;
		Integer attemptCount = this.registerDAO.checkAttempts(registerDTO.getEmail());
		System.out.println("value of checkAttempts :" + attemptCount);

		registerEntity = this.registerDAO.validateEmail(registerDTO, model);
		if (Objects.isNull(registerEntity)) {
			return "checkEmail";
		}
		System.out.println("this is under service got from DAO registerEntity :" + registerEntity);
		System.out.println();
		System.out.println("details entered by usser :" + registerDTO);
		BCryptPasswordEncoder decoder = new BCryptPasswordEncoder();

		String passwordDecoded = decoder.encode(registerDTO.getPassword());
		System.out.println("Password from Database  : " + passwordDecoded);

		isPassMatch = decoder.matches(registerDTO.getPassword(), registerEntity.getPassword());
		System.out.println("value of isPassMatch in case of invalid email :" + isPassMatch);

		if (attemptCount <= 3) {
			if (registerEntity.getEmail().equals(registerDTO.getEmail()) && isPassMatch)
				return "allow";
			else if (registerEntity.getEmail() != registerDTO.getEmail()
					&& registerEntity.getPassword() != registerDTO.getPassword()) {

				System.out.println("inside validateLogin in ServiceDAOImpl with email and password..."
						+ registerDTO.getEmail() + "/t" + registerDTO.getPassword() + "/n");
				System.out.println("Check Email or Password......");
				attemptCount++;
				System.out.println("number of attempts :" + attemptCount);
				this.registerDAO.addAttempts(registerDTO.getEmail(), attemptCount);
				return "somethingWrong";
			}

		}

		else if (attemptCount > 3) {
			System.out.println("you have attempted more than 3 times..");
			return "blocked";
		}
		return null;
	}

}
