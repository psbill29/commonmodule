package com.xworks.commonmodule.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xworks.commonmodule.dto.RegisterDTO;
import com.xworks.commonmodule.entity.RegisterEntity;
import com.xworks.commonmodule.service.ForgotPasswordService;
import com.xworks.commonmodule.service.ServiceRegister;

@Component
@RequestMapping("/")
public class RegisterController {

	private static final Logger log = Logger.getLogger(RegisterController.class);

	@Autowired
	private ServiceRegister serviceRegister;

	@Autowired
	private ForgotPasswordService forgotPasswordService;

	public RegisterController() {
		// System.out.println("invoked controller :" +
		// this.getClass().getSimpleName());
		log.info("invoked Controller :" + this.getClass().getSimpleName());
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String toRegister(Model model) {
		model.addAttribute("user", new RegisterEntity());
		return "index";

	}

	@RequestMapping(value = "/registerPage")
	public String fromHome(Model model) {
		model.addAttribute("user", new RegisterEntity());
		return "register";

	}

	@RequestMapping(value = "/registerSuccess", method = RequestMethod.POST)
	public String registerSuccess(@Valid @ModelAttribute("user") RegisterDTO registerDTO, BindingResult result,
			Model model) {

		if (result.hasErrors()) {

			return "register";
		}

		System.out.println();
		String returnValue = this.serviceRegister.validateUser(registerDTO, model);
		try {
			if (returnValue.equals("userDetails")) {
				// System.out.println("invoking register from controller:");
				log.info("invoking register from controller:");
				// System.out.println("model attribute:" + registerDTO);
				log.info("model attribute :" + registerDTO);
				model.addAttribute("Message", "User Registered successfuly");
				return "userDetails";
			}
			return "register";
		} catch (Exception e) {
			// System.out.println("exception found in Register Controller...");
			log.info("exception found in Register Controller...");
			log.error(e.getMessage(), e);
		}
		return null;

	}

	@RequestMapping(value = "/loginPage")
	public String toLogin(Model model) {
		model.addAttribute("user", new RegisterEntity());
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

				model.addAttribute("Message", "User loggedIn succefully:\n" + "User.ID :" + registerDTO.getEmail()
						+ "\n" + "Password: " + registerDTO.getPassword());
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

	@RequestMapping("/forgot")
	public String toResetPage(Model model) {
		model.addAttribute("User", new RegisterEntity());
		return "forgot";
	}

	@RequestMapping(value = "/forgotPass", method = RequestMethod.POST)
	public String resettingPassword(@Valid @ModelAttribute("User") RegisterDTO registerDTO, BindingResult result,
			Model model) {
		log.info("inside the controller");

		// if (result.hasErrors()) {
		// return "forgot";
		// }
		// NOT WORKING

		String resetRequired = this.forgotPasswordService.validateUserForPasswordReset(registerDTO, model);
		try {
			if ("doneReset".equals(resetRequired)) {
				log.info("going forward to validate and register :");
				log.info("model attribute: " + registerDTO);
				model.addAttribute("restMessage", "Password reset successfuly");
				return "newDetails";
			} else if ("invalid".equals(resetRequired)) {
				log.info("user doesnt exist: ");
				model.addAttribute("doestNotExist", "email doesnt exist");
			} else if ("tryPassword".equals(resetRequired)) {
				log.info("inside controller for resetting the password: user can try with password");
				model.addAttribute("userNotBlocked", "       try using password..");

				return "login";
			} else if ("tryLogin".equals(resetRequired)) {
				model.addAttribute("userNotBlocked", "try logging in using the password");
				return "login";
			}
			return "login";
		} catch (Exception e) {
			log.info("found exception in Forgot Password module...");
			log.error(e.getMessage(), e);
		}
		return null;

	}

}
