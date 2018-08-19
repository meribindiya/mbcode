<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	isELIgnored="false" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Bread crumb -->
<div class="row page-titles">
	<div class="col-md-5 align-self-center">
		<h3 class="text-primary">Dashboard</h3>
	</div>
	<div class="col-md-7 align-self-center">
		<ol class="breadcrumb">
			<li class="breadcrumb-item"><a href="javascript:void(0)">Home</a></li>
			<li class="breadcrumb-item active">Dashboard</li>
		</ol>
	</div>
</div>
<!-- End Bread crumb -->
<!-- Container fluid  -->
<div class="container-fluid">
	<!-- Start Page Content -->
	<div class="row">
		<div class="col-12">
			<div class="card">
				<div class="card-body">
					<h4 class="card-title">Employee All</h4>
					<div class="table-responsive">
						<table id="datatable"
							class="display nowrap table table-hover table-striped table-bordered"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>Name</th>
									<th>Email</th>
									<th>Gender</th>
									<th>Created at</th>
									<th>Created By</th>
									<th>Status</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="employee" items="${employees}">
									<tr>
										<td>${employee.name}</td>
										<td>${employee.email}</td>
										<td>${employee.gender}</td>
										<td><fmt:formatDate pattern="dd-MMM-yyyy" value="${employee.createdat}" /></td>
										<td>${employee.createdByName}</td>
										<td>${employee.status?'Activate':'Deactivate'}</td>
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