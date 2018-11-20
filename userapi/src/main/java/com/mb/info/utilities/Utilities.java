package com.mb.info.utilities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.paytm.pg.merchant.CheckSumServiceHelper;

@Component
public class Utilities {

	private static final Logger LOGGER = LogManager.getLogger(Utilities.class);

	public String getRandomNumericString(int len) {
		char[] alphNum = "123456789".toCharArray();
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder();
		len = len < 1 ? 1 : len;
		len = len > 36 ? 36 : len;
		for (int i = 0; i < len; i++) {
			sb.append(alphNum[rnd.nextInt(alphNum.length)]);
		}
		String otp = sb.toString();
		return otp;
	}

	public String sendGet(String url) {
		StringBuilder response = new StringBuilder();
		try {
			LOGGER.info("URL :: " + url);
			String useragent = "Mozilla/5.0";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", useragent);
			int responseCode = con.getResponseCode();
			LOGGER.info("\nSending 'GET' request to URL : " + url);
			LOGGER.info("Response Code : " + responseCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (Exception e) {
			LOGGER.error("context", e);
		}
		return response.toString();
	}

	public String validatePaytmTransaction(String urlP, String mid, String pSourceId, String mKey) {
		LOGGER.info(" mid :: " + mid + " pSourceId :: " + pSourceId + " mKey :: " + mKey);
		HttpURLConnection connection = null;
		TreeMap<String, String> tmap = new TreeMap<>();
		String checksum;
		StringBuilder builder = new StringBuilder();
		try {
			tmap.put("MID", mid);
			tmap.put("ORDERID", pSourceId);
			LOGGER.info("tmap :: " + tmap);
			checksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(mKey, tmap);
			LOGGER.info("checksum :: " + checksum);
			tmap.put("CHECKSUMHASH", checksum);
			JSONObject obj = new JSONObject(tmap);
			String urlParameters = obj.toString();
			LOGGER.info("urlParameters :: " + urlParameters);
			urlParameters = URLEncoder.encode(urlParameters, "UTF-8");
			LOGGER.info("urlParameters :: " + urlParameters);
			URL url = new URL(urlP);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("contentType", "application/json");
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes("JsonData=");
			wr.writeBytes(urlParameters);
			wr.close();
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while ((line = rd.readLine()) != null) {
				builder.append(line);
				builder.append('\r');
			}
			rd.close();
			LOGGER.info("Builder :: " + builder);
			return builder.toString();
		} catch (Exception e) {
			LOGGER.error("Exception ", e);
			return null;
		}
	}

	public List<String> getSlots() {
		List<String> slots = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		while (calendar.get(Calendar.HOUR_OF_DAY) <= 19) {
			String timeSlot = (calendar.get(Calendar.HOUR) == 0 ? calendar.get(Calendar.HOUR_OF_DAY)
					: calendar.get(Calendar.HOUR)) + " " + (calendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM");
			timeSlot += " - ";
			calendar.add(Calendar.HOUR_OF_DAY, 1);
			timeSlot += (calendar.get(Calendar.HOUR) == 0 ? calendar.get(Calendar.HOUR_OF_DAY)
					: calendar.get(Calendar.HOUR)) + " " + (calendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM");
			LOGGER.info(timeSlot);
			slots.add(timeSlot);
		}
		return slots;
	}

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
