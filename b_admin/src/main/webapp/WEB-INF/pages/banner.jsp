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
						${msg}
					</c:if>
					<br />
					<div class="basic-form">
						<sf:form commandName="bannerRequest" method="post"
							enctype="multipart/form-data"
							class="form-horizontal form-label-left">
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="first-name">Image <span class="required">*</span>
								</label>

								<div >
									<input name="files" id="files" type="file" multiple="multiple"
										required="required" class="col-md-7 col-xs-12" />
								</div>
								<div class="col-md-12 col-sm-12 col-xs-12">
									<span
										style="color: red; font-size: 12px; font-weight: bold;">(Press
										Ctrl for multiple image selection.)</span>
								</div>
							</div>
							<input name='${_csrf.parameterName}' value='${_csrf.token}'
								type="hidden" />
							<div class="ln_solid"></div>
							<div class="form-group">
								<div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
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