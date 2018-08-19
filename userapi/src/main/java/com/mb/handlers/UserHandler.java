package com.mb.handlers;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mb.common.CommonResponse;
import com.mb.executor.CustomExecutor;
import com.mb.info.req.AddressRequest;
import com.mb.info.req.OTPVerificationRequest;
import com.mb.info.req.UserFcmRequest;
import com.mb.info.req.UserRequest;
import com.mb.info.utilities.Utilities;
import com.mb.persistance.Address;
import com.mb.persistance.User;
import com.mb.persistance.UserFCM;
import com.mb.persistance.UserOTP;
import com.mb.service.CommonService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
@CrossOrigin(allowCredentials="true")
public class UserHandler {

	private static final Logger LOGGER = LogManager.getLogger(HomeHandler.class);

	private static final String SUCCESS = "success";

	private static final String FAILED = "failed";

	@Autowired
	private CommonService commonService;

	@Autowired
	private Utilities utilities;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@GetMapping("/{mobile}")
	public ResponseEntity<CommonResponse> login(@PathVariable("mobile") Long mobile) {
		CommonResponse commonResponse = new CommonResponse();
		try {
			Map<String, Object> conditionMap = new HashMap<>();
			conditionMap.put("mobile", mobile);
			List<User> users = commonService.findAllByProperties(User.class, conditionMap);
			if (users.isEmpty()) {
				commonResponse.setStatus(SUCCESS);
				commonResponse.setMessage("REGISTRATION_SCREEN");
				return ResponseEntity.ok(commonResponse);
			}
			User user = users.stream().findFirst().get();
			String otp = utilities.getRandomNumericString(4);
			UserOTP userOTP = new UserOTP();
			userOTP.setMobile(mobile);
			userOTP.setMobileotp(Integer.parseInt(otp));
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			userOTP.setCreatedat(timestamp);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(timestamp);
			calendar.add(Calendar.MINUTE, 30);
			userOTP.setMobileotpExpiretime(new Timestamp(calendar.getTime().getTime()));
			commonService.save(userOTP);
			String message = otp + " is your OTP.";
			String smsurl = "http://newsms.designhost.in/index.php/smsapi/httpapi/?" + "uname="
					+ URLEncoder.encode("meribindiya", "UTF-8") + "&password=" + URLEncoder.encode("123456", "UTF-8")
					+ "&sender=" + "BINDYA&route=TA&msgtype=1&receiver=" + URLEncoder.encode(mobile.toString(), "UTF-8")
					+ "&sms=" + URLEncoder.encode(message, "UTF-8");
			taskExecutor.execute(new CustomExecutor(utilities, smsurl));
			Map<String, Object> map = new HashMap<>();
			map.put("otp", otp);
			map.put("user",user);
			commonResponse.setObject(map);
			commonResponse.setStatus(SUCCESS);
			commonResponse.setMessage("OTP_SCREEN");

		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
		}
		return ResponseEntity.ok(commonResponse);
	}

	@PostMapping("/register")
	public ResponseEntity<CommonResponse> register(@RequestBody UserRequest userRequest) {
		CommonResponse commonResponse = new CommonResponse();
		try {
			User user = new User();
			LOGGER.info(" UserRequest userRequest :: " + userRequest);
			Map<String, Object> conditionMap = new HashMap<>();
			conditionMap.put("mobile", userRequest.getMobile());
			List<User> users = commonService.findAllByProperties(User.class, conditionMap);
			if (!users.isEmpty()) {
				commonResponse.setStatus(FAILED);
				commonResponse.setMessage("Mobile number already exist.\nPlease try with different mobile number");
				return ResponseEntity.ok(commonResponse);
			}
			BeanUtils.copyProperties(userRequest, user);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			user.setCreatedat(timestamp);
			user.setUpdatedat(timestamp);
			Long userid = (Long)commonService.save(user);
			user.setUser_id(userid);
			String otp = utilities.getRandomNumericString(4);
			UserOTP userOTP = new UserOTP();
			userOTP.setMobile(userRequest.getMobile());
			userOTP.setMobileotp(Integer.parseInt(otp));
			userOTP.setCreatedat(timestamp);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(timestamp);
			calendar.add(Calendar.MINUTE, 30);
			userOTP.setMobileotpExpiretime(new Timestamp(calendar.getTime().getTime()));
			commonService.save(userOTP);
			String message = otp + " is your OTP.";
			String smsurl = "http://newsms.designhost.in/index.php/smsapi/httpapi/?" + "uname="
					+ URLEncoder.encode("meribindiya", "UTF-8") + "&password=" + URLEncoder.encode("123456", "UTF-8")
					+ "&sender=" + "BINDYA&route=TA&msgtype=1&receiver="
					+ URLEncoder.encode(userRequest.getMobile().toString(), "UTF-8") + "&sms="
					+ URLEncoder.encode(message, "UTF-8");
			taskExecutor.execute(new CustomExecutor(utilities, smsurl));
			Map<String, Object> map = new HashMap<>();
			map.put("otp", otp);
			map.put("user",user);
			commonResponse.setObject(map);
			commonResponse.setStatus(SUCCESS);
			commonResponse.setMessage("success");

		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
		}
		return ResponseEntity.ok(commonResponse);
	}

