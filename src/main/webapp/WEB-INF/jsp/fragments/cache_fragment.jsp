<?xml version="1.0" encoding="ISO-8859-1"?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="l-content"   >

	<div class="container" >

		<section class="row">

			<!--LEFT CONTENT ( MAIN BOX AND FORM HELP ON THE RIGHT )-->
			<div class="l-box-main col-lg-9 col-md-9 col-sm-9">

				<div class="row">

					<!-- MAIN FORM BOX -->
					<div id="l-q-and-b-form" class="l-form-column col-lg-8 " >

						<div class="l-box-form-label"  >
							<h2 id="lbl-form_title">&nbsp;</h2>
						</div>
						
						
					<form:form method="POST" modelAttribute="viewData" id="frm-vehicle-info" role="form" class="form-horizontal l-frm-main">
						
						<div class="row  l-messages  ">
								<div class="col-xs_12" >
									<div  class="validation-summary-errors visually-hidden">
										<span class="error"></span>
									</div>
								</div>
							</div>
							<div class="row l-form-title" >
								<div class="col-xs-12 ">
									<h3 id="lbl-form-title-driver">&nbsp; </h3>
								</div>
							</div>
							
							
							
							
							
							
						
							
							
							
							<div class="row l-form-row toshow" >
								<div class="col-xs-12">
									<label for="year-Make-model" class="" id="lbl-year-Make-model"> Year, Make and Model </label>
								</div>
								<div class="col-xs-12"  >
								<label  class="col-sm-4 control-label ">Vehicle Year</label>
									<div class="row" >
										<div class="col-sm-6"  >
											<form:select path="year" class="form-control" id="vehicle-year"
												placeholder="Select Vehicle Year">
												<option value="">-- Select --</option>
												<c:forEach items="${years}" var="year">
													<form:option value="${year}" label="${year}" />
												</c:forEach>
											</form:select>
										</div>
										<div class="col-sm-1" >
											&nbsp;
										</div>

									</div>
								</div>
								<div class="col-sm-1" >&nbsp; </div>
								<!--- Make -->
								<div class="col-xs-12"  >
								<label for="vehicles0.Make" class="col-sm-4 control-label ">Vehicle Make</label>
									<div class="row" >
										<div class="col-sm-6" >
											<form:select path="make" class="form-control"
												placeholder="Select Vehicle Make" id="vehicle-make">
												<option value="">-- Select --</option>
											</form:select>
										</div>
										<div class="col-sm-1" >
											&nbsp;
										</div>
									</div>
								</div>
								<div class="col-sm-1" >&nbsp; </div>
								<!----  Model --->
								<div class="col-xs-12"  >
								<label for="vehicles0.Model" class="col-sm-4 control-label ">Vehicle Model</label>
									<div class="row" >
										
										<div class="col-sm-6" >
											<form:select path="model" class="form-control"
												placeholder="Select Vehicle Model" id="vehicle-model">
												<option value="">-- Select --</option>
											</form:select>
										</div>
										<div class="col-sm-1" >
											&nbsp;
										</div>

									</div>
								</div>
							
								
								
								<!-- Help  -->
								
								<div class="col-xs-12 visually-hidden">
									<p class="hidden-info" id="help-model">
										&nbsp;
									</p>
								</div>
							</div>
						</form:form>
					</div>
					<!-- END OF MAIN FORM BOX -->
					<!-- MAIN FORM RIGHT INFO -->
					<div id="l-q-and-b-help" class="l-help-column l-col-lg-1 visible-lg visible-md clearfix" >
						<div  id="form-help-info" class="l-help-box clearfix">
							<p ></p>
						</div>
					</div>
					<!-- END OF MAIN FORM RIGHT INFO -->

				</div>

			</div>
			<!-- END LEFT CONTENT ( MAIN BOX AND FORM HELP ON THE RIGHT ) -->
		</section>

	</div>

</div>
<!-- END OF CONTENT -->