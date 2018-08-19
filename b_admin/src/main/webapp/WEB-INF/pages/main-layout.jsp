<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" type="image/png" sizes="16x16"
	href="images/favicon.png">
<title><tiles:getAsString name="title" /></title>
<!-- Bootstrap Core CSS -->
<link
	href="${pageContext.request.contextPath}/resources/css/lib/bootstrap/bootstrap.min.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/helper.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/style.css"
	rel="stylesheet">
<link
	href="https://cdn.datatables.net/1.10.18/css/dataTables.jqueryui.min.css"
	rel="stylesheet">
	
	

<script
	src="${pageContext.request.contextPath}/resources/js/lib/jquery/jquery.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/lib/bootstrap/js/popper.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/lib/bootstrap/js/bootstrap.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/jquery.slimscroll.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/sidebarmenu.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/lib/sticky-kit-master/dist/sticky-kit.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/scripts.js"></script>

<script
	src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>

</head>

<body class="fix-header fix-sidebar">
	<div class="preloader"
		style="width: 100%; height: 100%; top: 0; position: fixed; z-index: 99999; background: #000 !important; opacity: 0.8 !important;">
		<svg class="circular" viewBox="25 25 50 50">
			<circle class="path" cx="50" cy="50" r="20" fill="none"
				stroke-width="2" stroke-miterlimit="10" /> </svg>
	</div>
	<div id="main-wrapper">
		<div class="row page-titles">
			<div class="col-md-5 align-self-center">
				<h3 class="text-primary">Dashboard</h3>
			</div>
			<div class="col-md-7 align-self-center">
				<a
					style="float: right; font-weight: 600; color: #000; text-decoration: underline !important;"
					href="${pageContext.request.contextPath}/logout">logout</a>
			</div>
		</div>
		<div class="header">
			<tiles:insertAttribute name="menu" />
		</div>
		<div class="left-sidebar">
			<div class="scroll-sidebar">
				<tiles:insertAttribute name="header" />
			</div>
		</div>
		<div class="page-wrapper">
			<tiles:insertAttribute name="body" />

			<!-- End footer -->
		</div>
		<!-- End Page wrapper  -->
	</div>

</body>

</html>