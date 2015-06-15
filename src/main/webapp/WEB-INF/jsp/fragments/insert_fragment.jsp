<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div class="row l-form-row toshow">
	<div class="col-xs-12">
		<label for="year-Make-model" class="" id="lbl-year-Make-model">
			Please select Vehicle Year, Make and Model </label>
	</div>
	<div class="col-xs-12">
		<label for="vehicles0.year" class="col-sm-4 control-label ">Vehicle
			Year</label>
		<div class="row">
			<div class="col-sm-6">
				<form:select path="year" class="form-control" id="vehicle-year"
					placeholder="Select Vehicle Year">
					<option value="">-- Select --</option>
					<c:forEach items="${years}" var="year">
						<form:option value="${year}" label="${year}" />
					</c:forEach>
				</form:select>
			</div>
			<div class="col-sm-1">&nbsp;</div>

		</div>
	</div>
	<div class="col-sm-1">&nbsp;</div>
	<!--- Make -->
	<div class="col-xs-12">
		<label for="vehicles0.Make" class="col-sm-4 control-label ">Vehicle
			Make</label>
		<div class="row">
			<div class="col-sm-6">
				<form:select path="make" class="form-control"
					placeholder="Select Vehicle Make" id="vehicle-make">
					<option value="">-- Select --</option>
				</form:select>
			</div>


			<div class="col-sm-1 align-left">
				<a href="#" class="helpControl  helpControlPadding">Help</a>
			</div>

		</div>
	</div>
	<div class="col-sm-1">&nbsp;</div>
	<!----  Model --->
	<div class="col-xs-12">
		<label for="vehicles0.Model" class="col-sm-4 control-label ">Vehicle
			Model</label>
		<div class="row">

			<div class="col-sm-6">
				<form:select path="model" class="form-control"
					placeholder="Select Vehicle Model" id="vehicle-model">
					<option value="">-- Select --</option>
				</form:select>
			</div>
			<div class="col-sm-1">&nbsp;</div>

		</div>
	</div>



	<!-- Help  -->

	<div class="col-xs-12 visually-hidden">
		<p class="hidden-info" id="help-model">Lorem ipsum dolor sit amet,
			consectetur adipiscing elit. Cras eu dolor finibus, gravida nisl id,
			commodo magna. Vivamus sodales placerat suscipit. Sed eu feugiat
			sapien. Suspendisse aliquam fringilla sem, non pretium justo suscipit
			nec. Cras efficitur mauris ipsum, aliquet bibendum leo fringilla ac.
			Morbi enim ante, hendrerit non semper vitae, feugiat sed est.</p>
	</div>
</div>