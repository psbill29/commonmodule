package com.xworks.commonmodule.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xworks.commonmodule.dto.RegisterDTO;
import com.xworks.commonmodule.entity.RegisterEntity;
import com.xworks.commonmodule.service.ForgotPasswordService;

@Component
@RequestMapping("/")
public class ForgotPasswordController {

	private static final Logger log = Logger.getLogger(RegisterController.class);

	@Autowired
	private ForgotPasswordService forgotPasswordService;

	public ForgotPasswordController() {
		log.info("invoked Controller :" + this.getClass().getSimpleName());
	}

	@RequestMapping("/forgot")
	public String toResetPage(Model model) {
		model.addAttribute("User", new RegisterDTO());
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
				model.addAttribute("restMessage",
						"Password reset successfuly and has been sent to registered Email adress");
				return "newDetails";
			} else if ("invalid".equals(resetRequired)) {
				log.info("user doesnt exist: ");
				model.addAttribute("doestNotExist", "Invalid email adress please check the mail adress entered");
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
