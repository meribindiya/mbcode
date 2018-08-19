package com.mb.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.mb.common.service.CommonService;
import com.mb.info.request.BannerRequest;
import com.mb.persistance.Banner;
import com.mb.persistance.Employee;

@Controller
@RequestMapping("/banner")
public class BannerController {

	private static final Logger LOGGER = Logger.getLogger(BannerController.class);

	@Value("${com.mb.banner.file}")
	private String filePath;

	@Value("${com.mb.banner.http}")
	private String httpPath;

	@Autowired
	private CommonService commonService;

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String banner(ModelMap modelMap, @ModelAttribute("msg") String msg) {
		try {
			modelMap.addAttribute("msg", msg);
			modelMap.addAttribute("title", "Add Banner");
			modelMap.addAttribute("bannerRequest", new BannerRequest());
			return "banner";
		} catch (Exception exception) {
			LOGGER.error(exception);
			return "exception";
		}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String banner(@PathVariable("id") Integer id, RedirectAttributes attributes) {
		try {
			Banner banner = commonService.findOne(Banner.class, id);
			banner.setStatus(false);
			commonService.saveOrUpdate(banner);
			attributes.addFlashAttribute("msg", "Banner successfully deleted.");
			return "redirect:/banner/all";
		} catch (Exception exception) {
			LOGGER.error(exception);
			return "exception";
		}
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String banner(BannerRequest bannerRequest, HttpSession httpSession, RedirectAttributes attributes) {
		try {
			Employee employee = (Employee) httpSession.getAttribute("activeUser");
			for (MultipartFile file : bannerRequest.getFiles()) {
				if (!file.isEmpty()) {
					Banner banner = new Banner();
					banner.setCreatedat(new Timestamp(new Date().getTime()));
					banner.setCreatedby(employee.getId());
					banner.setStatus(true);
					Map<String, String> map = commonService.saveFileOnHardrive(file, filePath, httpPath);
					if ("success".equalsIgnoreCase(map.get("status"))) {
						banner.setFilepath(map.get("filePath"));
						banner.setHttppath(map.get("httpPath"));
					}

					commonService.save(banner);
				}

			}
			attributes.addFlashAttribute("msg", "Banner saved successfully.");
			return "redirect:/banner/upload";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public String banners(ModelMap modelMap, @ModelAttribute("msg") String msg) {
		try {
			modelMap.addAttribute("msg", msg);
			modelMap.addAttribute("title", "All Banners");
			modelMap.addAttribute("banners", commonService.findAll(Banner.class).stream().filter(Banner::getStatus)
					.collect(Collectors.toList()));
			return "banners";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}
	}

}