	@PostMapping("/fcm")
	public ResponseEntity<CommonResponse> fcm(@RequestBody UserFcmRequest userFcmRequest) {
		LOGGER.info(" UserFcmRequest userFcmRequest :: " + userFcmRequest);
		CommonResponse commonResponse = new CommonResponse();
		try {
			UserFCM fcm = new UserFCM();
			BeanUtils.copyProperties(userFcmRequest, fcm);
			commonService.saveOrUpdate(fcm);
			commonResponse.setStatus(SUCCESS);
			commonResponse.setMessage("Save Successfully");
			return new ResponseEntity<>(commonResponse, HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
			return new ResponseEntity<>(commonResponse, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/otp/verification")
	public ResponseEntity<CommonResponse> otpVerify(@RequestBody OTPVerificationRequest otpVerificationRequest) {
		LOGGER.info("OTPVerificationRequest :: " + otpVerificationRequest);
		CommonResponse commonResponse = new CommonResponse();
		try {
			Map<String, Object> conditionMap = new HashMap<>();
			conditionMap.put("mobile", otpVerificationRequest.getMobile());
			List<UserOTP> userOTPs = commonService.findAllByProperties(UserOTP.class, conditionMap);
			if (userOTPs.isEmpty()) {
				commonResponse.setStatus(FAILED);
				commonResponse.setMessage("Please resend otp.");
			}
			UserOTP userOTP = userOTPs.get(userOTPs.size() - 1);
			LOGGER.info("UserOTP :: " + userOTP);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			if (timestamp.after(userOTP.getMobileotpExpiretime())) {
				commonResponse.setStatus(FAILED);
				commonResponse.setMessage("Your otp is expired.\nPlease try again.");
				return new ResponseEntity<>(commonResponse, HttpStatus.OK);
			}
			if (otpVerificationRequest.getOtp().intValue() != userOTP.getMobileotp()) {
				commonResponse.setStatus(FAILED);
				commonResponse.setMessage("Invalid OTP");
				return new ResponseEntity<>(commonResponse, HttpStatus.OK);
			}
			conditionMap.clear();
			conditionMap.put("mobile", otpVerificationRequest.getMobile());
			User user = commonService.findAllByProperties(User.class, conditionMap).get(0);
			commonResponse.setStatus(SUCCESS);
			commonResponse.setMessage("Login successfully");
			commonResponse.setObject(user);
			return new ResponseEntity<>(commonResponse, HttpStatus.OK);

		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
			return new ResponseEntity<>(commonResponse, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/address/add")
	public ResponseEntity<CommonResponse> addAddress(@RequestBody AddressRequest addressRequest) {
		LOGGER.info("AddressRequest :: " + addressRequest);
		CommonResponse commonResponse = new CommonResponse();
		try {
			Address address = new Address();
			BeanUtils.copyProperties(addressRequest, address);
			address.setCreatedAt(new Timestamp(System.currentTimeMillis()));
			address.setStatus(true);
			commonService.save(address);
			commonResponse.setStatus(SUCCESS);
			commonResponse.setMessage("Saved successfully.");
			return new ResponseEntity<>(commonResponse, HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
			return new ResponseEntity<>(commonResponse, HttpStatus.OK);
		}
	}

	@GetMapping(value = "/address/all/{userid}")
	public ResponseEntity<CommonResponse> addAddress(@PathVariable("userid") Long userId) {
		LOGGER.info("addAddress :: " + userId);
		CommonResponse commonResponse = new CommonResponse();
		try {
			Map<String, Object> conditionMap = new HashMap<>();
			conditionMap.put("userid", userId);
			List<Address> addresses = commonService.findAllByProperties(Address.class, conditionMap);
			commonResponse.setStatus(SUCCESS);
			commonResponse.setObject(addresses.stream().filter(Address::getStatus).collect(Collectors.toList()));
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
		}
		return new ResponseEntity<>(commonResponse, HttpStatus.OK);
	}

	@PostMapping(value = "/update")
	public ResponseEntity<CommonResponse> update(@RequestBody UserRequest userRequest) {
		LOGGER.info("UserRequest userRequest :: " + userRequest);
		CommonResponse commonResponse = new CommonResponse();
		try {
			Map<String, Object> conditionMap = new HashMap<>();
			conditionMap.put("mobile", userRequest.getMobile());
			List<User> users = commonService.findAllByProperties(User.class, conditionMap);
			if (users.isEmpty()) {
				commonResponse.setStatus(FAILED);
				commonResponse.setMessage("User details not found.");
				return new ResponseEntity<>(commonResponse, HttpStatus.OK);
			}
			User user = users.get(0);
			BeanUtils.copyProperties(userRequest, user);
			commonService.saveOrUpdate(user);
			commonResponse.setStatus(SUCCESS);
			commonResponse.setMessage("User successfully updated.");
			commonResponse.setObject(user);
			return new ResponseEntity<>(commonResponse, HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
			return new ResponseEntity<>(commonResponse, HttpStatus.OK);
		}

	}

	@GetMapping(value = "/address/delete/{addressid}")
	@ApiOperation(value = "Login", notes = "Pass Address id to detete address")
	public ResponseEntity<CommonResponse> deleteAddress(@PathVariable("addressid") Integer addressid) {
		LOGGER.info("remove address  " + addressid);
		CommonResponse commonResponse = new CommonResponse();
		try {
			Address address = commonService.findOne(Address.class, addressid);
			if (StringUtils.isEmpty(address)) {
				commonResponse.setStatus(FAILED);
				commonResponse.setMessage("Address details not found againt address id.");
				return new ResponseEntity<>(commonResponse, HttpStatus.OK);
			}
			address.setStatus(false);
			commonService.saveOrUpdate(address);
			commonResponse.setStatus(SUCCESS);
			commonResponse.setMessage("Address successfully deleted");
			return new ResponseEntity<>(commonResponse, HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
			return new ResponseEntity<>(commonResponse, HttpStatus.OK);
		}
	}

	@GetMapping("/resend-otp/{mobile}")
	public ResponseEntity<CommonResponse> resendOTP(@PathVariable("mobile") Long mobile) {
		CommonResponse responseObj = new CommonResponse();
		try {
			UserOTP userOTP = new UserOTP();
			userOTP.setMobile(mobile);
			String otp = utilities.getRandomNumericString(4);
			userOTP.setMobileotp(Integer.parseInt(otp));
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			userOTP.setCreatedat(timestamp);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(timestamp);
			calendar.add(Calendar.MINUTE, 30);
			userOTP.setMobileotpExpiretime(new Timestamp(calendar.getTime().getTime()));
			commonService.save(userOTP);
			String message = otp + " is your OTP.";
			String smsurl = "http://newsms.designhost.in/index.php/smsapi/httpapi/?" + "uname="
					+ URLEncoder.encode("meribindiya", "UTF-8") + "&password=" + URLEncoder.encode("123456", "UTF-8")
					+ "&sender=" + "BINDYA&route=TA&msgtype=1&receiver=" + URLEncoder.encode(mobile.toString(), "UTF-8")
					+ "&sms=" + URLEncoder.encode(message, "UTF-8");
			taskExecutor.execute(new CustomExecutor(utilities, smsurl));
			responseObj.setStatus(SUCCESS);
			responseObj.setMessage("OTP successfully resend");
			return new ResponseEntity<>(responseObj, HttpStatus.OK);
		} catch (Exception exception) {
			exception.printStackTrace();
			responseObj.setStatus(FAILED);
			responseObj.setMessage("Oops! something went wrong.");
			return new ResponseEntity<>(responseObj, HttpStatus.OK);
		}
	}

}
