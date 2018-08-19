<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<div class="container-fluid">
	<div class="row">

		<c:forEach var="banner" items="${banners}">
			<div class="col-lg-3">
				<div class="card" style="padding: 3px !important;">
					<div class="card-body">
						<div class="basic-form">
							<img alt="" src="${banner.httppath}" width="100%" height="150px;"><br />
							<a
								style="color: red; text-decoration: underline !important; font-size: 13px; font-weight: 700;"
								href="${pageContext.request.contextPath}/banner/delete/${banner.id}">delete</a>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>

	</div>
</div>
