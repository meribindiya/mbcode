package com.mb.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	private static final Logger logger = Logger.getLogger(LoginController.class);

	@RequestMapping(value = { "/login", "/" }, method = RequestMethod.GET)
	public String loginGet(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, ModelMap model) {
		if (error != null) {
			model.addAttribute("error", "Invalid Credentials provided.");
		}
		if (logout != null) {
			model.addAttribute("message", "Logged out successfully.");
		}
		return "login";
	}
}
