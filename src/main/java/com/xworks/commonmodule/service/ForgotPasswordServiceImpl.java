package com.xworks.commonmodule.service;

import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.xworks.commonmodule.dao.ForgotPasswordDAO;
import com.xworks.commonmodule.dto.RegisterDTO;
import com.xworks.commonmodule.entity.RegisterEntity;

@Component
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

	private static final Logger log = Logger.getLogger(ForgotPasswordServiceImpl.class);

	@Autowired
	private ForgotPasswordDAO forgotPasswordDAO;

	public ForgotPasswordServiceImpl() {
		log.info("invoked service class: " + this.getClass().getSimpleName());
	}

	public String validateUserForPasswordReset(RegisterDTO registerDTO, Model model) {

		log.info("inside SERVICE for the Validation of user :" + this.getClass().getSimpleName());

		RegisterEntity registerEntity = new RegisterEntity();
		BeanUtils.copyProperties(registerDTO, registerEntity);

		registerEntity = this.forgotPasswordDAO.checkUserDetails(registerDTO.getEmail(), model);
		log.info("value from DAO on calling checkDetails from DB:" + registerEntity);
		log.info("details entered from User(DTO class) :" + registerDTO);
		if (registerEntity != null) {
			if (registerEntity.getEmail().equals(registerDTO.getEmail())) {
				System.out.println("inside valid email check in service :");
				if (registerEntity.getNoOfAttempts() != null) {
					if (registerEntity.getNoOfAttempts() > 3) {
						log.info("inside count check in service :");

						log.info("to the resetting password after fulfilling the password");

						String chars = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
						String newPassword = "";
						int length = 8;

						Random random = new Random();
						char[] text = new char[length];
						for (int i = 0; i < length; i++) {
							text[i] = chars.charAt(random.nextInt(chars.length()));
							newPassword += text[i];
						}
						log.info("System generated password on resetting the password :" + newPassword);

						System.out.println();

						System.out.println();
						log.info("Password saved to DB :   " + newPassword);

						BCryptPasswordEncoder passEncoded = new BCryptPasswordEncoder();
						String hashedPass = passEncoded.encode(newPassword);

						log.info("encoded newPass :   " + hashedPass);

						registerEntity.setPassword(hashedPass);
						registerEntity.setNoOfAttempts(0);
						registerEntity.setDecodedPass(newPassword);

						model.addAttribute("user_id", registerEntity.getUser_id());
						model.addAttribute("email", registerEntity.getEmail());
						model.addAttribute("newPassword", registerEntity.getPassword());
						System.out.println();
						log.info("passed to entity :" + newPassword);

						log.info("about to enter resetPassword method in DAO ");
						this.forgotPasswordDAO.resetPasswrod(registerEntity);
						
						log.info("about to enter resetNoOfAttempts method in DAO ");
						this.forgotPasswordDAO.resetNoOfAttempts(registerEntity.getEmail(), model);

						return "doneReset";

					}
					model.addAttribute("userNotBlocked", "try using the password");
					log.info("try using the password");
					return "tryPassword";
				}

				return "tryLogin";

			}
			model.addAttribute("notValidEmail", "email is not valid");
			log.info("email is not valid");
			return null;

		}

		return "invalid";
	}

}
