package com.mb.handlers;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.mb.common.CommonResponse;
import com.mb.info.req.ReferContactRequest;
import com.mb.persistance.Constants;
import com.mb.persistance.ReferContact;
import com.mb.persistance.User;
import com.mb.persistance.UserEarnings;
import com.mb.service.CommonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/earning")
public class UserEarningHandler {

	private static final Logger LOGGER = LogManager.getLogger(UserEarningHandler.class);

	private static final String SUCCESS = "success";

	private static final String FAILED = "failed";

	@Autowired
	private CommonService commonService;

	@GetMapping ("/get/{userId}/{isMissed}")
	public ResponseEntity<CommonResponse> getEarning (@PathVariable("userId") Long userId, @PathVariable("isMissed") boolean isMissed) {
		CommonResponse commonResponse = new CommonResponse();

		try {
            //all earning by user
            Map<String, Object> earningCondition = new HashMap<>();
			earningCondition.put("userId", userId);
            earningCondition.put("isMissed", isMissed);


            List<UserEarnings> earnings = commonService.findAllByProperties(UserEarnings.class, earningCondition);

            commonResponse.setStatus(SUCCESS);
            commonResponse.setObject(earnings);
		} catch (Exception exception) {
			LOGGER.error("GET earning/get :: context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
		}

		return ResponseEntity.ok(commonResponse);
	}
}
