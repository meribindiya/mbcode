<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	isELIgnored="false" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<div class="container-fluid">
	<!-- Start Page Content -->
	<div class="row">
		<div class="col-lg-8 col-sm-8 col-md-8 col-xs-8">
			<div class="card service-details">
				<div class="card-body">
					<h4 class="card-title">Book Order</h4>
					<div>
						<div id="tabs">
							<ul>
								<c:forEach var="cat" items="${obj}">
									<li><a href="#boxxer${cat.id}">${cat.name}</a></li>
								</c:forEach>

							</ul>

							<c:forEach var="cat" items="${obj}">
								<div id="boxxer${cat.id}">
									<div id="accordion">
										<c:forEach var="subcat" items="${cat.subcategories}">
											<h3>${subcat.name}</h3>
											<div>
												<c:forEach var="service" items="${subcat.services}">

													<div class="card">
														<div class="card-body">
															<div class="row">
																<div class="col-md-4">${service.name}</div>
																<div class="col-md-3">Rs. ${service.price}/-</div>
																<div class="col-md-5">
																	<span data-catid="${cat.id}"
																		data-subcatid="${subcat.id}"
																		data-serviceid="${service.id}"
																		data-price="${service.price}"
																		data-time="${service.time}"
																		data-servicename="${service.name}"
																		style="display: none;" class="details-finder"></span>
																	<button data-catid="${cat.id}"
																		data-subcatid="${subcat.id}"
																		data-serviceid="${service.id}"
																		class="label label-primary btn add-product"
																		style="text-align: right">+ Add product</button>
																	<div style="display: none;" class="counter-collector">
																		<input type="button" value="-"
																			class="subs btn btn-danger"
																			class="btn btn-default pull-left"
																			style="margin-right: 2%" />&nbsp; <input type="text"
																			value="1" readonly="readonly"
																			style="width: 50px; text-align: center; margin: 0px;"
																			class="onlyNumber form-control pull-left"
																			id="noOfRoom" value="" name="noOfRoom" />&nbsp; <input
																			type="button" value="+" class="adds btn btn-success"
																			class="btn btn-default" />
																	</div>
																</div>
															</div>
															<div class="row">
																<div class="col-md-12">${service.shortdesc}</div>
															</div>
														</div>
													</div>
												</c:forEach>


											</div>
										</c:forEach>
									</div>
								</div>
							</c:forEach>

						</div>

					</div>
				</div>
			</div>
		</div>
		<div class="col-lg-4 col-sm-4 col-md-4 col-xs-4">
			<div class="card">
				<div class="panel">
					<div class="panel-heading">
						<h2>Cart</h2>
					</div>
					<div class="panel-body cart">No Item Found</div>
					<div class="panel-heading">
						<button id="totalAmountbutton" class="btn btn-success"
							data-toggle="modal" data-target="#myModal"
							style="display: none; width: 100%; margin: 6px 0px;">Create
							Order</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog"
	style="padding-left: 18.9884px; background-color: #0000009c; opacity: 1.6;">
	<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-body">
				<div id="loginDailog">
					<table class="table">
						<tr>
							<td>Mobile Number</td>
							<td><input id="mobilenumber" class="form-control" /></td>
						</tr>
						<tr>
							<td colspan="2"><button class="bt btn-success"
									id="login-submit">Submit</button></td>
						</tr>
					</table>
				</div>
				<div id="registerDailog" style="display: none;">
					<table class="table">
						<tr>
							<td>Name</td>
							<td><input id="name" class="form-control" /></td>
						</tr>
						<tr>
							<td>Mobile Number</td>
							<td><input id="mnumber" class="form-control" /></td>
						</tr>
						<tr>
							<td>Email</td>
							<td><input id="email" class="form-control" /></td>
						</tr>
						<tr>
							<td colspan="2"><button class="bt btn-success"
									id="register-submit">Register</button></td>
						</tr>
					</table>
				</div>
				<div id="addressDailog" style="display: none;">
					<table class="table">
						<tr>
							<td>Building Name</td>
							<td><input id="buildName" class="form-control" /></td>
						</tr>
						<tr>
							<td>Address</td>
							<td><input id="address1" class="form-control" /></td>
						</tr>
						<tr>
							<td>Landmark</td>
							<td><input id="landmark" class="form-control" /></td>
						</tr>
						<tr>
							<td colspan="2"><button class="bt btn-success"
									id="address-submit">Save</button></td>
						</tr>
					</table>
				</div>
				<div id="commonDailog" style="display: none;">
					<table class="table">
						<tr>
							<td>Select Address</td>
							<td><select id="addressid" class="form-control"
								style="height: 50px;">
									<option value="">Select Address</option>
							</select>
								<button id="add-address"
									style="background: none; border: none; text-decoration: underline !important; font-weight: 600;">add
									address</button></td>

						</tr>
						<tr>
							<td>Booking Date</td>
							<td><input type="text" class="form-control" id="bookingdate" /></td>
						</tr>
						<tr>
							<td>Booking Time</td>
							<td><select id="bookingtime" class="form-control"
								style="height: 30px;">
									<c:forEach var="time" items="${times}">
										<option value="${time}">${time}</option>
									</c:forEach>
							</select></td>
						</tr>
						<tr>
							<td>Add Discount</td>
							<td><input type="number" class="form-control" id="discount" /></td>
						</tr>
						<tr>
							<td colspan="2"><button class="bt btn-success"
									id="order-submit">Create Order</button></td>
						</tr>
					</table>
				</div>
			</div>
		</div>

	</div>
