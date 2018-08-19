<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	isELIgnored="false" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container-fluid">
	<!-- Start Page Content -->
	<div class="row">
		<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
			<div class="card">
				<div class="card-body">
					<h4 class="card-title">Orders</h4>
					<div>
						<table id="datatable" style="table-layout: fixed" cellspacing="0"
							width="100%"
							class="display nowrap table table-hover table-bordered">
							<thead>
								<tr>
									<th>Action</th>
									<th>Order Id && Order Status</th>
									<th>Custome Name && Customer Mobile</th>
									<th>Custome Address</th>
									<th>Booked At</th>
									<th>Booking date and Time</th>
									<th>Payment Mode</th>
									<th>Price</th>
									<th>Assigned Beautician</th>
									<th>Services</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="co" items="${customerOrders}">
									<tr>
										<td>
											<div class="dropdown">
												<button class="btn btn-primary dropdown-toggle "
													data-id="${co.id}" type="button" data-toggle="dropdown">
													Action</span>
												</button>
												<ul class="dropdown-menu">
													<li><a data-modal="1" class="modal-hit"
														data-oid="${co.id}" href="#/">Assign Or Reassign
															Beautician</a></li>
													<li><a data-modal="2" class="modal-hit"
														data-oid="${co.id}" href="#/">Start Order</a></li>
													<li><a data-modal="3" class="modal-hit"
														data-oid="${co.id}" href="#/">Stop Order</a></li>
													<li><a data-modal="4" class="modal-hit"
														data-oid="${co.id}" href="#/">Rework Order</a></li>
													<li><a data-modal="5" class="modal-hit"
														data-oid="${co.id}" href="#/">Cancel Order</a></li>
												</ul>
											</div>
										</td>
										<td>${co.id}<br /> <label
											style="${co.order_status_code == 10 ?'color:red':'color:#000'}">${co.orderstatus}</label></td>
										
										<td>${co.customername}<br /> ${co.customermobile}</td>
										<td>${co.address}</td>
										<td>${co.bookedat}</td>
										<td>${co.bookingdate}<br /> ${co.booking_time}</td>
										<td>${co.pymnt_source}</td>
										<td>${co.total}</td>
										<td>${co.spname}</td>
										<td>${co.services}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">
	<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-body">
				<div id="container">

					<table class="table">
						<tr>
							<td>Beautician</td>
							<td><select id="beautician" class="form-control"
								style="height: 30px;">
									<option value="">Select Beautician</option>
									<c:forEach var="beautician" items="${beauticians}">
										<option value="${beautician.key}">${beautician.value}</option>
									</c:forEach>
							</select></td>
							<input type="hidden" id="oid" />
							<input type="hidden" id="code" />
						</tr>
						<tr>
							<td colspan="2"><button class="bt btn-success"
									id="assign-beautician">Assign Beautician</button></td>
						</tr>
					</table>

				</div>
			</div>
		</div>

	</div>
</div>

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("#datatable").DataTable({
							"ordering" : false,
							"scrollX" : true,
						});

						$(".modal-hit")
								.click(
										function() {
											var action = $(this).attr(
													"data-modal");
											var oid = $(this).attr("data-oid");
											$(".preloader").show();
											if (parseInt(action) == 2
													|| parseInt(action) == 3
													|| parseInt(action) == 4
													|| parseInt(action) == 5) {
												$.ajax({
															contentType : 'application/json',
															url : "${pageContext.request.contextPath}/orders/action/"
																	+ oid
																	+ "/"
																	+ parseInt(action),
															type : "GET",
															success : function(
																	result) {
																$(".preloader")
																		.hide();
																alert(result);
																window.location
																		.reload();
															},
															error : function(
																	result) {
																$(".preloader")
																		.hide();
																alert("Oops! something went wrong, Please try after sometime.")
															}
														});
											} else {
												$("#oid").val(oid);
												$("#myModal").modal('show');
												$(".preloader").hide();
											}
										});
						$("#assign-beautician")
								.click(
										function() {
											if ($.trim($("#beautician").val()) == "") {
												alert("Please select beautician..");
												return;
											}
											$(".preloader").show();
											$
													.ajax({
														contentType : 'application/json',
														url : "${pageContext.request.contextPath}/orders/action-order-to-sp/"
																+ $("#oid")
																		.val()
																+ "/"
																+ parseInt($(
																		"#beautician")
																		.val()),
														type : "GET",
														success : function(
																result) {
															$(".preloader")
																	.hide();
															alert(result);
															window.location
																	.reload();
														},
														error : function(result) {
															$(".preloader")
																	.hide();
															alert("Oops! something went wrong, Please try after sometime.")
														}
													});

										});
					});
</script>
<style type="text/css">
td, th {
	white-space: nowrap;
	min-width: 150px;
}
</style>