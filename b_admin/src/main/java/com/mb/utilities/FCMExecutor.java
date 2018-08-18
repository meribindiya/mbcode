package com.mb.utilities;

import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.mb.info.request.FcmMain;
import com.mb.info.response.NotificationResponse;

@Component
@Scope("prototype")
public class FCMExecutor implements Runnable {

	private static Logger logger = Logger.getLogger(FCMExecutor.class);

	private static final String SERVERKEY = "Key=AAAAG2dmrYk:APA91bErlxeCRg-0WSb0SIboFDI45s7ftWHIs3kWUB4Jsnu4_CM81RYen_5O-FyDXHtbYpvq-sADrTanpdJKhdf2g2AxL55hcp4zog5vq89YFIaRhC_YL20TwuLEukuyhPYWYBoeq_ii";

	private static final String fcmPath = "https://fcm.googleapis.com/fcm/send";

	private Set<String> userFCMs;

	private Gson gson = new Gson();

	private FcmMain fcmMain;

	private Utilities utilities;

	public FCMExecutor(Set<String> userFCMs, FcmMain fcmMain, Utilities utilities) {
		this.userFCMs = userFCMs;
		this.fcmMain = fcmMain;
		this.utilities = utilities;
	}

	public void run() {
		try {
			for (String userFcm : userFCMs) {
				fcmMain.setTo(userFcm);
				try {
					Thread.sleep(100);
					JSONObject payload = new JSONObject(new Gson().toJson(fcmMain).toString());
					logger.info("payload.toString() :: "+payload.toString());
					String response = utilities.sendPostRequestWithBearer(fcmPath, payload.toString(), SERVERKEY);
					NotificationResponse notificationResponse = gson.fromJson(response, NotificationResponse.class);
					logger.info("gson.toJson(notificationResponse) :: " + gson.toJson(notificationResponse));

				} catch (Exception e) {
					logger.error(e);
				}
			}
		} catch (Exception exception) {
			logger.error(exception);
		}
	}
}
