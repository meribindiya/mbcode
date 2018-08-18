<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<div class="container-fluid">
	<div class="row">
		<div class="col-lg-6">
			<div class="card">
				<div class="card-title">
					<h4>${title}</h4>
				</div>
				<div class="card-body">
					<c:if test="${not empty msg}">
						<b style="color :red;">${msg}</b>
						</c:if>
					<div class="basic-form">
						<sf:form commandName="request" method="post">
							<div class="form-group">
								<p class="text-muted ">Name</p>
								<sf:input path="name" cssClass="form-control" required ="required"/>
							</div>
							<div class="form-group">
								<p class="text-muted ">Email</p>
								<sf:input path="email" cssClass="form-control" type="email" required ="required" />
							</div>
							<div class="form-group">
								<p class="text-muted ">Mobile</p>
								<sf:input path="mobile" cssClass="form-control"  required ="required"/>
							</div>
							<div class="form-group">
								<p class="text-muted ">Password</p>
								<sf:password path="password" cssClass="form-control" required ="required" />
							</div>
							<div class="form-group">
								<label>Assign Category</label>
								<sf:select path="catIds" cssClass="form-control" cssStyle="height: 100px" required ="required"
									multiple="multiple">
									<sf:options items="${items}" />
								</sf:select>
							</div>
							<div class="form-group">
								<label>Gender</label>
								<sf:select path="gender" cssClass="form-control" required ="required">
									<option  value="female">Female</option>
									<option value="male">Male</option>
								</sf:select>
							</div>
							<div class="form-group">
								<p class="text-muted ">Address</p>
								<sf:input path="address" cssClass="form-control" required ="required" />
							</div>
							<div class="form-group">
								<p class="text-muted ">City</p>
								<sf:input path="city" cssClass="form-control" required ="required" />
							</div>
							<div class="form-group">
								<p class="text-muted ">State</p>
								<sf:input path="state" cssClass="form-control"  required ="required" />
							</div>
							<div class="form-group">
								<label> Status</label>
								<sf:select path="status" cssClass="form-control" required ="required">
									<option value="1">Activate</option>
									<option value="0">Deactivate</option>
								</sf:select>
							</div>
							<sf:hidden path="id" />
							<div class="form-group">
								<button type="submit">Submit</button>
							</div>
						</sf:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>