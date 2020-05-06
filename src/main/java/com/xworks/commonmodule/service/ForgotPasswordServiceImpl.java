package com.xworks.commonmodule.service;

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

	@Autowired
	private RandomPassGenerator randomPassGenerator;

	public ForgotPasswordServiceImpl() {
		log.info("invoked service class: " + this.getClass().getSimpleName());
	}

	public String validateUserForPasswordReset(RegisterDTO registerDTO, Model model) {

		log.info("inside SERVICE for the Validation of user :" + this.getClass().getSimpleName());

		RegisterEntity registerEntity = new RegisterEntity();
		BeanUtils.copyProperties(registerDTO, registerEntity);

		try {
			registerEntity = this.forgotPasswordDAO.checkUserDetails(registerDTO.getEmail(), model);
			log.info("value from DAO on calling checkDetails from DB:" + registerEntity);
			log.info("details entered from User(DTO class) :" + registerDTO);
			if (registerEntity != null) {
				if (registerEntity.getEmail().equals(registerDTO.getEmail())) {
					System.out.println("inside valid email check in service :");
					if (registerEntity.getNoOfAttempts() >= 3) {
						log.info("inside count check in service :");

						log.info("to the resetting password after fulfilling the password");

						String decodePassword = this.randomPassGenerator.passGenerator();

						BCryptPasswordEncoder toEncoded = new BCryptPasswordEncoder();
						String passEncoded = toEncoded.encode(decodePassword);

						log.info("encoded newPass :   " + passEncoded);

						registerEntity.setPassword(passEncoded);
						registerEntity.setNoOfAttempts(0);
						registerEntity.setDecodedPass(decodePassword);

						// to send credentials to the registered mail
						log.info("entering details for which the mail has to be sent");

						SimpleMailMessage mailMessage = new SimpleMailMessage();
						mailMessage.setTo(registerDTO.getEmail());
						log.info(mailMessage.getTo() + ":this is the mail id to which the password to be sent");

						mailMessage.setSubject("Password Reset successful:" + registerEntity.getUser_id());
						log.info(mailMessage.getSubject() + ": subject of the mail");

						mailMessage.setText("Dear customer, \nAs per your request the password has been reset, "
								+ "and the \n\tnew password is :\t" + decodePassword
								+ "\nto set new password click here\n"
								+ "http://localhost:8080/com.xworks.commonmodule/reset");
						log.info(mailMessage.getText() + ": details which are sent");

						try {
							log.info("about send new forgot :");
							mail.send(mailMessage);
						} catch (MailException e) {
							log.error(e.getMessage(), e);
						}

						model.addAttribute("user_id", registerEntity.getUser_id());
						model.addAttribute("email", registerEntity.getEmail());
						model.addAttribute("newPassword",
								"One Time password has been sent to registered mail adress,please set new Password");
						System.out.println();
						log.info("passed to entity for hashed:" + passEncoded);
						log.info("passed to entity for normal:" + decodePassword);

						log.info(
								"about to enter resetPasswordAndCounts method in DAO to reset password as well as attempts ");
						this.forgotPasswordDAO.resetPasswordAndCounts(registerEntity);

						return "doneReset";

					}
					model.addAttribute("userNotBlocked", "try using the password");
					log.info("try using the password");
					return "tryPassword";

				}
				model.addAttribute("notValidEmail", "email is not valid");
				log.info("email is not valid");
				return "invalid";

			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	@Override
	public String setNewPassword(RegisterDTO registerDTO, Model model) {

		log.info(this.getClass().getSimpleName() + ":created ");

		boolean isOldPassMatch = false;

		RegisterEntity registerEntity = new RegisterEntity();
		BeanUtils.copyProperties(registerDTO, registerEntity);

		try {
			registerEntity = this.forgotPasswordDAO.checkUserDetails(registerDTO.getEmail(), model);

			log.info("value from DAO on calling checkDetails from DB:" + registerEntity);
			log.info("details entered from User(DTO class) :" + registerDTO);

			if (registerEntity != null) {
				BCryptPasswordEncoder encodeOldPassword = new BCryptPasswordEncoder();

				String oldPassEncoded = encodeOldPassword.encode(registerDTO.getPassword());
				log.info("old password encoded :" + oldPassEncoded);

				isOldPassMatch = encodeOldPassword.matches(registerDTO.getPassword(), registerEntity.getPassword());

				log.info("password entered decoded is :" + isOldPassMatch);
				System.out.println();
				if (isOldPassMatch == true) {

					BCryptPasswordEncoder toEncoded = new BCryptPasswordEncoder();
					String passEncoded = toEncoded.encode(registerDTO.getNewPassword());

					log.info("encoded newPass :   " + passEncoded);

					registerEntity.setPassword(passEncoded);
					registerEntity.setNoOfAttempts(0);
					registerEntity.setDecodedPass(registerDTO.getNewPassword());

					log.info("entering details for which the mail has to be sent");

					SimpleMailMessage mailMessage = new SimpleMailMessage();
					mailMessage.setTo(registerDTO.getEmail());
					log.info(mailMessage.getTo() + ":this is the mail id to which the password to be sent");

					mailMessage.setSubject("Password Reset successful:" + registerEntity.getUser_id());
					log.info(mailMessage.getSubject() + ": subject of the mail");

					mailMessage.setText("Dear customer, \nAs per your request the password has been set, "
							+ "and the \n\tOne Time password is :\t" + registerDTO.getNewPassword());
					log.info(mailMessage.getText() + ": details which are sent");

					try {
						log.info("about send new forgot :");
						mail.send(mailMessage);
					} catch (MailException e) {
						log.error(e.getMessage(), e);
					}

					this.forgotPasswordDAO.resetPasswordAndCounts(registerEntity);
					return "userPasswordSet";

				}

				return "invalidPassword";
			}
			return "invalid";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;

	}
}
