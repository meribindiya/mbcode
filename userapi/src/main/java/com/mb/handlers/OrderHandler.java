package com.mb.handlers;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.mb.common.CommonResponse;
import com.mb.info.req.OrderRequest;
import com.mb.info.req.OrderStatus;
import com.mb.info.req.PymntMode;
import com.mb.info.utilities.Utilities;
import com.mb.persistance.Address;
import com.mb.persistance.CustomerOrder;
import com.mb.persistance.CustomerOrderService;
import com.mb.persistance.PaytmStatusResponse;
import com.mb.persistance.Service;
import com.mb.service.CommonService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true")
public class OrderHandler {

	private static final Logger LOGGER = LogManager.getLogger(OrderHandler.class);

	private static final String SUCCESS = "success";

	private static final String FAILED = "failed";

	@Value("${com.paytm.MID}")
	private String MID;

	@Value("${com.paytm.MERCAHNTKEY}")
	private String MERCAHNTKEY;

	@Value("${com.paytm.VALIDATE_URL}")
	private String VALIDATE_URL;

	@Autowired
	private CommonService commonService;

	@Autowired
	private Utilities utilities;

	@PostMapping("/create-order")
	@ApiOperation(value = "", notes = "date format dd-MM-yyyy,time format hh:mm a")
	public ResponseEntity<CommonResponse> createOrder(@RequestBody OrderRequest orderRequest) {
		CommonResponse commonResponse = new CommonResponse();
		try {
			LOGGER.info("OrderRequest orderRequest :: "+orderRequest);
			if (1 == orderRequest.getPymnt_mode()) {
				Gson gson = new Gson();
				String response = utilities.validatePaytmTransaction(VALIDATE_URL, MID,
						String.valueOf(orderRequest.getOnline_source_id()), MERCAHNTKEY);
				LOGGER.info("Response :: " + response);
				PaytmStatusResponse paytmStatusResponse = gson.fromJson(response, PaytmStatusResponse.class);
				paytmStatusResponse.setUSER_ID(orderRequest.getUserid());
				commonService.save(paytmStatusResponse);
				if (!("TXN_SUCCESS".equalsIgnoreCase(paytmStatusResponse.getSTATUS()))) {
					commonResponse.setStatus(FAILED);
					commonResponse.setMessage(paytmStatusResponse.getRESPMSG());
					return new ResponseEntity<>(commonResponse, HttpStatus.OK);
				}
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			CustomerOrder customerOrder = new CustomerOrder();
			BeanUtils.copyProperties(orderRequest, customerOrder, "booking_date");
			Map<Integer, Service> serviceMap = commonService.findAll(Service.class).stream()
					.collect(Collectors.toMap(Service::getId, Function.identity()));
			Set<CustomerOrderService> customerOrderServices = orderRequest.getOrderServiceRequests().stream()
					.map(osr -> {
						CustomerOrderService customerOrderService = new CustomerOrderService();
						Service service = serviceMap.get(osr.getServiceid());
						customerOrderService.setService_id(osr.getServiceid());
						customerOrderService.setMinutes(service.getTime());
						customerOrderService.setService_amount(service.getPrice());
						customerOrderService.setTotal_service_amount(service.getPrice() * osr.getQuantity());
						customerOrderService.setQuantity(osr.getQuantity());
						return customerOrderService;
					}).collect(Collectors.toSet());
			customerOrder.setCustomerOrderServices(customerOrderServices);
			customerOrder.setBooking_date(new Date(dateFormat.parse(orderRequest.getBooking_date()).getTime()));
			customerOrder.setOrder_status_code(OrderStatus.ORDER_NEW_REQUEST.getStatus());
			customerOrder.setBooked_at(new Timestamp(System.currentTimeMillis()));
			if (0 == orderRequest.getPymnt_mode()) {
				customerOrder.setPymnt_mode(PymntMode.COD.getStatus());
				customerOrder.setPymnt_source(PymntMode.findByValue(PymntMode.COD.getStatus()));

			} else if (1 == orderRequest.getPymnt_mode()) {
				customerOrder.setPymnt_mode(PymntMode.PREPAID.getStatus());
				customerOrder.setPymnt_source(PymntMode.findByValue(PymntMode.PREPAID.getStatus()));
			} else if (2 == orderRequest.getPymnt_mode()) {
                customerOrder.setPymnt_mode(PymntMode.PREPAID_WALLET.getStatus());
                customerOrder.setPymnt_source(PymntMode.findByValue(PymntMode.PREPAID_WALLET.getStatus()));
            } else {
				commonResponse.setStatus(FAILED);
				commonResponse.setMessage("Invalid payment mode.");
				return ResponseEntity.ok(commonResponse);
			}
			customerOrder.setTotal(customerOrderServices.stream()
					.mapToInt(CustomerOrderService::getTotal_service_amount).reduce(Integer::sum).orElse(0));
			Long orderid = (Long) commonService.save(customerOrder);
			commonResponse.setStatus(SUCCESS);
			commonResponse.setMessage("Order successfully placed.\nYour orderid is " + orderid);
		} catch (

		Exception exception) {
			LOGGER.error("context", exception);
			commonResponse.setStatus(FAILED);
			commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
		}
		return ResponseEntity.ok(commonResponse);
	}

	@GetMapping("/all/{userid}")
	public ResponseEntity<CommonResponse> orders(@PathVariable("userid") Long userid) {
		CommonResponse commonResponse = new CommonResponse();
		try {
			Map<Integer, String> serviceMap = commonService.findAll(Service.class).stream()
					.collect(Collectors.toMap(Service::getId, Service::getName));
			Map<String, Object> map = new HashMap<>();
			map.put("userid", userid);
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

	@GetMapping("update/{orderid}/{orderStatus}")
	public ResponseEntity<CommonResponse> updateOrder (@PathVariable("orderid") Long orderid, @PathVariable("orderStatus") int orderStatus) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Map<String, Object> updateCondition = new HashMap<>();
            updateCondition.put("id", orderid);

            List<CustomerOrder> orders = commonService.findAllByProperties(CustomerOrder.class, updateCondition);

            if (orders.isEmpty()) {
                commonResponse.setStatus(FAILED);
                commonResponse.setMessage("Can not find an order.");
            } else {
                CustomerOrder order = orders.stream().findFirst().get();
                order.setOrder_status_code(orderStatus);
                commonService.saveOrUpdate(order);
                commonResponse.setStatus(SUCCESS);
            }
        } catch (Exception exception) {
            LOGGER.error("GET order/update/" + orderid + "/" + orderStatus + ":: context", exception);
            commonResponse.setStatus(FAILED);
            commonResponse.setMessage("Something going wrong.\nPlease try after sometime.");
        }
        return ResponseEntity.ok(commonResponse);
	}

}
