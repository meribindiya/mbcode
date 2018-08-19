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
					<div class="basic-form">
						<sf:form commandName="request" method="post">
							<div class="form-group">
								<label>Category</label>
								<sf:select path="catid" cssClass="form-control"
									required="required">
									<option value="">Select Category</option>
									<sf:options items="${items}" />
								</sf:select>
							</div>
							<div class="form-group">
								<label>SubCategory</label>
								<sf:select path="subcatid" cssClass="form-control"
									required="required">
									<option value="">Select Subcategory</option>
									<sf:options items="${subitems}" />
								</sf:select>
							</div>
							<div class="form-group">
								<p class="text-muted ">Name</p>
								<sf:input path="name" cssClass="form-control" required="required" />
							</div>
							<div class="form-group">
								<p class="text-muted ">Price</p>
								<sf:input path="price" type="number" cssClass="form-control" required="required" />
							</div>
							<div class="form-group">
								<p class="text-muted ">Min</p>
								<sf:input path="time" type="number" cssClass="form-control" required="required" />
							</div>
							<div class="form-group">
								<p class="text-muted ">Short Desc</p>
								<sf:textarea path="shortdesc" cssStyle="width: 100%" required="required" />
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

<script type="text/javascript">
$(document).ready(function(){
	
	$("#catid").change(function(){
		var id = $(this).val();
		var url = "${pageContext.request.contextPath}/subcategory/bycategory/"+id;
		
		 $.ajax({
	   		  contentType: 'application/json',
	   		  url: url,
	   		  type:"GET",
	   		  dataType:"json",
	   		  success: function(response){
	   			  $("#wapper_id").hide();
	   			  var result = "";
	   			var res="<option value=''>Select Subcategory</option>";  
				  for(var i=0;i<response.length;i++){
						res+="<option  value='"+response[i].id+"' >"+response[i].name+"</option>";
				  }
				  $("#subcatid").html(res);
	   	  },
	   	  error:function(result){
	   		  $("#wapper_id").hide();
	   		  alert("Oops! something went wrong, Please try after sometime.")
	   	  }
	   	});
	})
	
});

</script>