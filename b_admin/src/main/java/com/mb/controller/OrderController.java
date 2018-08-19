package com.mb.controller;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mb.common.service.CommonService;
import com.mb.info.request.OrderStatus;
import com.mb.persistance.Address;
import com.mb.persistance.Beautician;
import com.mb.persistance.Category;
import com.mb.persistance.CustomerOrder;
import com.mb.persistance.Service;
import com.mb.persistance.Subcategory;
import com.mb.persistance.User;
import com.mb.utilities.Utilities;

@Controller
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private CommonService commonService;

	@Autowired
	private Utilities utilities;

	private static final Logger LOGGER = Logger.getLogger(CategoryController.class);

	@GetMapping
	public String order(ModelMap modelMap) {
		try {

			Map<Integer, String> serviceMap = commonService.findAll(Service.class).stream()
					.collect(Collectors.toMap(Service::getId, Service::getName));

			Map<Integer, String> spmap = commonService.findAll(Beautician.class).stream()
					.collect(Collectors.toMap(Beautician::getId, Beautician::getName));

			List<CustomerOrder> customerOrders = commonService.findAllWithDistinctRootEntity(CustomerOrder.class);
			customerOrders = customerOrders.stream()
					.sorted(Comparator.comparing(CustomerOrder::getBooked_at).reversed()).collect(Collectors.toList());
			Map<String, Collection<Long>> map = new HashMap<>();
			map.put("user_id",
					customerOrders.stream().collect(Collectors.mapping(CustomerOrder::getUserid, Collectors.toList())));
			Map<Long, User> userMap = commonService.findAllByInCondition(User.class, map).stream()
					.collect(Collectors.toMap(User::getUser_id, Function.identity()));
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy");
			for (CustomerOrder co : customerOrders) {
				Address address = commonService.findOne(Address.class, co.getAddressid().longValue());
				co.setServices(co.getCustomerOrderServices().stream().map(cos -> serviceMap.get(cos.getService_id()))
						.collect(Collectors.joining(",")));
				co.setAddress(address);
				co.setBookedat(dateFormat.format(co.getBooked_at()));
				co.setOrderstatus(OrderStatus.findByValue(co.getOrder_status_code()));
				co.setBookingdate(dateFormat1.format(co.getBooking_date()));
				co.setCustomermobile(userMap.get(co.getUserid()).getMobile().toString());
				co.setCustomername(userMap.get(co.getUserid()).getName());
				if (!StringUtils.isEmpty(co.getSpid())) {
					co.setSpname(spmap.get(co.getSpid()));
				}
				co.setTotal(co.getTotal() - (StringUtils.isEmpty(co.getUtf1()) ? 0 : Integer.parseInt(co.getUtf1())));

			}
			modelMap.addAttribute("customerOrders", customerOrders);
			modelMap.addAttribute("beauticians", spmap);
			return "orders";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}

	}

	@GetMapping("/book-order")
	public String bookorder(ModelMap modelMap) {
		try {
			Map<String, Object> conditionmap = new HashMap<>();
			List<Category> categories = commonService.findAll(Category.class);
			for (Category category : categories) {
				conditionmap.clear();
				conditionmap.put("catid", category.getId());
				List<Subcategory> subcategories = commonService.findAllByProperties(Subcategory.class, conditionmap);
				for (Subcategory subcategory : subcategories) {
					conditionmap.put("subcatid", subcategory.getId());
					List<Service> services = commonService.findAllByProperties(Service.class, conditionmap);
					subcategory.setServices(services);
				}
				category.setSubcategories(subcategories);

			}
			modelMap.addAttribute("times", utilities.getSlots());
			modelMap.addAttribute("obj", categories);
			return "book-order";
		} catch (Exception exception) {
			LOGGER.error("context", exception);
			return "exception";
		}

	}

	@GetMapping("/action/{orderid}/{code}")
	public void changeOrderAction(@PathVariable("code") Integer code, @PathVariable("orderid") Long orderid,
			HttpServletResponse response) {
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
			} else {
				commonService.assignBeauticianToOrder(orderid, code);
				message = "Selected beautician successfully assign to order.";
			}
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(message.getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (Exception exception) {
			LOGGER.error("context", exception);
		}

	}

	@GetMapping("/action-order-to-sp/{orderid}/{spid}")
	public void assignOrderToSp(@PathVariable("spid") Integer code, @PathVariable("orderid") Long orderid,
			HttpServletResponse response) {
		try {

			commonService.assignBeauticianToOrder(orderid, code);
			String message = "Selected beautician successfully assign to order.";
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(message.getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (Exception exception) {
			LOGGER.error("context", exception);
		}

	}

}
