package com.mb.controller;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mb.common.service.CommonService;
import com.mb.persistance.Beautician;
import com.mb.persistance.Category;
import com.mb.persistance.Employee;

@Controller
@RequestMapping("/beautician")
public class BeauticianController {

	private static final Logger LOGGER = Logger.getLogger(BeauticianController.class);

	@Autowired
	private CommonService commonService;

	@GetMapping("/add")
	public String add(ModelMap modelMap) {
		try {
			Map<Integer, String> map = commonService.findAll(Category.class).stream().filter(Category::getStatus)
					.collect(Collectors.toMap(Category::getId, Category::getName));
			modelMap.addAttribute("items", map);
			modelMap.addAttribute("request", new Beautician());
			return "beautician-add";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}
	}

	@PostMapping("/add")
	public String add(ModelMap modelMap, Beautician beautician, HttpServletRequest httpServletRequest) {
		try {
			Map<String, Object> conditionMap = new HashMap<>();
			conditionMap.put("mobile", beautician.getMobile());
			List<Beautician> beauticians = commonService.findAllByProperties(Beautician.class, conditionMap);
			if(!beauticians.isEmpty()) {
				Map<Integer, String> map = commonService.findAll(Category.class).stream().filter(Category::getStatus)
						.collect(Collectors.toMap(Category::getId, Category::getName));
				modelMap.addAttribute("items", map);
				modelMap.addAttribute("request", beautician);
				modelMap.addAttribute("msg","Mobile number already exist.");
				return "beautician-add";
			}
			beautician.setCategoryIds(
					beautician.getCatIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
			Employee employee = (Employee) httpServletRequest.getSession().getAttribute("activeUser");
			beautician.setCreatedby(employee.getId());
			beautician.setCreatedat(new Timestamp(System.currentTimeMillis()));
			commonService.save(beautician);
			return "redirect:/beautician/all";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}

	}

	@GetMapping("/update/{id}")
	public String updatePost(@PathVariable("id") Integer id, ModelMap modelMap) {
		try {
			Map<Integer, String> map = commonService.findAll(Category.class).stream().filter(Category::getStatus)
					.collect(Collectors.toMap(Category::getId, Category::getName));
			Beautician beautician = commonService.findOne(Beautician.class, id);
			beautician.setCatIds(Arrays.asList(beautician.getCategoryIds().split(",")).stream()
					.mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
			modelMap.addAttribute("items", map);
			modelMap.addAttribute("request", beautician);
			return "beautician-add";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}
	}

	@PostMapping("/update/{id}")
	public String update(@PathVariable("id") Integer id, Beautician beautician,ModelMap modelMap) {
		try {
			Beautician beautician2 = commonService.findOne(Beautician.class, id);
			if (beautician2.getMobile().intValue() != beautician.getMobile().intValue()) {
				Map<String, Object> conditionMap = new HashMap<>();
				conditionMap.put("mobile", beautician.getMobile());
				List<Beautician> beauticians = commonService.findAllByProperties(Beautician.class, conditionMap);
				if(!beauticians.isEmpty()) {
					Map<Integer, String> map = commonService.findAll(Category.class).stream().filter(Category::getStatus)
							.collect(Collectors.toMap(Category::getId, Category::getName));
					modelMap.addAttribute("items", map);
					modelMap.addAttribute("request", beautician);
					modelMap.addAttribute("msg","Mobile number already exist.");
					return "beautician-add";
				}

			}

			beautician.setCategoryIds(
					beautician.getCatIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
			beautician.setCreatedby(beautician2.getCreatedby());
			beautician.setCreatedat(beautician2.getCreatedat());
			beautician.setId(beautician2.getId());
			commonService.saveOrUpdate(beautician);
			return "redirect:/beautician/all";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}
	}

	@GetMapping("/all")
	public String all(ModelMap modelMap) {
		try {
			List<Beautician> beauticians = commonService.findAll(Beautician.class);
			List<Category> categories = commonService.findAll(Category.class);
			Map<Integer, String> employeeMap = commonService.findAll(Employee.class).stream()
					.collect(Collectors.toMap(Employee::getId, Employee::getName));

			beauticians.forEach(beautician -> {
				beautician.setCreatedByName(employeeMap.get(beautician.getCreatedby()));
				beautician.setCategories(categories.stream().filter(category -> {
					return Arrays.asList(beautician.getCategoryIds().split(",")).stream().mapToInt(Integer::parseInt)
							.boxed().collect(Collectors.toList()).contains(category.getId());
				}).map(Category::getName).collect(Collectors.joining(",")));
			});

			modelMap.addAttribute("beauticians", beauticians);
			return "beauticians";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}

	}

}
