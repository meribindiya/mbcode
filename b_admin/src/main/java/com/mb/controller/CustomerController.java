package com.mb.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mb.common.service.CommonService;
import com.mb.persistance.Address;
import com.mb.persistance.User;

@RequestMapping("/customers")
@Controller
public class CustomerController {

	private static final Logger LOGGER = Logger.getLogger(BannerController.class);

	@Autowired
	private CommonService commonService;

	@GetMapping
	public String users(ModelMap modelMap) {
		try {
			List<User> users = commonService.findAll(User.class);
			modelMap.addAttribute("users",users);
			return "users";
		} catch (Exception exception) {
			LOGGER.error("context",exception);
			return "exception";
		}

	}

}
