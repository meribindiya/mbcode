package com.mb.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.mb.common.service.CommonService;
import com.mb.info.request.CategoryRequest;
import com.mb.persistance.Category;
import com.mb.persistance.Employee;

@Controller
@RequestMapping("/category")
public class CategoryController {

	@Value("${com.mb.category.file}")
	private String filePath;

	@Value("${com.mb.category.http}")
	private String httpPath;

	@Autowired
	private CommonService commonService;

	private static final Logger LOGGER = Logger.getLogger(CategoryController.class);
	
	private static final String EXCEPTION = "exception";
	
	private static final String CONTEXT = "context";

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String category(ModelMap modelMap) {
		try {
			modelMap.addAttribute("title", "Add Category");
			modelMap.addAttribute("category", new CategoryRequest());
			return "category";
		} catch (Exception exception) {
			LOGGER.error(CONTEXT, exception);
			return EXCEPTION;
		}

	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addCategory(ModelMap modelMap, CategoryRequest categoryRequest, HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("CategoryRequest categoryRequest :: " + categoryRequest);
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("name", categoryRequest.getName());
			if (StringUtils.isEmpty(commonService.checkExistOrNotOnProperties(Category.class, map))) {
				modelMap.addAttribute("categoryRequest", categoryRequest);
				String message = "Category name already exist.";
				modelMap.addAttribute("msg", message);
				return "category";
			}
			Employee employee = (Employee) httpServletRequest.getSession().getAttribute("activeUser");
			Category category = new Category();
			BeanUtils.copyProperties(categoryRequest, category);
			category.setId(null);
			MultipartFile file = categoryRequest.getFile();
			if (!file.isEmpty()) {
				Map<String, String> map2 = commonService.saveFileOnHardrive(file, filePath, httpPath);
				if ("success".equalsIgnoreCase(map2.get("status"))) {
					category.setImage(map2.get("httpPath"));
				}
			}
			Timestamp timestamp = new Timestamp(new Date().getTime());
			category.setCreatedat(timestamp);
			category.setCreatedby(employee.getId());
			category.setUpdatedat(timestamp);
			category.setUpdatedby(employee.getId());
			LOGGER.info("category :: " + category);
			commonService.save(category);
			String message = "Category saved successfully.";
			redirectAttributes.addFlashAttribute("msg", message);
			return "redirect:/category/all";
		} catch (Exception exception) {
			LOGGER.error(CONTEXT, exception);
			return EXCEPTION;
		}
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String updateCategory(@PathVariable("id") Integer id, ModelMap modelMap, CategoryRequest categoryRequest,
			HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
		LOGGER.info("CategoryRequest :: " + categoryRequest);
		try {

			Category category = commonService.findOne(Category.class, categoryRequest.getId());
			if (!category.getName().equalsIgnoreCase(categoryRequest.getName())) {
				Map<String, Object> map = new HashMap<>();
				map.put("name", categoryRequest.getName());
				if (StringUtils.isEmpty(commonService.checkExistOrNotOnProperties(Category.class, map))) {
					modelMap.addAttribute("categoryRequest", categoryRequest);
					String message = "Category name already exist.";
					modelMap.addAttribute("msg", message);
					return "category";
				}
			}

			Employee employee = (Employee) httpServletRequest.getSession().getAttribute("activeUser");
			BeanUtils.copyProperties(categoryRequest, category, "id");
			MultipartFile file = categoryRequest.getFile();
			if (!file.isEmpty()) {
				Map<String, String> map2 = commonService.saveFileOnHardrive(file, filePath, httpPath);
				if ("success".equalsIgnoreCase(map2.get("status"))) {
					category.setImage(map2.get("httpPath"));
				}
			}
			Timestamp timestamp = new Timestamp(new Date().getTime());
			category.setUpdatedat(timestamp);
			category.setUpdatedby(employee.getId());
			category.setId(id);
			LOGGER.info("category :: " + category);
			commonService.saveOrUpdate(category);
			String message = "Category updated successfully.";
			redirectAttributes.addFlashAttribute("msg", message);
			return "redirect:/category/all";
		} catch (Exception exception) {
			LOGGER.error(CONTEXT, exception);
			return EXCEPTION;
		}
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String category(@PathVariable("id") Integer id, ModelMap modelMap) {
		LOGGER.info("Category Id  :: " + id);
		try {
			Category category = commonService.findOne(Category.class, id);
			LOGGER.info("Category :: " + category);
			CategoryRequest categoryRequest = new CategoryRequest();
			BeanUtils.copyProperties(category, categoryRequest);
			LOGGER.info("CategoryRequest :: " + categoryRequest);
			modelMap.addAttribute("title", "Update Category");
			modelMap.addAttribute("category", categoryRequest);
			return "category";
		} catch (Exception exception) {
			LOGGER.error(CONTEXT, exception);
			return EXCEPTION;
		}

	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public String categoryAll(ModelMap modelMap, @ModelAttribute("msg") String msg) {
		try {
			Map<Integer,String> employeeMap = commonService.findAll(Employee.class).stream().collect(Collectors.toMap(Employee::getId, Employee::getName));
			List<Category> categories = commonService.findAll(Category.class);
			modelMap.addAttribute("msg", msg);
			categories.forEach(category->{
				category.setCreatedByName(employeeMap.get(category.getCreatedby()));
				category.setUpdatedByName(employeeMap.get(category.getUpdatedby()));
			});
			modelMap.addAttribute("categories", categories);
			return "categoryView";
		} catch (Exception exception) {
			LOGGER.error(CONTEXT, exception);
			return EXCEPTION;
		}
	}

}
