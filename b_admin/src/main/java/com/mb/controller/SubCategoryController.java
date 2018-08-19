package com.mb.controller;

import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.mb.common.service.CommonService;
import com.mb.persistance.Category;
import com.mb.persistance.Employee;
import com.mb.persistance.Subcategory;

@Controller
@RequestMapping("/subcategory")
public class SubCategoryController {

	private static final Logger LOGGER = Logger.getLogger(CategoryController.class);

	@Autowired
	private CommonService commonService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String category(ModelMap modelMap) {
		try {
			Map<Integer, String> categoryMap = commonService.findAll(Category.class).stream().filter(x -> x.getStatus())
					.collect(Collectors.toMap(Category::getId, Category::getName));
			modelMap.addAttribute("items", categoryMap);
			modelMap.addAttribute("title", "Add Subcategory");
			modelMap.addAttribute("request", new Subcategory());
			return "subCategory";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}

	}

	@PostMapping(value = "/add")
	public String categoryPost(Subcategory subcategory, HttpServletRequest httpServletRequest) {
		try {
			Employee employee = (Employee) httpServletRequest.getSession().getAttribute("activeUser");
			subcategory.setCreatedby(employee.getId());
			subcategory.setCreatedat(new Timestamp(System.currentTimeMillis()));
			commonService.save(subcategory);
			return "redirect:/subcategory/all";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}

	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public String subcategory(ModelMap modelMap) {
		try {
			Map<Integer, String> categoryMap = commonService.findAll(Category.class).stream().filter(x -> x.getStatus())
					.collect(Collectors.toMap(Category::getId, Category::getName));
			Map<Integer, String> employeeMap = commonService.findAll(Employee.class).stream()
					.collect(Collectors.toMap(Employee::getId, Employee::getName));

			List<Subcategory> subcategories = commonService.findAll(Subcategory.class);
			subcategories.forEach(subcategory -> {

				subcategory.setCategoryName(categoryMap.get(subcategory.getCatid()));
				subcategory.setCreatedByName(employeeMap.get(subcategory.getCreatedby()));

			});

			modelMap.addAttribute("subcategories", subcategories);
			return "allSubCategory";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}

	}

	@PostMapping(value = "/update/{id}")
	public String categoryUpdate(@PathVariable("id") Integer id, Subcategory subcategory,
			HttpServletRequest httpServletRequest) {
		try {
			Subcategory subcategory1 = commonService.findOne(Subcategory.class, id);
			BeanUtils.copyProperties(subcategory, subcategory1, "createdat", "createdby");
			commonService.saveOrUpdate(subcategory1);
			return "redirect:/subcategory/all";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}

	}

	@GetMapping(value = "/update/{id}")
	public String categoryUpdateGET(@PathVariable("id") Integer id, ModelMap modelMap) {
		try {
			Map<Integer, String> categoryMap = commonService.findAll(Category.class).stream().filter(x -> x.getStatus())
					.collect(Collectors.toMap(Category::getId, Category::getName));
			Subcategory subcategory = commonService.findOne(Subcategory.class, id);
			modelMap.addAttribute("items", categoryMap);
			modelMap.addAttribute("title", "Update Subcategory");
			modelMap.addAttribute("request", subcategory);
			return "subCategory";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}

	}

	@GetMapping(value = "/bycategory/{id}")
	public void subcategoryBy(@PathVariable("id") Integer id, HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("catid", id);
			List<Subcategory> categories = commonService.findAllByProperties(Subcategory.class, map);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(new Gson().toJson(categories).getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (Exception exception) {
			LOGGER.error("context", exception);
		}

	}

}
