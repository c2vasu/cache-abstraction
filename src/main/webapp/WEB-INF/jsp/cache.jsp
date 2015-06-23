<?xml version="1.0" encoding="ISO-8859-1"?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"     pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en-GB">
<%-- comment --%>
<%-- HEADER INCLUDE--%>
<%@include file="fragments/head_fragment.jsp" %>
<%-- /HEADER INCLUDE--%>

	<body data-controller="vehicle-page" data-action="render" >
		<%-- HEADER --%>
		<jsp:include page="fragments/header_fragment.jsp" />
		<%-- /HEADER --%>
		
		<%-- CONTENT --%>			
		<jsp:include page="fragments/cache_fragment.jsp"/>
		<%-- /CONTENT --%>
			
		<%-- FOOTER --%>
		<%@include file="fragments/footer_fragment.jsp" %>
		<%-- /FOOTER --%>
				
		<%-- JAVASCRIPT LIBRARIES --%>
		<%@include file="fragments/libraries_fragment.jsp" %>
		<%-- /JAVASCRIPT LIBRARIES --%>

	</body>

</html>