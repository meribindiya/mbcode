package com.mb.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mb.common.service.CommonService;
import com.mb.info.request.ChangePasswordRequest;
import com.mb.persistance.Employee;

@Controller
@RequestMapping("/password")
public class ChangePasswordController {

	private static final Logger LOGGER = Logger.getLogger(ChangePasswordController.class);

	@Autowired
	private BCryptPasswordEncoder encode;

	@Autowired
	private CommonService commonService;

	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public String changePassword(Model model, @ModelAttribute("msg") String message) {
		try {
			ChangePasswordRequest changePassword = new ChangePasswordRequest();
			model.addAttribute("cp", changePassword);
			model.addAttribute("msg", message);
			return "cp";
		} catch (Exception e) {
			LOGGER.error("context", e);
			return "exception";
		}
	}

	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public String changePassword(ChangePasswordRequest changePassword, HttpSession httpSession,
			RedirectAttributes attributes) {
		try {
			Employee employee = (Employee) httpSession.getAttribute("activeUser");
			employee.setPassword(encode.encode(changePassword.getPassword()));
			commonService.saveOrUpdate(employee);
			attributes.addFlashAttribute("msg", "Password successfully changed.");
			return "redirect:/password/change";
		} catch (Exception e) {
			LOGGER.error("context", e);
			return "exception";
		}
	}

}
