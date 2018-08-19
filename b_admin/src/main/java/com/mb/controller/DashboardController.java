package com.mb.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class DashboardController {

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashBoard(ModelMap map, HttpServletRequest request) {
		try {
			return "dashboard";
		} catch (Exception exception) {
			return "exception";
		}

	}

}
