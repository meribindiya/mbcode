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
					<h4 class="card-title">Users</h4>
					</br>
					<p>${msg}</p>
					<div >
						<table id="datatable" style="table-layout:fixed" cellspacing="0" width="100%" class="display nowrap table table-hover table-bordered">
							<thead>
								<tr>
									<th>Name</th>
									<th>Mobile No.</th>
									<th>Email</th>
									<th>Registered At</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="user" items="${users}">
									<tr>
										<td>${user.name}</td>
										<td>${user.mobile}</td>
										<td>${user.email}</td>
										<td><fmt:formatDate pattern="dd-MMM-yyyy hh:mm a"
												value="${user.createdat}" /></td>
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
			"autoWidth": true,
			"lengthMenu": [50,100]
			 
		});
	});
</script>
<style type="text/css">
td,th{
white-space: nowrap;
min-width: 150px;
}

</style>