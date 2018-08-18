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
					<h4>Employee Register</h4>

				</div>
				<div class="card-body">
					<div class="basic-form">
						<sf:form commandName="employee" method="post">
							<div class="form-group">
								<p class="text-muted ">Name</p>
								<sf:input path="name"  cssClass="form-control"/>
							</div>
							<div class="form-group">
								<p class="text-muted ">Email</p>
								<sf:input path="email" cssClass="form-control" />
							</div>
							<div class="form-group">
								<p class="text-muted">Mobile Number</p>
								<sf:input path="mobile"  cssClass="form-control" />
							</div>
							<div class="form-group">
								<p class="text-muted ">Password</p>
								<sf:password path="password" cssClass="form-control" />
							</div>
							<div class="form-group">
								<label> Gender</label>
								<sf:select path="gender" cssClass="form-control">
									<option value="male">Male</option>
									<option value="female">Female</option>
								</sf:select>
							</div>
							<div class="form-group">
								<label> Status</label>
								<sf:select path="status" cssClass="form-control">
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