<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	isELIgnored="false" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="preloader"></div>
<div id="main-wrapper">

	<div class="unix-login">
		<div class="container-fluid">
			<div class="row justify-content-center">
				<div class="col-lg-4">
					<div class="login-content card">
						<c:if test="${not empty error}">
						<b style="color :red;">Invalid username and password.</b>
						</c:if>
						<div class="login-form">
							<h4>Login</h4>
							<form method="post">
								<div class="form-group">
									<label>Email</label> <input name="username"
										class="form-control" placeholder="Email">
								</div>
								<div class="form-group">
									<label>Password</label> <input type="password" name="password"
										class="form-control" placeholder="Password">
								</div>
								<button type="submit"
									class="btn btn-primary btn-flat m-b-30 m-t-30">Sign in</button>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>