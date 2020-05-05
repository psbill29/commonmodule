package com.xworks.commonmodule.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xworks.commonmodule.dto.RegisterDTO;
import com.xworks.commonmodule.entity.RegisterEntity;
import com.xworks.commonmodule.service.ForgotPasswordService;
import com.xworks.commonmodule.service.ServiceRegister;

@Component
@RequestMapping("/")
public class LoginController {
	private static final Logger log = Logger.getLogger(RegisterController.class);

	@Autowired
	private ServiceRegister serviceRegister;

	public LoginController() {
		log.info("invoked Controller :" + this.getClass().getSimpleName());
	}

	@RequestMapping(value = "/loginPage")
	public String toLogin(Model model) {
		model.addAttribute("user", new RegisterDTO());
		return "login";

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginSuccess(Model model, @ModelAttribute("user") RegisterDTO registerDTO) {

		String emailPasswordCheck = null;
		emailPasswordCheck = this.serviceRegister.validateLogin(registerDTO, model);
		// System.out.println("value of emailCheck at controller: " +
		// emailPasswordCheck);
		log.info("value of emailCheck at controller: " + emailPasswordCheck);
		try {
			if ("allow".equals(emailPasswordCheck)) {
				// System.out.println("invoking login:");
				log.info("invoking login:");
				// System.out.println("model attribute:" + registerDTO);
				log.info("model attribute: " + registerDTO);

				return "home";

			} else if ("somethingWrong".equals(emailPasswordCheck)) {
				// System.out.println("either password or email is wrong...");
				log.error("either password or email is wrong...");
				model.addAttribute("Message", "Please check entered Email and Password ");
				return "login";
			} else if ("blocked".equals(emailPasswordCheck)) {
				// System.out.println("3 attempts done...");
				log.error("3 attempts done...");
				model.addAttribute("Message", "You have made already 3 attempts, Please try resetting password");
				return "login";
			} else if ("checkEmail".equals(emailPasswordCheck))
				// System.out.println("check entered email adress...");
				log.error("check entered email adress...");
			model.addAttribute("Message", "Please check email id entered and try again");
			return "login";
		} catch (Exception e) {
			// System.out.println("Exception found in the login controller");
			log.info("Exception found in the login controller");
			log.error(e.getMessage(), e);
		}
		return null;

	}

}
