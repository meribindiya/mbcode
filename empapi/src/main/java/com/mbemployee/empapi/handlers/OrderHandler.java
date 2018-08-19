package com.mbemployee.empapi.handlers;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.apache.bcel.generic.IINC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mbemployee.empapi.common.CommonResponse;
import com.mbemployee.empapi.common.OrderStatus;
import com.mbemployee.empapi.persistance.Address;
import com.mbemployee.empapi.persistance.CustomerOrder;
import com.mbemployee.empapi.persistance.CustomerOrderService;
import com.mbemployee.empapi.persistance.Service;
import com.mbemployee.empapi.persistance.User;
import com.mbemployee.empapi.service.CommonService;

@RestController
@RequestMapping("/orders")
public class OrderHandler {

	private static final Logger LOGGER = LogManager.getLogger(LoginHandler.class);

	private static final String SUCCESS = "success";

	private static final String FAILED = "failed";

	@Autowired
	private CommonService commonService;

	@GetMapping("/all/{spid}")
	public ResponseEntity<CommonResponse> orders(@PathVariable("spid") Integer spid) {
		CommonResponse commonResponse = new CommonResponse();
		try {
			Map<Integer, String> serviceMap = commonService.findAll(Service.class).stream()
					.collect(Collectors.toMap(Service::getId, Service::getName));
			Map<String, Object> map = new HashMap<>();
			map.put("spid", spid);
			List<CustomerOrder> customerOrders = commonService
					.findAllByPropertiesWithDistinctRootEntity(CustomerOrder.class, map);
			customerOrders = customerOrders.stream()
					.sorted(Comparator.comparing(CustomerOrder::getBooked_at).reversed()).collect(Collectors.toList());

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy");
			customerOrders.forEach(co -> {
				co.setServices(co.getCustomerOrderServices().stream().map(cos -> serviceMap.get(cos.getService_id()))
						.collect(Collectors.joining(",")));
				co.setBookedat(dateFormat.format(co.getBooked_at()));
				co.setOrderstatus(OrderStatus.findByValue(co.getOrder_status_code()));
				co.setBookingdate(dateFormat1.format(co.getBooking_date()));
				if (!StringUtils.isEmpty(co.getUtf1())) {
					co.setTotal(co.getTotal() - Integer.parseInt(co.getUtf1()));
				}
			});
			commonResponse.setStatus(SUCCESS);
			commonResponse.setObject(customerOrders);
			return ResponseEntity.ok(commonResponse);
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
			return ResponseEntity.ok(commonResponse);
		}

	}

	@GetMapping("/action/{orderid}/{code}")
	public ResponseEntity<CommonResponse> changeOrderAction(@PathVariable("code") Integer code,
			@PathVariable("orderid") Long orderid, HttpServletResponse response) {
		CommonResponse commonResponse = new CommonResponse();
		try {
			String message = "";
			if (code == 2) {
				commonService.changeOrderStatus(orderid, OrderStatus.ORDER_START.getStatus());
				message = "Order succesfully started";
			} else if (code == 3) {
				commonService.changeOrderStatus(orderid, OrderStatus.ORDER_COMPLETED.getStatus());
				message = "Order succesfully completed";
			} else if (code == 4) {
				commonService.changeOrderStatus(orderid, OrderStatus.ORDER_REWORK.getStatus());
				message = "Order ready for rework.";
			} else if (code == 5) {
				commonService.changeOrderStatus(orderid, OrderStatus.ORDER_CANCELLED.getStatus());
				message = "Order cancelled.";
			}
			commonResponse.setStatus(SUCCESS);
			commonResponse.setMessage(message);
			return ResponseEntity.ok(commonResponse);
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
			return ResponseEntity.ok(commonResponse);
		}

	}

	@GetMapping("/details/{orderid}")
	public ResponseEntity<CommonResponse> ordersDetails(@PathVariable("orderid") Long orderid) {
		CommonResponse commonResponse = new CommonResponse();
		try {
			Map<Integer, String> serviceMap = commonService.findAll(Service.class).stream()
					.collect(Collectors.toMap(Service::getId, Service::getName));

			CustomerOrder customerOrder = commonService.findOneWithDistinctRootEntity(CustomerOrder.class, orderid);
			if (StringUtils.isEmpty(customerOrder)) {
				commonResponse.setStatus(FAILED);
				commonResponse.setMessage("Invalid order id.");
				return ResponseEntity.ok(commonResponse);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("user_id", customerOrder.getUserid());
			Optional<User> user = commonService.findAllByPropertiesWithDistinctRootEntity(User.class, map).stream()
					.findFirst();
			if (user.isPresent()) {
				customerOrder.setUserName(user.get().getName());
			} else {
				customerOrder.setUserName("User not found");
			}

			Address address = commonService.findOne(Address.class, customerOrder.getAddressid());
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");

			customerOrder.setServices(customerOrder.getCustomerOrderServices().stream()
					.map(cos -> serviceMap.get(cos.getService_id())).collect(Collectors.joining(",")));
			customerOrder.setBookedat(dateFormat.format(customerOrder.getBooked_at()));
			customerOrder.setOrderstatus(OrderStatus.findByValue(customerOrder.getOrder_status_code()));
			customerOrder.setBookingdate(dateFormat1.format(customerOrder.getBooking_date()));

			Set<CustomerOrderService> customerOrderServices = customerOrder.getCustomerOrderServices().stream()
					.map(cos -> {
						cos.setServiceName(serviceMap.get(cos.getService_id()));
						return cos;
					}).collect(Collectors.toSet());
			customerOrder.setCustomerOrderServices(customerOrderServices);
			if (!StringUtils.isEmpty(customerOrder.getUtf1())) {
				customerOrder.setTotal(customerOrder.getTotal() - Integer.parseInt(customerOrder.getUtf1()));
			}
			customerOrder.setAddress(address);
			commonResponse.setStatus(SUCCESS);
			commonResponse.setObject(customerOrder);
			return ResponseEntity.ok(commonResponse);
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
			return ResponseEntity.ok(commonResponse);
		}

	}

}
