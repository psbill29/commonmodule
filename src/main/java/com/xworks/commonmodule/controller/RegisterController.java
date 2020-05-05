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

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String toRegister(Model model) {
		model.addAttribute("user", new RegisterDTO());
		return "index";

	}

	@RequestMapping(value = "/registerPage")
	public String fromHome(Model model) {
		model.addAttribute("user", new RegisterDTO());
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
				model.addAttribute("Message",
						"Hello,  " + registerDTO.getUser_name() + "  you are now Registered successfuly");
				return "userDetails";
			}
			return "register";
		} catch (Exception e) {
			log.info("exception found in Register Controller...");
			log.error(e.getMessage(), e);
		}
		return null;

	}

}
