package com.mb.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mb.common.service.CommonService;
import com.mb.persistance.Category;
import com.mb.persistance.Employee;
import com.mb.persistance.Service;
import com.mb.persistance.Subcategory;

@RequestMapping("/service")
@Controller
public class ServiceController {

	private static final Logger LOGGER = Logger.getLogger(ServiceController.class);

	@Autowired
	private CommonService commonService;

	@GetMapping("/add")
	public String add(ModelMap modelMap) {
		try {
			Map<Integer, String> categoryMap = commonService.findAll(Category.class).stream().filter(x -> x.getStatus())
					.collect(Collectors.toMap(Category::getId, Category::getName));
			Map<Integer, String> subcategoryMap = commonService.findAll(Subcategory.class).stream()
					.filter(x -> x.getStatus()).collect(Collectors.toMap(Subcategory::getId, Subcategory::getName));

			modelMap.addAttribute("items", categoryMap);
			modelMap.addAttribute("subitems", subcategoryMap);
			modelMap.addAttribute("request", new Service());
			return "service";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}

	}

	@PostMapping("/add")
	public String addPost(ModelMap modelMap, Service service, HttpServletRequest request) {
		try {
			Employee employee = (Employee) request.getSession().getAttribute("activeUser");
			service.setCreatedat(new Timestamp(System.currentTimeMillis()));
			service.setCreatedby(employee.getId());
			service.setUpdatedat(new Timestamp(System.currentTimeMillis()));
			service.setUpdatedby(employee.getId());
			commonService.save(service);
			return "redirect:/service/all";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}

	}

	@GetMapping("/update/{id}")
	public String updateGet(@PathVariable("id") Integer id, ModelMap modelMap) {
		try {
			Map<Integer, String> categoryMap = commonService.findAll(Category.class).stream().filter(x -> x.getStatus())
					.collect(Collectors.toMap(Category::getId, Category::getName));
			Service service = commonService.findOne(Service.class, id);
			Map<String,Object> map = new HashMap<>();
			map.put("catid",service.getCatid());
			Map<Integer, String> subcategoryMap = commonService.findAllByProperties(Subcategory.class,map).stream()
					.collect(Collectors.toMap(Subcategory::getId, Subcategory::getName));
			
			modelMap.addAttribute("items", categoryMap);
			modelMap.addAttribute("subitems", subcategoryMap);
			modelMap.addAttribute("request", service);
			return "service";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}

	}

	@PostMapping("/update/{id}")
	public String updatePost(@PathVariable("id") Integer id, ModelMap modelMap, Service serviceRequest,
			HttpServletRequest request) {
		try {
			Service service = commonService.findOne(Service.class, id);
			BeanUtils.copyProperties(serviceRequest, service, "createdat", "createdby");
			Employee employee = (Employee) request.getSession().getAttribute("activeUser");
			service.setUpdatedat(new Timestamp(System.currentTimeMillis()));
			service.setUpdatedby(employee.getId());
			commonService.saveOrUpdate(service);
			return "redirect:/service/all";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}

	}

	@GetMapping("/all")
	public String all(ModelMap modelMap) {
		try {
			Map<Integer, String> categoryMap = commonService.findAll(Category.class).stream()
					.collect(Collectors.toMap(Category::getId, Category::getName));
			Map<Integer, String> subcategoryMap = commonService.findAll(Subcategory.class).stream()
					.collect(Collectors.toMap(Subcategory::getId, Subcategory::getName));
			Map<Integer, String> employeeMap = commonService.findAll(Employee.class).stream()
					.collect(Collectors.toMap(Employee::getId, Employee::getName));

			List<Service> services = commonService.findAll(Service.class);
			services.forEach(service -> {
				service.setCreatedByName(employeeMap.get(service.getCreatedby()));
				service.setCategoryName(categoryMap.get(service.getCatid()));
				service.setUpdatedByName(employeeMap.get(service.getUpdatedby()));
				service.setSubcategoryName(subcategoryMap.get(service.getSubcatid()));

			});

			modelMap.addAttribute("services", services);
			return "serviceall";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}

	}

}
