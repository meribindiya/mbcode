package com.mb.handlers;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mb.common.CommonResponse;
import com.mb.persistance.Banner;
import com.mb.service.CommonService;

@RestController
@RequestMapping("/banners")
public class BannerHandler {

	private static final Logger LOGGER = LogManager.getLogger(HomeHandler.class);

	private static final String SUCCESS = "success";

	private static final String FAILED = "failed";

	@Autowired
	private CommonService commonService;

	@GetMapping
	public ResponseEntity<CommonResponse> banners() {
		CommonResponse commonResponse = new CommonResponse();
		try {
			commonResponse.setStatus(SUCCESS);
			commonResponse.setMessage("");
			commonResponse.setObject(commonService.findAll(Banner.class).stream().filter(Banner::getStatus)
					.collect(Collectors.toList()));
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
		}
		return ResponseEntity.ok(commonResponse);
	}

}
