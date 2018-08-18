package com.mb.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Utilities {

	private static final Logger LOGGER = Logger.getLogger(Utilities.class);

	public List<SecurityRole> getSuperAdminRoles() {

		List<SecurityRole> roles = new ArrayList<>();

		SecurityRole role1 = new SecurityRole();
		role1.setRole("ROLE_SUPERADMIN");
		role1.setRole_desc("Super Admin");

		roles.add(role1);
		return roles;

	}

	public String sendGetRequest(String url) throws Exception {
		LOGGER.info("URL :: " + url);
		String USER_AGENT = "Mozilla/5.0";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		LOGGER.info("\nSending 'GET' request to URL : " + url);
		LOGGER.info("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}

	public String sendPostRequest(String requestUrl, String payload) {
		StringBuilder jsonString = new StringBuilder();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			writer.write(payload);
			writer.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				jsonString.append(line);
			}
			br.close();
			connection.disconnect();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return jsonString.toString();
	}

	public String sendPostRequestWithBearer(String requestUrl, String payload, String header) {
		StringBuilder jsonString = new StringBuilder();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setRequestProperty("Authorization", header);
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			writer.write(payload);
			writer.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				jsonString.append(line);
			}
			br.close();
			connection.disconnect();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return jsonString.toString();
	}

	public String createRandomCode(int codeLength) {
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		char[] into = "1234567890".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new SecureRandom();
		for (int i = 0; i < codeLength / 2; i++) {
			char b = into[random.nextInt(into.length)];
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
			sb.append(b);
		}
		return shuffle(sb.toString());
	}

	public String shuffle(String input) {
		List<Character> characters = new ArrayList<Character>();
		for (char c : input.toCharArray()) {
			characters.add(c);
		}
		StringBuilder output = new StringBuilder(input.length());
		while (characters.size() != 0) {
			int randPicker = (int) (Math.random() * characters.size());
			output.append(characters.remove(randPicker));
		}
		return output.toString();
	}

	public HashMap<String, Object> saveFileOnHardriveWithImageName(MultipartFile file, String filePath,
			String imageName) {
		HashMap<String, Object> returnBack = new HashMap<String, Object>();
		try {
			File buildPath = new File(filePath);
			if (!buildPath.exists()) {
				LOGGER.info("File buildPath :: " + buildPath.mkdirs());
			}
			if (!file.isEmpty()) {
				byte[] bytes = file.getBytes();
				String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."),
						file.getOriginalFilename().length());
				LOGGER.info("Extension :: " + ext);
				String docsName = imageName + ext;
				String filepath = filePath + docsName;
				LOGGER.info("Filepath :: " + filepath);
				Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
				perms.add(PosixFilePermission.OWNER_READ);
				perms.add(PosixFilePermission.OWNER_WRITE);
				perms.add(PosixFilePermission.OWNER_EXECUTE);
				// add group permissions
				perms.add(PosixFilePermission.GROUP_READ);
				perms.add(PosixFilePermission.GROUP_WRITE);
				perms.add(PosixFilePermission.GROUP_EXECUTE);
				// add others permissions
				perms.add(PosixFilePermission.OTHERS_READ);
				perms.add(PosixFilePermission.OTHERS_WRITE);
				perms.add(PosixFilePermission.OTHERS_EXECUTE);
				Path path = Paths.get(filepath);
				Files.write(path, bytes);
				Files.setPosixFilePermissions(path, perms);
				returnBack.put("status", "success");
				returnBack.put("obj", docsName);

			} else {
				returnBack.put("status", "failed");
				returnBack.put("obj", null);

			}

		} catch (Exception exception) {
			exception.printStackTrace();
			returnBack.put("status", "failed");
			returnBack.put("obj", null);

		}
		return returnBack;
	}

	public Map<String, String> saveFileOnHardrive(MultipartFile file, String filePath, String httpPath)
			throws Exception {
		Map<String, String> map = new HashMap<>();
		Set<PosixFilePermission> perms = new HashSet<>();
		perms.add(PosixFilePermission.OWNER_READ);
		perms.add(PosixFilePermission.OWNER_WRITE);
		perms.add(PosixFilePermission.OWNER_EXECUTE);
		// add group permissions
		perms.add(PosixFilePermission.GROUP_READ);
		perms.add(PosixFilePermission.GROUP_WRITE);
		perms.add(PosixFilePermission.GROUP_EXECUTE);
		// add others permissions
		perms.add(PosixFilePermission.OTHERS_READ);
		perms.add(PosixFilePermission.OTHERS_WRITE);
		perms.add(PosixFilePermission.OTHERS_EXECUTE);

		File buildPath = new File(filePath);
		if (!buildPath.exists()) {
			LOGGER.info("File buildPath :: " + buildPath.mkdirs());
		}

		Path path1 = Paths.get(filePath);
		Files.setPosixFilePermissions(path1, perms);
		if (!file.isEmpty()) {
			byte[] bytes = file.getBytes();
			String fileOriginalName = file.getOriginalFilename();
			LOGGER.info("File Original Name " + fileOriginalName);
			String ext = fileOriginalName.substring(fileOriginalName.indexOf("."), fileOriginalName.length());
			String fileName = fileOriginalName.substring(0, fileOriginalName.lastIndexOf(".") - 1);
			LOGGER.info("Extension :: " + ext);
			String name = StringUtils.trimAllWhitespace(fileName) + "_" + System.currentTimeMillis() + ext;
			String filepath = filePath + name;
			String httppath = httpPath + name;
			LOGGER.info("Filepath :: " + filepath);
			Path path = Paths.get(filepath);
			Files.write(path, bytes);
			Files.setPosixFilePermissions(path, perms);
			map.put("status", "success");
			map.put("filePath", filepath);
			map.put("httpPath", httppath);
		} else {
			map.put("status", "failed");
			map.put("obj", null);

		}

		return map;
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
}
