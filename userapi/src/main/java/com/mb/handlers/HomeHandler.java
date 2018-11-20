package com.mb.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mb.common.CommonResponse;
import com.mb.persistance.Category;
import com.mb.persistance.Service;
import com.mb.persistance.Subcategory;
import com.mb.service.CommonService;

@RestController
public class HomeHandler {

	private static final Logger LOGGER = LogManager.getLogger(HomeHandler.class);

	private static final String SUCCESS = "success";

	private static final String FAILED = "failed";

	@Autowired
	private CommonService commonService;

	@GetMapping("/categories")
	public ResponseEntity<CommonResponse> categories() {
		CommonResponse commonResponse = new CommonResponse();
		try {
			commonResponse.setStatus(SUCCESS);
			commonResponse.setMessage("");
			commonResponse.setObject(commonService.findAll(Category.class).stream().filter(Category::getStatus)
					.collect(Collectors.toList()));

		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
		}

		return ResponseEntity.ok(commonResponse);
	}

	@GetMapping("/sub-categories/{categoryid}")
	public ResponseEntity<CommonResponse> subcategories(@PathVariable("categoryid") Integer categoryid) {
		CommonResponse commonResponse = new CommonResponse();
		try {
			Map<String, Object> conditionMap = new HashMap<>();
			conditionMap.put("catid", categoryid);
			List<Subcategory> subcategories = commonService.findAllByProperties(Subcategory.class, conditionMap);
			commonResponse.setStatus(SUCCESS);
			commonResponse.setMessage("");
			commonResponse
					.setObject(subcategories.stream().filter(Subcategory::getStatus).collect(Collectors.toList()));

		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
		}

		return ResponseEntity.ok(commonResponse);
	}

	@GetMapping("/services/{categoryid}/{subcategoryid}")
	public ResponseEntity<CommonResponse> services(@PathVariable("categoryid") Integer categoryid,
			@PathVariable("subcategoryid") Integer subcategoryid) {
		LOGGER.info("categoryid :: " + categoryid + " subcategoryid :: " + subcategoryid);
		CommonResponse commonResponse = new CommonResponse();
		try {
			Map<String, Object> conditionMap = new HashMap<>();
			conditionMap.put("catid", categoryid);
			conditionMap.put("subcatid", subcategoryid);
			List<Service> services = commonService.findAllByProperties(Service.class, conditionMap);
			commonResponse.setStatus(SUCCESS);
			commonResponse.setMessage("");
			commonResponse.setObject(services.stream().filter(Service::getStatus).collect(Collectors.toList()));

		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
		}

		return ResponseEntity.ok(commonResponse);
	}

}
