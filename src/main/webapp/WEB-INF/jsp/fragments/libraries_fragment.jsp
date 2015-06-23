<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>   
	<%-- <script src=<%= "\"" + System.getenv().get("STATIC_FILES_URL") + "js/main.prod.js\"" %>></script> --%>
	<script src="js/jquery.min.js"></script>
	<script type="text/javascript" >
		$(document).ready(function(){
			$('#vehicle-year').change(function() {
			  	var year =  $(this).val();
			  	if($.trim(year) != '') {
			  		var contextRoot = "<%=request.getContextPath()%>"
			    	makeService(year,contextRoot);
			  	}else{
			  		$("#vehicle-make option").remove();
			  		$("#vehicle-model option").remove();
			  		$('#vehicle-make').append($('<option>').text('-- Select --').attr('value', ''));
			  		$('#vehicle-model').append($('<option>').text('-- Select --').attr('value', ''));
			  	}
			    
			});
			$('#vehicle-make').change(function() {
			  	var make =  $(this).val();
			  	var year =  $('#vehicle-year').val();
			  	if($.trim(year) != '' && $.trim(make) != '') {
			  		var contextRoot = "<%=request.getContextPath()%>"
			    	modelService(year,make,contextRoot);
			  	}else{
			  		$("#vehicle-model option").remove();
			  		$('#vehicle-model').append($('<option>').text('-- Select --').attr('value', ''));
			  	}
			    
			});
			
		});
		
		function makeService(value,contextRoot) {
			$.ajax({
			    url: contextRoot+"/makeService.json",
			    type: 'POST',
			    dataType: 'json',
			    data: "{\"id\":\"4\",\"method\":\"autofillBasedOnVIN\",\"params\":[\""+ value + "\"],\"jsonrpc\":\"2.0\"}",
			    contentType: 'application/json',
			    mimeType: 'application/json',
			    success: function(data) {
			    	$("#vehicle-make option").remove(); // Remove all the <option> child tags from the select box.
			    	$("#vehicle-model option").remove(); // Remove all the <option> child tags from the select box.
			    	$('#vehicle-make').append($('<option>').text('-- Select --').attr('value', ''));
			    	$('#vehicle-model').append($('<option>').text('-- Select --').attr('value', ''));
				    $.each(data, function(index, item) { //jQuery way of iterating through a collection
				        $('#vehicle-make').append($('<option>').text(data[index]).attr('value', data[index]));
				        });
			    },
			    error:function(data,status,er) {
			        alert("error: "+data+" status: "+status+" er:"+er);
			    }
			});	
		}
		function modelService(year, make, contextRoot) {
			$.ajax({
			    url: contextRoot+"/modelService.json",
			    type: 'POST',
			    dataType: 'json',
			    data: "{\"id\":\"4\",\"method\":\"autofillBasedOnVIN\",\"params\":[\""+year+"\",\""+make+"\"],\"jsonrpc\":\"2.0\"}",
			    contentType: 'application/json',
			    mimeType: 'application/json',
			    success: function(data) {
			    	$("#vehicle-model option").remove(); // Remove all the <option> child tags from the select box.
		    		$('#vehicle-model').append($('<option>').text('-- Select --').attr('value', ''));
		    		$.each(data, function(index, item) { //jQuery way of iterating through a collection
			        	$('#vehicle-model').append($('<option>').text(data[index]).attr('value', data[index]));
			        });
			    },
			    error:function(data,status,er) {
			        alert("error: "+data+" status: "+status+" er:"+er);
			    }
			});	
		}
	</script>
	
