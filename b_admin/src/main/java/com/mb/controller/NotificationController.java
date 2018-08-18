package com.mb.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mb.common.service.CommonService;
import com.mb.info.request.FcmData;
import com.mb.info.request.NotificationRequest;
import com.mb.info.request.FcmMain;
import com.mb.persistance.Notification;
import com.mb.persistance.UserFCM;
import com.mb.utilities.FCMExecutor;
import com.mb.utilities.Utilities;

@Controller
@RequestMapping("/notification")
public class NotificationController {

	@Value("${com.mb.notification.file}")
	private String notificationImagePath;

	@Value("${com.mb.notification.http}")
	private String notificationImagePathHttp;

	private Logger logger = Logger.getLogger(NotificationController.class);

	@Autowired
	ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private CommonService commonService;

	@Autowired
	private Utilities utilities;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String textNotificationGet(ModelMap modelMap, @ModelAttribute("msg") String msg) {
		try {
			modelMap.addAttribute("notificationRequest", new com.mb.info.request.NotificationRequest());
			modelMap.addAttribute("msg", msg);
			Map<String, String> map = new HashMap<>();
			map.put("1", "Text");
			map.put("2", "Image");
			modelMap.addAttribute("typeMap", map);
			return "notification";
		} catch (Exception exception) {
			return "exception";
		}

	}

	@GetMapping("/all")
	public String notificationAll(ModelMap modelMap) {
		try {
			modelMap.addAttribute("notifications", commonService.findAll(Notification.class));
			return "notifications";
		} catch (Exception exception) {
			return "exception";
		}

	}

	@RequestMapping(method = RequestMethod.POST)
	public String textNotificationPost(NotificationRequest notificationRequest, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		try {
			Notification notification = new Notification();
			notification.setFailed(0);
			notification.setFailed_rate(0.0f);
			notification.setFinish_at(null);
			Timestamp timestamp = new Timestamp(new Date().getTime());
			notification.setStart_at(timestamp);
			notification.setSuccess(0);
			notification.setSuccess_rate(0.0f);
			notification.setType(notificationRequest.getType());

			FcmMain fcmMain = new FcmMain();
			FcmData spFcmData = new FcmData();
			if (notificationRequest.getFile() != null && !notificationRequest.getFile().isEmpty()) {
				MultipartFile file = notificationRequest.getFile();
				byte[] bytes = file.getBytes();
				String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."),
						file.getOriginalFilename().length());
				logger.info("Extension :: " + ext);
				String imageName = System.currentTimeMillis() + ext;
				String filepath = notificationImagePath + imageName;
				logger.info("Filepath :: " + filepath);
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
				String httpFileSendPath = notificationImagePathHttp + imageName;
				spFcmData.setBitmap(httpFileSendPath);
			}
			notification.setNotification_image_path(spFcmData.getBitmap());
			spFcmData.setId("1023326858045");
			spFcmData.setLongDescription("");
			String fcmessage = notificationRequest.getMessage();
			spFcmData.setAlert_message(fcmessage);
			spFcmData.setOrderId("");
			spFcmData.setPackageName("");
			spFcmData.setTitle(notificationRequest.getTitle());
			spFcmData.setType(notificationRequest.getType().toString());
			spFcmData.setUdf1("");
			spFcmData.setUdf2("");
			fcmMain.setData(spFcmData);

			Set<String> userFCMs = commonService.findAll(UserFCM.class).stream()
					.collect(Collectors.mapping(UserFCM::getFcm, Collectors.toSet()));
			notification.setTitle(notificationRequest.getTitle());
			notification.setTotal(userFCMs.size());
			notification.setNotification(fcmessage);
			commonService.save(notification);
			taskExecutor.execute(new FCMExecutor(userFCMs, fcmMain, utilities));
			redirectAttributes.addFlashAttribute("msg", "FCM sended its take time to complete.");
			return "redirect:/notification";
		} catch (Exception e) {
			logger.error(e);
			return "exception";
		}
	}

}
