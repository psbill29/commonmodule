package com.xworks.commonmodule.service;

import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.xworks.commonmodule.dao.ForgotPasswordDAO;
import com.xworks.commonmodule.dto.RegisterDTO;
import com.xworks.commonmodule.entity.RegisterEntity;

@Component
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

	@Autowired
	private ForgotPasswordDAO forgotPasswordDAO;

	public ForgotPasswordServiceImpl() {
		System.out.println("invoked service class: " + this.getClass().getSimpleName());
	}

	public String validateUserForPasswordReset(RegisterDTO registerDTO, Model model) {

		System.out.println("inside SERVICE for the Validation of user :" + this.getClass().getSimpleName());

		RegisterEntity registerEntity = new RegisterEntity();
		BeanUtils.copyProperties(registerDTO, registerEntity);

		registerEntity = this.forgotPasswordDAO.checkUserDetails(registerDTO.getEmail(), model);
		System.out.println("entity details:" + registerEntity);
		System.out.println("\n" + "dto details:   " + registerDTO);
		if (registerEntity != null) {
			if (registerEntity.getEmail().equals(registerDTO.getEmail())) {
				System.out.println("inside valid email check in service :");
				if (registerEntity.getNoOfAttempts() != null) {
					if (registerEntity.getNoOfAttempts() > 3) {
						System.out.println("inside count check in service :");

						System.out.println("to the resetting password after fulfilling the password");

						String chars = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
						String newPassword = "";
						int length = 8;

						Random random = new Random();
						char[] text = new char[length];
						for (int i = 0; i < length; i++) {
							text[i] = chars.charAt(random.nextInt(chars.length()));
							newPassword += text[i];
						}
						System.out.println("system generated password is ..." + newPassword);

						System.out.println();
						System.out.println("Password saved to DB :   " + newPassword);
						registerEntity.setPassword(newPassword);

						model.addAttribute("user_id", registerEntity.getUser_id());
						model.addAttribute("email", registerEntity.getEmail());
						model.addAttribute("newPassword", registerEntity.getPassword());
						System.out.println();
						System.out.println("passed to entity :" + newPassword);

						this.forgotPasswordDAO.resetPasswrod(registerEntity);
						this.forgotPasswordDAO.resetNoOfAttempts(registerEntity.getEmail(), model);

						return "doneReset";

					}
					model.addAttribute("userNotBlocked", "try using the password");
					return "tryPassword";
				}

				return "tryLogin";

			}
			model.addAttribute("notValidEmail", "email is not valid");
			return null;

		}

		return "invalid";
	}

}
