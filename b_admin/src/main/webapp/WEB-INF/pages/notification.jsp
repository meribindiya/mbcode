<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	isELIgnored="false" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link
	href="${pageContext.request.contextPath}/resources/vendors/switchery/dist/switchery.min.css"
	rel="stylesheet">
<div class="container-fluid">
	<div class="row">
		<div class="col-lg-6">
			<div class="card">
				<div class="card-title">
					<h4>${title}</h4>
				</div>
				<div class="card-body">
					<div class="basic-form">
						<c:if test="${not empty msg}">
							<div 
								role="alert">
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">×</span>
								</button>
								${msg}
							</div>
						</c:if>
						<br />
						<sf:form commandName="notificationRequest" method="post"
							enctype="multipart/form-data"
							class="form-horizontal form-label-left">
							<div class="form-group">
								<label class="control-label" for="first-name">Type <span
									class="required">*</span>
								</label>
								<div>
									<sf:select path="type" class="form-control">
										<sf:options items="${typeMap}" />
									</sf:select>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label" for="first-name">Title <span
									class="required">*</span>
								</label>
								<div>
									<sf:input path="title" class="form-control "
										required="required" />
								</div>
							</div>
							<div class="form-group">
								<label class="control-label" for="first-name">Message </label>
								<div>
									<sf:textarea path="message" class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<label class="control-label" for="first-name">Image </label>
								<div>
									<input name="file" id="file" type="file" />
								</div>
							</div>
							<input name='${_csrf.parameterName}' value='${_csrf.token}'
								type="hidden" />
							<div class="ln_solid"></div>
							<div class="form-group">
								<div >
									<button class="btn btn-primary" type="reset">Reset</button>
									<button type="submit" class="btn btn-success">Submit</button>
								</div>
							</div>
						</sf:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script
	src="${pageContext.request.contextPath}/resources/vendors/switchery/dist/switchery.min.js"></script>
