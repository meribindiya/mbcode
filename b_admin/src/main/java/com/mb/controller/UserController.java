package com.mb.controller;

import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.google.gson.Gson;



@Controller
@RequestMapping("/user")
public class UserController {
	
	/*private static final Logger logger=Logger.getLogger(UserController.class);

	@RequestMapping(value="/register",method=RequestMethod.POST)
	public  void register(@RequestBody RegisterRequest registerRequest,HttpServletResponse response){
		logger.info("RegisterRequest :: "+registerRequest);
		ResponseObj responseObj=new ResponseObj();
		try{
			LoginRequest loginRequest=new LoginRequest();
			loginRequest.setMobile(registerRequest.getMobile());
			User user=commonServiceInterface.loadUserByUsername(loginRequest);
			if(user != null){
				responseObj.setStatus_code(516);
				responseObj.setStatus("failed");
				responseObj.setMessage("Mobile number already exist");
			}else{
				user=new User();
				user.setMobile(registerRequest.getMobile());
				user.setEmail(registerRequest.getEmail());
				user.setName(registerRequest.getNickName());
				user.setSource("admin panel");
				user.setSource_id(registerRequest.getSource_id());
				Timestamp timestamp=new Timestamp(new Date().getTime());
				user.setCreated_at(timestamp);
				user.setStatus(true);
				user.setUpdated_at(timestamp);
				String referrenceId=utilities.createRandomCode(Integer.parseInt(invitecodeLength));
				user.setReference_id(referrenceId);
				if(registerRequest.getReferCode() !=null && !"".equalsIgnoreCase(registerRequest.getReferCode())){
					User refCodeUser=commonServiceInterface.getUserBYRefCode(registerRequest.getReferCode());
					if(refCodeUser !=null){
					user.setWalletMin(100);
					user.setReferred_by(registerRequest.getReferCode());
					}
				}
				Long userId=commonServiceInterface.save(user);
				user.setUser_id(userId);
				if(registerRequest.getReferCode() !=null && !"".equalsIgnoreCase(registerRequest.getReferCode())){
				taskExecutor.execute(new ReferCodeExecutor(user, commonServiceInterface,Integer.parseInt(refcodeMin)));
				}
				responseObj.setStatus_code(517);
				responseObj.setStatus("success");
				responseObj.setMessage("User successfully register.");
				responseObj.setObject(user);
			}
			
			OutputStream outputStream=response.getOutputStream();
			outputStream.write(new Gson().toJson(responseObj).getBytes());
			outputStream.flush();
			outputStream.close();
			
		}catch (Exception exception) {
			exception.printStackTrace();
					}
	}
	
	@RequestMapping(value="/login",method=RequestMethod.POST,consumes="application/json")
	public void login(@RequestBody LoginRequest loginRequest,HttpServletResponse response){
		logger.info("LoginRequest :: "+loginRequest);
		ResponseObj responseObj=new ResponseObj();
		try{
			User user=commonServiceInterface.loadUserByUsername(loginRequest);
			if(user == null){
				responseObj.setStatus_code(509);
				responseObj.setStatus("success");
				responseObj.setMessage("move on registration page.");
			}else{
				List<Address> addresses=commonServiceInterface.fetchAllAddressByUserId(user.getUser_id());
				responseObj.setStatus_code(510);
				responseObj.setStatus("success");
				responseObj.setMessage("Address pages.");
				UserExistResponse existResponse=new UserExistResponse();
				existResponse.setUser(user);
				existResponse.setAddresses(addresses);
				responseObj.setObject(existResponse);
			}
			OutputStream outputStream=response.getOutputStream();
			outputStream.write(new Gson().toJson(responseObj).getBytes());
			outputStream.flush();
			outputStream.close();
		}catch (Exception exception) {
			exception.printStackTrace();
			responseObj.setStatus_code(300);
			responseObj.setStatus("failed");
			responseObj.setMessage("Oops! something went wrong.");
		}
	}
	
	@RequestMapping(value="/address/add",method=RequestMethod.POST)
	public void addAddress(@RequestBody AddressRequest addressRequest,HttpServletResponse response){
		logger.info("AddressRequest :: "+addressRequest);
		ResponseObj responseObj=new ResponseObj();
		try{
			Gson gson = new Gson();
			String url = apibaseurl + saveaddress;
			String apiresponse = utilities.sendPostRequest(url, gson.toJson(addressRequest));
			Address address2=commonServiceInterface.fetchDefaultAddress(addressRequest.getUser_id()); 
			logger.info("Add address api response :: "+apiresponse);
			ResponseObj obj = gson.fromJson(apiresponse,ResponseObj.class);
			if("success".equalsIgnoreCase(obj.getStatus())) {
				obj.setObject(address2);
			}
			OutputStream outputStream=response.getOutputStream();
			outputStream.write(new Gson().toJson(obj).getBytes());
			outputStream.flush();
			outputStream.close();
		}catch(Exception exception){
			logger.error("Exception",exception);
			exception.printStackTrace();
		}
	}
	
	
	@RequestMapping(value="/address/update",method=RequestMethod.POST)
	public void updateAddress(@RequestBody AddressRequest addressRequest,HttpServletResponse response){
		logger.info("AddressRequest :: "+addressRequest);
		ResponseObj responseObj=new ResponseObj();
		try{
			Address address=commonServiceInterface.getAddressById(addressRequest.getId());
			BeanUtils.copyProperties(addressRequest,address);
			Timestamp timestamp=new Timestamp(new Date().getTime());
			address.setUpdated_at(timestamp);
			String addResponse =commonServiceInterface.saveAddress(address);
			logger.info("Response :: "+addResponse);
			responseObj.setStatus_code(511);
			responseObj.setStatus("success");
			responseObj.setMessage("Address successfully added.");
			OutputStream outputStream=response.getOutputStream();
			outputStream.write(new Gson().toJson(responseObj).getBytes());
			outputStream.flush();
			outputStream.close();
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	
	@RequestMapping(value="/address/all",method=RequestMethod.GET)
	public void addAddress(@RequestParam Long userId,HttpServletResponse response){
		logger.info("addAddress :: "+userId);
		ResponseObj responseObj=new ResponseObj();
		try{
			List<Address> addresses1=commonServiceInterface.fetchAllAddressByUserId(userId);
			List<Address> addresses=new ArrayList<Address>();
			logger.info("List<Address> :: "+addresses1);
			for(Address address:addresses1){
				if(address.getAddress1() !=null && !"".equalsIgnoreCase(address.getAddress1())){
					address.setAddress(address.getAddress()+", "+address.getAddress1());	
				}
				addresses.add(address);
			}
			responseObj.setStatus("success");
			responseObj.setMessage("fetch address successfully");
			responseObj.setObject(addresses);
			OutputStream outputStream=response.getOutputStream();
			outputStream.write(new Gson().toJson(responseObj).getBytes());
			outputStream.flush();
			outputStream.close();
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	
	@RequestMapping(value="/address/default",method=RequestMethod.GET)
	public void defaultAddress(@RequestParam Long addressId,@RequestParam Long userId,HttpServletResponse response){
		logger.info("defaultAddress(@RequestParam Long addressId,@RequestParam Long userId) :: "+userId+" :: "+addressId);
		ResponseObj responseObj=new ResponseObj();
		try{
			String dresponse=commonServiceInterface.defaultAddress(userId,addressId);
			List<Address> addresses=commonServiceInterface.fetchAllAddressByUserId(userId);
			logger.info("Response :: "+dresponse);
			responseObj.setStatus("success");
			responseObj.setMessage("default address successfully updated");
			responseObj.setObject(addresses);
			OutputStream outputStream=response.getOutputStream();
			outputStream.write(new Gson().toJson(responseObj).getBytes());
			outputStream.flush();
			outputStream.close();
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	
	@RequestMapping(value="/address/fetchById",method=RequestMethod.GET)
	public void fetchAddress(@RequestParam(name="addressId") Long addressId,HttpServletResponse servletResponse){
		ResponseObj responseObj=new ResponseObj();
		try{
			Address address=commonServiceInterface.fetchAddressById(addressId);
			responseObj.setObject(address);
			OutputStream outputStream=servletResponse.getOutputStream();
			outputStream.write(new Gson().toJson(responseObj).getBytes());
			outputStream.flush();
			outputStream.close();
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}*/
}
