<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	isELIgnored="false" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Bread crumb -->
<!-- End Bread crumb -->
<!-- Container fluid  -->
<div class="container-fluid">
	<!-- Start Page Content -->
	<div class="row">
		<div class="col-12">
			<div class="card">
				<div class="card-body">
					<h4 class="card-title">Categories</h4></br>
					<p>${msg}</p>
					<div class="table-responsive">
						<table id="datatable"
							class="display nowrap table table-hover table-striped table-bordered"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Action</th>
									<th>Name</th>
									<th>status</th>
									<th>Created By</th>
									<th>Created at</th>
									<th>Updated By</th>
									<th>Updated at</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="category" items="${categories}">
									<tr>
										<td><a href="${pageContext.request.contextPath}/category/update/${category.id}">Edit</a></td>
										<td>${category.name}</td>
										<td>${category.status?'Activate':'Deactivate'}</td>
										<td>${category.createdByName}</td>
										<td><fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" value="${category.createdat}" /></td>
										<td>${category.updatedByName}</td>
										<td><fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" value="${category.updatedat}" /></td>
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
	$(document).ready(function(){
		$("#datatable").DataTable({
			 "ordering": false
		});
	});
</script>