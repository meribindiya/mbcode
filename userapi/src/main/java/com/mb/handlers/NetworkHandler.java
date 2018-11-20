package com.mb.handlers;

import com.mb.common.CommonResponse;
import com.mb.info.req.ReferContactRequest;
import com.mb.info.utilities.Utilities;
import com.mb.persistance.*;
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
@RequestMapping("/network")
public class NetworkHandler {

	private static final Logger LOGGER = LogManager.getLogger(NetworkHandler.class);

	private static final String SUCCESS = "success";

	private static final String FAILED = "failed";

	@Autowired
	private CommonService commonService;

	@Autowired
    private Utilities utils;

    @GetMapping ("/isGoldMember/{userId}")
    public ResponseEntity<CommonResponse> isGoldMember(@PathVariable("userId") Long userId) {

        CommonResponse commonResponse = new CommonResponse();
        try {
            Map<String, String> constants = commonService.findAll(Constants.class).stream()
                    .collect(Collectors.toMap(Constants::getProperty, Constants::getValue));

            int noOfNetworkActiveMembers = this.getActiveNetworkChildUsers(userId).size();
            double earnedAmountPerAccActivatedPerson = (Double.valueOf(constants.get("ACCOUNT_ACTIVE_AMT_CRITERIA")) *
                                                Double.valueOf(constants.get("NETWORK_COMMISION_PERCENTAGE")))/100;

            double earnedAmountCriteria = earnedAmountPerAccActivatedPerson * Integer.valueOf(constants.get("GOLD_MEMBER_ACTIVE_USER_CRITERIA"));

            Map<String, Object> totalEarningCondition = new HashMap<>();
            totalEarningCondition.put("isMissed", false);
            totalEarningCondition.put("userId", userId);

            double totalEarnings = commonService.findSumByProperties(UserEarnings.class, "amount", totalEarningCondition);


            boolean isGoldMember = false;

            if ( totalEarnings > 0 && noOfNetworkActiveMembers > 0 &&
                    (noOfNetworkActiveMembers >= Integer.valueOf(constants.get("GOLD_MEMBER_ACTIVE_USER_CRITERIA"))
                    || totalEarnings >= earnedAmountCriteria))
                isGoldMember = true;

            commonResponse.setStatus(SUCCESS);
            commonResponse.setMessage("");
            commonResponse.setObject(isGoldMember);
        } catch (Exception exception) {
            LOGGER.error("GET network/isGoldMember/" + userId + " :: context", exception);
            commonResponse.setStatus(FAILED);
            commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
        }
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping ("/activeMembers/{userId}")
	public ResponseEntity<CommonResponse> getNetworkChilds(@PathVariable("userId") Long userId) {

		CommonResponse commonResponse = new CommonResponse();
		try {
            commonResponse.setStatus(SUCCESS);
            commonResponse.setMessage("");
            commonResponse.setObject(this.getActiveNetworkChildUsers(userId));
		} catch (Exception exception) {
			LOGGER.error("GET network/activeMembers/" + userId + " :: context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
		}
		return ResponseEntity.ok(commonResponse);
	}

	private List<User> getActiveNetworkChildUsers (Long userId) throws Exception {
        Map<String, String> constants = commonService.findAll(Constants.class).stream()
                .collect(Collectors.toMap(Constants::getProperty, Constants::getValue));


        List<Long> childs = new ArrayList<>();
        this.getChilds(userId, childs, Integer.valueOf(constants.get("NETWORK_DEPTH")));

        if (childs.size() > 0) {
            Map<String, Collection<Long>> usersCondition = new HashMap<>();
            usersCondition.put("user_id", childs);

            return commonService.findAllByInCondition(User.class, usersCondition).stream().
                    filter(user -> {
                        if (user.isActivated())
                            return true;
                        return false;
                    }).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

	private void getChilds (Long userId, List<Long> childList, int depth) {
	    try {
            Map<String, Object> map = new HashMap<>();
            map.put("parentId", userId);
            List<Long> childs = commonService.findAllByProperties(UserNetwork.class, map).stream().map(un->un.getUserId()).collect(Collectors.toList());

            Iterator<Long> i = childs.iterator();
            depth = depth - 1;
            while (i.hasNext() && depth >= 0) {
                Long childId = i.next();
                childList.add(childId);
                this.getChilds(childId, childList, depth);
            }
        } catch (Exception e) {
            LOGGER.error("");
        }
    }

    @GetMapping ("/getParents/{userId}")
    public ResponseEntity<CommonResponse> getNetworkParents(@PathVariable("userId") Long userId) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Map<String, String> constants = commonService.findAll(Constants.class).stream()
                    .collect(Collectors.toMap(Constants::getProperty, Constants::getValue));

            List<Long> parents = new ArrayList<>();
            this.getParents(userId, parents, Integer.valueOf(constants.get("NETWORK_DEPTH")));
            commonResponse.setStatus(SUCCESS);
            commonResponse.setMessage("");
            commonResponse.setObject(parents);
        } catch (Exception exception) {
            LOGGER.error("GET network/getParents/" + userId + " :: context", exception);
            commonResponse.setStatus(FAILED);
            commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
        }
        return ResponseEntity.ok(commonResponse);
    }

    private void getParents (Long userId, List<Long> parentList, int depth) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            List<Long> parents = commonService.findAllByProperties(UserNetwork.class, map).stream().map(un->un.getParentId()).collect(Collectors.toList());

            Iterator<Long> i = parents.iterator();
            depth = depth - 1;
            while (i.hasNext() && depth >= 0) {
                Long parentId = i.next();
                if (parentId != 0)
                    parentList.add(parentId);
                this.getParents(parentId, parentList, depth);
            }
        } catch (Exception e) {
            LOGGER.error("");
        }
    }

    @GetMapping ("/detail/{userId}")
    public ResponseEntity<CommonResponse> getDetails(@PathVariable("userId") Long userId) {

        CommonResponse commonResponse = new CommonResponse();
        try {
            Map<String, Object> details = new HashMap<>();

            // no of active members
            details.put("activeMembersNo", this.getActiveNetworkChildUsers(userId).size());

            // number of referred contacts
            Map<String, Object> referContactCondition = new HashMap<>();
            referContactCondition.put("referrerId", userId);
            List<ReferContact> referContacts = commonService.findAllByProperties(ReferContact.class, referContactCondition);
            details.put("referContactsNo", referContacts.size());

            // find total earnings
            Map<String, Object> totalEarningCondition = new HashMap<>();
            totalEarningCondition.put("isMissed", false);
            totalEarningCondition.put("userId", userId);

            double totalEarnings = commonService.findSumByProperties(UserEarnings.class, "amount", totalEarningCondition);
            details.put("totalEarnings", utils.round(totalEarnings, 2));

            // find missed earnings
            Map<String, Object> missedEarningCondition = new HashMap<>();
            missedEarningCondition.put("isMissed", true);
            missedEarningCondition.put("userId", userId);
            double missedEarning = commonService.findSumByProperties(UserEarnings.class, "amount", missedEarningCondition);
            details.put("missedEarning", utils.round(missedEarning, 2));

            commonResponse.setStatus(SUCCESS);
            commonResponse.setMessage("");
            commonResponse.setObject(details);
        } catch (Exception exception) {
            LOGGER.error("GET network/detail/" + userId + " :: context", exception);
            commonResponse.setStatus(FAILED);
            commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
        }
        return ResponseEntity.ok(commonResponse);
    }
}
