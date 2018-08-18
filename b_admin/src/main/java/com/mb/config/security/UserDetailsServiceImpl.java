package com.mb.config.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.mb.common.service.CommonService;
import com.mb.persistance.Employee;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger LOGGER = Logger.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private CommonService commonService;

	public UserDetails loadUserByUsername(String username) {
		LOGGER.info("start execurtion of loadUserByUsername()" + username);
		Employee employee = null;
		try {
			LOGGER.info("username :: " + username);
			employee = commonService.findEmployeeByUsername(username);
		} catch (Exception e) {
			LOGGER.error("context", e);
		}
		if (employee == null) {
			LOGGER.info("Employee " + employee);
			throw new BadCredentialsException("invalid_username");
		}
		LOGGER.info("Employee :: " + employee);
		List<SimpleGrantedAuthority> au = new ArrayList<>();
		return new User(employee.getEmail(), employee.getPassword(), true, true, true, true, au);
	}
}