</div>

<style type="text/css">
td {
	text-align: left !important;
	color: #000 !important;
}
</style>
<link rel="stylesheet"
	href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" />
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
	var orderRequest = {
			"addressid" : 0,
			"booking_date" : "string",
			"booking_time" : "string",
			"category_id" : 0,
			"mobile" : 0,
			"online_source_id" : "string",
			"online_source_txndate" : "string",
			"online_source_txnid" : "string",
			"orderServiceRequests" : [ {
				"quantity" : 0,
				"serviceid" : 0
			} ],
			"payment_source_id" : "string",
			"payment_source_txndate" : "string",
			"payment_source_txnid" : "string",
			"pymnt_mode" : 0,
			"pymnt_source" : "string",
			"userid" : 0,
			"utf1" : "string",
			"utf2" : "string",
			"utf3" : "string"
		}
		var map = new Map();
		var service = new Object();
		var user = new Object;
		$(".add-product").click(function() {
			$(this).hide();
			$(this).parent().find(".counter-collector").css("display", "flex");
			var detailFinder = $(this).parent().find(".details-finder");
			var service={};
    		var serviceid=detailFinder.attr('data-serviceid');
    		var catid=detailFinder.attr('data-catid');
    		var subcatid=detailFinder.attr('data-subcatid');
    		var price=detailFinder.attr('data-price');
    		var time=detailFinder.attr('data-time');
    		var servicename=detailFinder.attr('data-servicename');
    		service['serviceid']=serviceid;
    		service['servicename']=servicename;
    		service['catid']=catid;
    		service['subcatid']=subcatid;
    		service['price']=price;
    		service['time']=time;
    		service['quantity'] = 1;
    		map.set(serviceid,service);
    		buildSummary(map);
		});
		
		$("#add-address").click(function(){
			  $("#commonDailog").hide(function(){
				  $("#addressDailog").show();
			  });
			
		});

		
		$(".subs").click(function() {
			var detailFinder = $(this).parent().siblings(".details-finder");
			var serviceid = detailFinder.attr('data-serviceid');
			var value = $(this).next().val();
			value = parseInt(value) - 1;
			if (value < 1) {
				$(this).parent().siblings(".add-product").show();
				$(this).parent().hide();
				map.delete(serviceid);
			}else{
				var service = map.get(serviceid);
				service['quantity'] = value;
				map.set(serviceid,service);
				 $(this).next().val(value);
			}
			buildSummary(map);
			
		});
		
		$("#login-submit").click(function(){
			
		 var IndNum = /^[0]?[6789]\d{9}$/;

		   if(!IndNum.test($("#mobilenumber").val())){
			   alert("Please enter valid mobile number");
				return ;
		    }
		   $(".preloader").show();
			
			 $.ajax({
				  contentType: 'application/json',
				  url: "http://live.meribindiya.com/user/"+$("#mobilenumber").val(),
				  type:"GET",
				  success: function(result){
					 
					  if(result.status == "success"){
						  
						  if(result.message == "OTP_SCREEN"){
							  $("#addressid").html("<option value =''>Select Address</option>");
							   user = result.object.user;
							  $.ajax({
								  contentType: 'application/json',
								  url: "http://live.meribindiya.com/user/address/all/"+user.user_id,
								  type:"GET",
								  success: function(result){
									  if(result.status == "success"){
										  var addressString = "";
										  var address = result.object;
										  for(var i=0;i<address.length;i++){
											  addressString +="<option value="+address[i].id+">"+address[i].buildingName+", "+address[i].address+", "+address[i].landmark+"</option>"
										  }
										  $("#addressid").html(addressString);
										  
										  $("#loginDailog").hide(function(){
											  $("#commonDailog").show();
										  });
									  }else{
										  alert(result.message);
									  }
									 
									  $(".preloader").hide();
							  },
							  error:function(result){
								  $(".preloader").hide();
								  alert("Oops! something went wrong, Please try after sometime.")
							  }
							});
							
						  }else{
							  $(".preloader").hide();
							  $("#loginDailog").hide(function(){
								  $("#registerDailog").show();
							  });
						  }
						  
					  }else{
						  $(".preloader").hide();
						  alert(result.message);
						  
					  }
			  },
			  error:function(result){
				  $(".preloader").hide();
				  alert("Oops! something went wrong, Please try after sometime.")
			  }
			});
		   		
			
			
		});
		
		
		
		
	$("#address-submit").click(function(){
			if($.trim($("#buildName").val()) == ""){
				   alert("Please enter building name..");
					return ;
			  }
			 
			  if($.trim($("#address1").val()) == ""){
				   alert("Please enter address.");
					return ;
			  }
			  if($.trim($("#landmark").val()) == ""){
				   alert("Please enter landmark.");
					return ;
			  }
			  var addressRequest  = {
						  "address": $("#address1").val(),
						  "addressType": "Home",
						  "buildingName": $("#buildName").val(),
						  "landmark": $("#landmark").val(),
						  "userid": user.user_id
				}
			  $(".preloader").show();
			  $.ajax({
				  contentType: 'application/json',
				  url: "http://live.meribindiya.com/user/address/add",
				  type:"POST",
				  data:JSON.stringify(addressRequest),
				  dataType:"json",
				  success: function(result){
					  if(result.status == "success"){
						  $.ajax({
							  contentType: 'application/json',
							  url: "http://live.meribindiya.com/user/address/all/"+user.user_id,
							  type:"GET",
							  success: function(result){
								  if(result.status == "success"){
									  var addressString = "";
									  var address = result.object;
									  for(var i=0;i<address.length;i++){
										  addressString +="<option value="+address[i].id+">"+address[i].buildingName+", "+address[i].address+", "+address[i].landmark+"</option>"
									  }
									  $("#addressid").html(addressString);
									  $("#addressDailog").hide(function(){
										  $("#commonDailog").show();
									  });
								  }else{
									  alert(result.message);
								  }
								  $(".preloader").hide();
						  },
						  error:function(result){
							  $(".preloader").hide();
							  alert("Oops! something went wrong, Please try after sometime.")
						  }
						});
					  }else{
						  alert(result.message);
					  }
			  },
			  error:function(result){
				  $(".preloader").hide();
				  alert("Oops! something went wrong, Please try after sometime.")
			  }
			});
			  
			
		});
		
		
		$("#register-submit").click(function(){
			
			if($.trim($("#name").val()) == ""){
				   alert("Please enter name..");
					return ;
			  }
			 var IndNum = /^[0]?[6789]\d{9}$/;
			  if(!IndNum.test($("#mnumber").val())){
				   alert("Please enter valid mobile number");
					return ;
			   }
			  if($.trim($("#email").val()) == ""){
				   alert("Please enter email address");
					return ;
			  }
			  var registerRequest  = {
					  "email": $("#email").val(),
					  "gender": "female",
					  "mobile": $("#mnumber").val(),
					  "name": $("#name").val()
			}
			  $(".preloader").show();
			  $.ajax({
				  contentType: 'application/json',
				  url: "http://live.meribindiya.com/user/register",
				  type:"POST",
				  data:JSON.stringify(registerRequest),
				  dataType:"json",
				  success: function(result){
					  $(".preloader").hide();
					  if(result.status == "success"){
						  user = result.object.user;
						  $("#registerDailog").hide(function(){
							  $("#commonDailog").show();
						  });
					  }else{
						  alert(result.message);
					  }
			  },
			  error:function(result){
				  $(".preloader").hide();
				  alert("Oops! something went wrong, Please try after sometime.")
			  }
			});
			  
			
		});
		
		$("#bookingdate").datepicker({
		        minDate: 0 ,
		        dateFormat: 'dd-mm-yy'
		});

		$("#order-submit").click(function(){
			
			if($("#addressid").val() == ""){
				alert("Please select addess");
				return;
			}
			if($("#bookingdate").val() == ""){
				alert("Please select booking date");
				return;
			}
			if($("#bookingtime").val() == ""){
				alert("Please select booking time");
				return;
			}
			  $(".preloader").show();
			
			orderRequest.addressid = $("#addressid").val();
			orderRequest.booking_date = $("#bookingdate").val();
			orderRequest.booking_time = $("#bookingtime").val();
			orderRequest.utf1 = $("#discount").val();
			orderRequest.mobile = user.mobile;
			orderRequest.pymnt_mode = 0;
			orderRequest.pymnt_source = "cod";
			orderRequest.userid = user.user_id;
			var orderJsonArray=[];
			var j=0;
			map.forEach(function (item, key, mapObj) { 
				
			  		        var service=iterateMap(map,key);
			  		        var orderServiceRequest={};
			  		 		orderServiceRequest.quantity=service.quantity;
					   		orderServiceRequest.serviceid=service.serviceid;
					   		orderJsonArray[j++]=orderServiceRequest;
					   		
			 });
			orderRequest.orderServiceRequests = orderJsonArray;
			
			  $.ajax({
				  contentType: 'application/json',
				  url: "http://live.meribindiya.com/order/create-order",
				  type:"POST",
				  data:JSON.stringify(orderRequest),
				  dataType:"json",
				  success: function(result){
					  $(".preloader").hide();
					  if(result.status == "success"){
						  alert(result.message);
					  }else{
						  alert(result.message);
					  }
					  window.location.reload();
			  },
			  error:function(result){
				  $(".preloader").hide();
				  alert("Oops! something went wrong, Please try after sometime.")
			  }
			});
		});
		
		 function iterateMap(mapReplica,key){
		    	return mapReplica.get(key);
		   }
		$(".adds").click(function() {
			var detailFinder = $(this).parent().siblings(".details-finder");
			var serviceid = detailFinder.attr('data-serviceid');
			var value = $(this).prev().val();
			value = parseInt(value) + 1;
			var service = map.get(serviceid);
			service['quantity'] = value;
			map.set(serviceid,service);
			buildSummary(map);
			$(this).prev().val(value);

		});
		
		buildSummary=function(map){
		    	var content="<table class='table table-light'>";
		    	content+="<tr><td>S. Name</td><td>Duration</td><td>Qty</td><td>Amount</td></tr>";
		    	var finalTotal=0;
		    	map.forEach(function (item, key, mapObj) {  
		    		
		    		var service=map.get(key);
		    		content+="<tr><td>"+service.servicename+"</td><td class='text-center hidden-xs hidden-sm'>"+service.time+"</td><td class='text-center hidden-xs hidden-sm'>"+service.quantity+"</td><td class='text-center hidden-xs hidden-sm'>"+parseInt(service.quantity)*parseInt(service.price)+"</td></tr>";
		    	finalTotal+=parseInt(service.quantity)*parseInt(service.price);
		    	}); 
		    	var fint = "Rs. "+finalTotal+"/- ";
		    	content+="<tr style='font-weight: 700;font-size: 18px;'><td colspan='2'>Final Total</td><td colspan='2' class='text-center hidden-xs hidden-sm'>"+fint+"</td></tr>";
				$("#totalAmount").html(finalTotal);
				if(parseInt(finalTotal)<1){
					$('#totalAmountbutton').prop('disabled', true);
					$('#totalAmountbutton').hide();
				}else{
					$('#totalAmountbutton').prop('disabled', false);
					$('#totalAmountbutton').show();
				}
				$(".cart").html(content);
		    }
		    
		 $("#tabs").tabs();
			$("#accordion").accordion();
	})
</script>