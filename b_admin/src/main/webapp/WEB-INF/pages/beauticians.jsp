<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	isELIgnored="false" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Bread crumb -->

<!-- End Bread crumb -->
<!-- Container fluid  -->
<div class="container-fluid">
	<!-- Start Page Content -->
	<div class="row">
		<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
			<div class="card">
				<div class="card-body">
					<h4 class="card-title">Beauticians</h4>
					</br>
					<p>${msg}</p>
					<div >
						<table id="datatable" style="table-layout:fixed" cellspacing="0" width="100%" class="display nowrap table table-hover table-bordered">
							<thead>
								<tr>
									<th>Action</th>
									<th>Assigned Categories</th>
									<th>Name</th>
									<th>Mobile</th>
									<th>Email</th>
									<th>Password</th>
									<th>Gender</th>
									<th>Address</th>
									<th>City</th>
									<th>State</th>
									<th>Created At</th>
									<th>Created By</th>
									<th>Statue</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="beautician" items="${beauticians}">
									<tr>
										<td><a style=" text-decoration: underline !important; "
											href="${pageContext.request.contextPath}/beautician/update/${beautician.id}">Edit</a></td>
										<td>${beautician.categories}</td>
										<td>${beautician.name}</td>
										<td>${beautician.mobile}</td>
										<td>${beautician.email}</td>
										<td>${beautician.password}</td>
										<td>${beautician.gender}</td>
										<td>${beautician.address}</td>
										<td>${beautician.city}</td>
										<td>${beautician.state}</td>
										<td>${beautician.createdByName}</td>
										<td><fmt:formatDate pattern="dd-MMM-yyyy hh:mm a"
												value="${beautician.createdat}" /></td>
										<td>${beautician.status?'Activate':'Deactivate'}</td>
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
<script type="text/javascript">
	$(document).ready(function() {
		$("#datatable").DataTable({
			"ordering" : false,
			"scrollX" : true,
			 "autoWidth": true
		});
	});
</script>
<style type="text/css">
td,th{
white-space: nowrap;
min-width: 150px;
}

</style>