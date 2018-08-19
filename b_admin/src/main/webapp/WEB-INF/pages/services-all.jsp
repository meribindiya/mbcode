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
					<h4 class="card-title">Services</h4>
					</br>
					<p>${msg}</p>
					<div >
						<table id="datatable" cellspacing="0" width="100%" class="display nowrap table table-hover table-bordered">
							<thead>
								<tr>
									<th>Action</th>
									<th>Name</th>
									<th>Category Name</th>
									<th>Subcategory Name</th>
									<th>Price</th>
									<th>Time</th>
									<th>Short Desc</th>
									<th>status</th>
									<th>Created By</th>
									<th>Created at</th>
									<th>Updated By</th>
									<th>Updated at</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="service" items="${services}">
									<tr>
										<td><a style=" text-decoration: underline !important; "
											href="${pageContext.request.contextPath}/service/update/${service.id}">Edit</a></td>
										<td>${service.name}</td>
										<td>${service.categoryName}</td>
										<td>${service.subcategoryName}</td>
										<td>${service.price}</td>
										<td>${service.time}</td>
										<td>${service.shortdesc}</td>
										<td>${service.status?'Activate':'Deactivate'}</td>
										<td>${service.createdByName}</td>
										<td><fmt:formatDate pattern="dd-MMM-yyyy hh:mm a"
												value="${service.createdat}" /></td>
										<td>${service.updatedByName}</td>
										<td><fmt:formatDate pattern="dd-MMM-yyyy hh:mm a"
												value="${service.updatedat}" /></td>
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