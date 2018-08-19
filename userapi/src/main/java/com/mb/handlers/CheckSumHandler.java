package com.mb.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mb.common.CommonResponse;
import com.mb.info.req.CheckSumGenetareRequest;
import com.mb.info.req.CheckSumValidateRequest;
import com.paytm.pg.merchant.CheckSumServiceHelper;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/paytm")
@Api("CheckSumController")
public class CheckSumHandler {

	private static final Logger LOGGER = LogManager.getLogger(CheckSumHandler.class);

	@Value("${com.paytm.MID}")
	private String MID;

	@Value("${com.paytm.MERCAHNTKEY}")
	private String MERCAHNTKEY;

	@Value("${com.paytm.INDUSTRY_TYPE_ID}")
	private String INDUSTRY_TYPE_ID;

	@Value("${com.paytm.CHANNLE_ID}")
	private String CHANNLE_ID;

	@Value("${com.paytm.WEBSITE}")
	private String WEBSITE;

	@Value("${com.paytm.CALLBACK_URL}")
	private String CALLBACK_URL;

	@RequestMapping(value = "/checksum/generate", method = RequestMethod.POST)
	public ResponseEntity<CommonResponse> gc(@RequestBody CheckSumGenetareRequest checkSumGenetareRequest) {
		LOGGER.info("CheckSumGenetareRequest :: " + checkSumGenetareRequest);
		CommonResponse responseObj = new CommonResponse();
		try {
			LOGGER.info("MID :: " + MID);
			LOGGER.info("MERCAHNTKEY :: " + MERCAHNTKEY);
			LOGGER.info("INDUSTRY_TYPE_ID :: " + INDUSTRY_TYPE_ID);
			LOGGER.info("CHANNLE_ID :: " + CHANNLE_ID);
			LOGGER.info("WEBSITE :: " + WEBSITE);
			LOGGER.info("CALLBACK_URL :: " + CALLBACK_URL);

			TreeMap<String, String> paramMap = new TreeMap<String, String>();
			paramMap.put("MID", MID);
			paramMap.put("ORDER_ID", checkSumGenetareRequest.getOrderId() + "");
			paramMap.put("CUST_ID", checkSumGenetareRequest.getUserId() + "");
			paramMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
			paramMap.put("CHANNEL_ID", CHANNLE_ID);
			paramMap.put("TXN_AMOUNT", String.valueOf(checkSumGenetareRequest.getFinalAmount()));
			paramMap.put("WEBSITE", WEBSITE);
			paramMap.put("EMAIL", checkSumGenetareRequest.getEmail().toString());
			paramMap.put("MOBILE_NO", checkSumGenetareRequest.getMobile().toString());
			paramMap.put("CALLBACK_URL", CALLBACK_URL + checkSumGenetareRequest.getOrderId());
			

			String checkSum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(MERCAHNTKEY, paramMap);
			paramMap.put("CHECKSUMHASH", checkSum);
			LOGGER.info("Paytm Payload: " + paramMap);
			responseObj.setStatus("success");
			responseObj.setMessage("Complete payload.");
			responseObj.setObject(paramMap);

			return new ResponseEntity<>(responseObj, HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			responseObj.setStatus("failed");
			responseObj.setMessage("Oops! something went wrong.");
			return new ResponseEntity<>(responseObj, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/checksum/validate", method = RequestMethod.POST)
	public ResponseEntity<CommonResponse> cv(@RequestBody CheckSumValidateRequest checkSumValidateRequest,
			HttpServletResponse httpServletResponse) {
		LOGGER.info("@RequestBody CheckSumValidateRequest checkSumValidateRequest :: " + checkSumValidateRequest);
		CommonResponse responseObj = new CommonResponse();
		boolean isValideChecksum = false;
		try {
			Map<String, String> paramMap = checkSumValidateRequest.getParamMap();
			String paytmChecksum = "";
			TreeMap<String, String> paytmParams = new TreeMap<String, String>();
			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				if (entry.getKey().equals("CHECKSUMHASH")) {
					paytmChecksum = paramMap.get(entry.getKey());
				} else {
					paytmParams.put(entry.getKey(), entry.getValue());
				}
			}
			LOGGER.info("@RequestBody CheckSumValidateRequest checkSumValidateRequest :: " + checkSumValidateRequest
					+ " :MercahntKey :: " + MERCAHNTKEY + "  paytmParams ::: " + paytmParams + "  paytmChecksum :: "
					+ paytmChecksum);
			isValideChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().verifycheckSum(MERCAHNTKEY, paytmParams,
					paytmChecksum);
			responseObj.setStatus("success");
			responseObj.setMessage("");
			Map<String, Boolean> map = new HashMap<String, Boolean>();
			map.put("value", isValideChecksum);
			responseObj.setObject(map);
			return new ResponseEntity<>(responseObj, HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			responseObj.setStatus("failed");
			responseObj.setMessage("Oops! something went wrong.");
			return new ResponseEntity<>(responseObj, HttpStatus.OK);
		}
	}
}
