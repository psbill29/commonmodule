package com.xworks.commonmodule.service;

import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
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
	private MailSender mail;

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
					if (registerEntity.getNoOfAttempts() >= 3) {
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

						// to send credentials to the registered mail
						log.info("entering details for which the mail has to be sent");

						SimpleMailMessage mailMessage = new SimpleMailMessage();
						mailMessage.setTo(registerDTO.getEmail());
						log.info(mailMessage.getTo() + ":this is the mail id to which the password to be sent");

						mailMessage.setSubject("Password Reset successful:" + registerEntity.getUser_id());
						log.info(mailMessage.getSubject() + ": subject of the mail");

						mailMessage.setText("Dear customer, \nAs per your request the password has been reset, "
								+ "and the \n\tnew password is :\t" + newPassword);
						log.info(mailMessage.getText() + ": details which are sent");

						try {
							log.info("about send new forgot :");
							mail.send(mailMessage);
						} catch (MailException e) {
							log.error(e.getMessage(), e);
						}

						model.addAttribute("user_id", registerEntity.getUser_id());
						model.addAttribute("email", registerEntity.getEmail());
						model.addAttribute("newPassword", "Reset password has been sent to registered mail adress");
						System.out.println();
						log.info("passed to entity :" + newPassword);

						log.info(
								"about to enter resetPasswordAndCounts method in DAO to reset password as well as attempts ");
						this.forgotPasswordDAO.resetPasswordAndCounts(registerEntity);

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
