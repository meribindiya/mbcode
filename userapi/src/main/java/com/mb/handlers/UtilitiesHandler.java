package com.mb.handlers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mb.common.CommonResponse;
import com.mb.info.utilities.Utilities;

import io.swagger.annotations.ApiOperation;

@RestController
public class UtilitiesHandler {

	private static final Logger LOGGER = LogManager.getLogger(UtilitiesHandler.class);

	@Autowired
	private Utilities utilites;

	@GetMapping("/slots")
	@ApiOperation(notes = "date format :: dd-MM-yyyy", value = "")
	public ResponseEntity<CommonResponse> slots(@RequestParam("date") String date) {
		CommonResponse commonResponse = new CommonResponse();
		try {
			List<String> slots = utilites.getSlots();

			commonResponse.setStatus("success");
			commonResponse.setObject(slots);

		} catch (Exception exception) {
			commonResponse.setStatus("failed");
			LOGGER.error("context", exception);

		}
		return ResponseEntity.ok(commonResponse);

	}

}
