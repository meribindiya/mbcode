package com.mb.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.mb.common.service.CommonService;
import com.mb.persistance.Employee;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	private static final Logger logger = Logger.getLogger(EmployeeController.class);

	@Autowired
	private BCryptPasswordEncoder encode;

	@Autowired
	private CommonService commonService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String register(ModelMap modelMap, @ModelAttribute("msg") String message, HttpServletRequest request) {
		try {
			modelMap.addAttribute("employee", new Employee());
			return "eregister";
		} catch (Exception exception) {
			logger.error("context", exception);
			return "exception";
		}
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String register(Employee employeeRequest, HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes, ModelMap modelMap, HttpSession httpSession) {
		try {
			logger.info("Employee employee " + employeeRequest);
			Employee activeUser = (Employee) httpSession.getAttribute("activeUser");
			logger.info("Employee activeUser :: " + activeUser);
			Employee employee = new Employee();
			Map<String, Object> map = new HashMap<>();
			map.put("email", employeeRequest.getEmail());
			if (!commonService.checkExistOrNotOnProperties(Employee.class, map)) {
				modelMap.addAttribute("employee", employeeRequest);
				modelMap.addAttribute("msg", "Email already exist.");
				return "eregister";
			}
			BeanUtils.copyProperties(employeeRequest, employee);
			employee.setPassword(encode.encode(employeeRequest.getPassword()));
			Timestamp timestamp = new Timestamp(new Date().getTime());
			employee.setCreatedat(timestamp);
			employee.setUpdatedat(timestamp);
			employee.setCreatedby(activeUser.getId());
			employee.setUpdatedby(activeUser.getId());
			commonService.save(employee);
			redirectAttributes.addFlashAttribute("msg", "Registration successfully completed.");
			return "redirect:/employee/all";
		} catch (Exception exception) {
			logger.error("context", exception);
			return "exception";
		}
	}

	@RequestMapping(value = "/{employeeid}", method = RequestMethod.GET)
	public String updateGet(ModelMap modelMap, @PathVariable("employeeid") Integer employeeid,
			HttpServletRequest request) {
		try {

			modelMap.addAttribute("employee", commonService.findOne(Employee.class, employeeid));
			return "updateEmployeeDetails";

		} catch (Exception exception) {
			logger.error("context", exception);
			return "exception";
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updatePost(Employee employee, RedirectAttributes redirectAttributes) {
		try {
			employee.setPassword(encode.encode(employee.getPassword()));
			commonService.saveOrUpdate(employee);
			redirectAttributes.addFlashAttribute("msg", "Employee updation successfully completed.");
			return "redirect:/employee/all";
		} catch (Exception exception) {
			logger.error("context", exception);
			return "exception";
		}
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public String showRegisterUsers(@ModelAttribute("msg") String message, ModelMap modelMap,
			HttpServletRequest request, HttpSession httpSession) {
		try {
			List<Employee> employees = commonService.findAll(Employee.class);
			Map<Integer,String> employeeMap = employees.stream().collect(Collectors.toMap(Employee::getId,Employee::getName));
			employees.forEach(employee->{
				employee.setCreatedByName(employeeMap.get(employee.getCreatedby()));
			});
			modelMap.addAttribute("employees", employees);
			modelMap.addAttribute("msg", message);
			return "allemployees";
		} catch (Exception exception) {
			return "exception";
		}
	}

}
