package com.mbemployee.empapi.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mbemployee.empapi.common.CommonResponse;
import com.mbemployee.empapi.persistance.Beautician;
import com.mbemployee.empapi.service.CommonService;

@RestController
public class LoginHandler {

	private static final Logger LOGGER = LogManager.getLogger(LoginHandler.class);

	private static final String SUCCESS = "success";

	private static final String FAILED = "failed";

	@Autowired
	private CommonService commonService;

	@GetMapping("/login")
	public ResponseEntity<CommonResponse> login(@RequestParam("mobile") String mobile,
			@RequestParam("password") String password) {
		CommonResponse commonResponse = new CommonResponse();
		try {
			Map<String, Object> filter = new HashMap<>();
			filter.put("mobile", Long.parseLong(mobile));
			filter.put("password", password);
			List<Beautician> beauticians = commonService.findAllByProperties(Beautician.class, filter);
			if (StringUtils.isEmpty(beauticians) || beauticians.isEmpty()) {
				commonResponse.setStatus(FAILED);
				commonResponse.setMessage("Invalid username and password.");
				commonResponse.setObject(null);
				return ResponseEntity.ok(commonResponse);
			}
			commonResponse.setStatus(SUCCESS);
			commonResponse.setMessage("User Details");
			commonResponse.setObject(beauticians.stream().findFirst().orElse(null));
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
		}

		return ResponseEntity.ok(commonResponse);

	}

}
