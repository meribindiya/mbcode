package com.mb.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.mb.common.service.CommonService;
import com.mb.persistance.Employee;

public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private CommonService commonService;

	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) throws IOException, ServletException {
		String username = authentication.getName();
		httpServletRequest.getSession().setMaxInactiveInterval(60 * 60 * 60);
		RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
		try {
			Employee employee = commonService.findEmployeeByUsername(username);
			httpServletRequest.getSession().setAttribute("activeUser", employee);
			redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/dashboard");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
