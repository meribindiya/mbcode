package com.mb.handlers;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.mb.common.CommonResponse;
import com.mb.info.req.ReferContactRequest;
import com.mb.persistance.Constants;
import com.mb.persistance.ReferContact;
import com.mb.persistance.User;
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
@RequestMapping("/refer")
public class ReferContactHandler {

	private static final Logger LOGGER = LogManager.getLogger(ReferContactHandler.class);

	private static final String SUCCESS = "success";

	private static final String FAILED = "failed";

	@Autowired
	private CommonService commonService;

	PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

	@PostMapping("/save")
	public ResponseEntity<CommonResponse> referContacts(@RequestBody ReferContactRequest referContactRequest) {
		CommonResponse commonResponse = new CommonResponse();
		try {

			LOGGER.info("POST refer/save :: referContactRequest :: " + referContactRequest);
			List<Long> referContactRequestMobileNumbers = new ArrayList<>();


            referContactRequestMobileNumbers = referContactRequest.getMobileNumbers().stream().map( phone -> {
                Long mobile = null;
                try {
                    mobile =  phoneUtil.parse(phone, "IN").getNationalNumber();
                } catch (NumberParseException e) {
                    LOGGER.info("POST refer/save :: numbers to save :: " + e.getLocalizedMessage());
                }
                return mobile;
            }).collect(Collectors.toList());

			// validating inputs
			if (referContactRequest.getUserId() == null) {
				commonResponse.setStatus(FAILED);
				commonResponse.setMessage(" userID is mandatory.");
				return ResponseEntity.ok(commonResponse);
			}

			if (referContactRequestMobileNumbers == null || referContactRequestMobileNumbers.size() == 0) {
				commonResponse.setStatus(FAILED);
				commonResponse.setMessage("Atleast one mobile no is required.");
				return ResponseEntity.ok(commonResponse);
			}

			//get application constants
			Map<String, String> constants = commonService.findAll(Constants.class).stream()
											.collect(Collectors.toMap(Constants::getProperty, Constants::getValue));

			//all referred numbers by this user.
			Map<String, Object> referContactCondition = new HashMap<>();
			referContactCondition.put("referrerId", referContactRequest.getUserId());
			List<ReferContact> referContacts = commonService.findAllByProperties(ReferContact.class, referContactCondition);
			List<Long> referContactsMobileNumber = referContacts.stream().map(con ->con.getMobile()).collect(Collectors.toList());

			//check how many referred numbers are registered one
			int unregisteredReferredUsers = 0;
			if (referContactsMobileNumber.size() > 0) {
				Map<String, Collection<Long>> registeredRefererredConditionMap = new HashMap<>();
				registeredRefererredConditionMap.put("mobile", referContactsMobileNumber);
				List<User> registeredRefererredUsers = commonService.findAllByInCondition(User.class, registeredRefererredConditionMap);
				unregisteredReferredUsers = referContactsMobileNumber.size() - registeredRefererredUsers.size();
			}


			// get all registered users with the supplied mobile numbers
			Map<String, Collection<Long>> conditionMap = new HashMap<>();
			conditionMap.put("mobile", referContactRequestMobileNumbers);
			List<User> users = commonService.findAllByInCondition(User.class, conditionMap);

			// removing registered nos from the list
			Iterator<User> i = users.iterator();
			while (i.hasNext()) {
				User u = i.next();

                referContactRequestMobileNumbers.remove (u.getMobile());
			}

			LOGGER.info("POST refer/save :: numbers to save :: " + referContactRequestMobileNumbers);

			if (referContactRequestMobileNumbers.size() > (Integer.valueOf(constants.get("MAX_UNREGISTERED_CONTACTS")) - unregisteredReferredUsers)) {
				commonResponse.setStatus(FAILED);
				commonResponse.setMessage("There can be only " +
						Integer.valueOf(constants.get("MAX_UNREGISTERED_CONTACTS")) + " unregistered shared contact. Please share maximum "
						+ (Integer.valueOf(constants.get("MAX_UNREGISTERED_CONTACTS")) - unregisteredReferredUsers));
			} else {
				// saving number in refer contact table.
				for (Long mobileNo: referContactRequestMobileNumbers) {
					ReferContact referContact = new ReferContact();
					referContact.setReferrerId(referContactRequest.getUserId());
					referContact.setMobile(mobileNo);
					try {
						commonService.save(referContact);
					} catch (ConstraintViolationException e) {
						LOGGER.info("POST refer/save :: number already exist in refer contact table :: " + mobileNo);
					}
				}

				commonResponse.setStatus(SUCCESS);
				commonResponse.setMessage("");
			}
		} catch (Exception exception) {
			LOGGER.error("POST refer/save :: context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
		}
		return ResponseEntity.ok(commonResponse);
	}

	@GetMapping ("/delete/{mobile}")
	public ResponseEntity<CommonResponse> deleteContact(@PathVariable("mobile") Long mobile) {
		CommonResponse commonResponse = new CommonResponse();
		try {

			// check if refer contact exist with this mobile number
			Map<String, Object> referContactByMobile = new HashMap<>();
			referContactByMobile.put("mobile", mobile);

			List<ReferContact> referContacts = commonService.findAllByProperties(ReferContact.class, referContactByMobile);

			if (referContacts.isEmpty()) { // no refer contact found by mobile no
				commonResponse.setStatus(FAILED);
				commonResponse.setMessage("No contact found with this mobile no.");
			} else {
				// check if requested mobile no is already has been registered.
				Map<String, Object> userByMobile = new HashMap<>();
				userByMobile.put("mobile", mobile);

				List<User> users = commonService.findAllByProperties(User.class, userByMobile);

				if (!users.isEmpty()) { // already registered contact
					commonResponse.setStatus(FAILED);
					commonResponse.setMessage("Can not delete as contact has already been registered.");
				} else {
					ReferContact referContact = referContacts.get(0);
					commonService.delete(referContact);

					commonResponse.setStatus(SUCCESS);
					commonResponse.setMessage("");
				}
			}
		} catch (Exception exception) {
			LOGGER.error("GET network/delete/" + mobile + " :: context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
		}
		return ResponseEntity.ok(commonResponse);
	}

	@GetMapping ("/get/{userId}")
	public ResponseEntity<CommonResponse> getSharedContacts (@PathVariable("userId") Long userId) {
		CommonResponse commonResponse = new CommonResponse();

		try {
            //all referred numbers by this user.
            Map<String, Object> referContactCondition = new HashMap<>();
            referContactCondition.put("referrerId", userId);
            List<ReferContact> referContacts = commonService.findAllByProperties(ReferContact.class, referContactCondition);
            List<Long> referContactsMobileNumber = referContacts.stream().map(con ->con.getMobile()).collect(Collectors.toList());

            // get users by mobile numbers
            if (referContactsMobileNumber.size() > 0) {
                Map<String, Collection<Long>> registeredRefererredConditionMap = new HashMap<>();
                registeredRefererredConditionMap.put("mobile", referContactsMobileNumber);
                Set<User> registeredRefererredUsers = commonService.findAllByInCondition(User.class, registeredRefererredConditionMap).stream().collect(Collectors.toSet());

                Iterator<ReferContact> i = referContacts.iterator();

                // adding other non registered users from refer list
                while (i.hasNext()) {
                    ReferContact referContact = i.next();

                    User user = new User();
                    user.setMobile(referContact.getMobile());
                    registeredRefererredUsers.add(user);
                }
                commonResponse.setStatus(SUCCESS);
                List<User> referContactsToReturn = registeredRefererredUsers.stream().collect(Collectors.toList());
				Collections.sort(referContactsToReturn, new ReferContactsComparator());
                commonResponse.setObject(referContactsToReturn);
            } else {
                commonResponse.setStatus(SUCCESS);
                commonResponse.setObject(new ArrayList<>());
            }
		} catch (Exception exception) {
			LOGGER.error("POST refer/get :: context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
		}

		return ResponseEntity.ok(commonResponse);
	}

    private class ReferContactsComparator implements Comparator<User>{

        @Override
        public int compare(User user1, User user2) {
            if (user1.getUser_id() == null  )
                return -1;
            else if (user2.getUser_id() == null)
                return 1;
            else
                return 0;
        }
    }
}
