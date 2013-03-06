<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<link href="/css/genius-admin.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
</head>
<body style="background: none">
	<div class="contentcontainer">
		<div class="headings altheading">
		
			<h2>类别:<c:out value="${type}"/></h2>
		</div>
		<div class="contentbox">
			<table width="100%">
				<thead>
					<tr>
						<th>序号</th>
						<th>Id</th>
						<th>标题</th>
					</tr>
				</thead>
				<tbody>
					<c:set var="number" value = "1"/>
					<c:forEach var="result" items="${oneTypeClassificationResults}">
					<tr>
						<td>${number}</td><c:set var="number" value = "${number+1}"/>
						<td><c:out value="${result.id}"/></td>
						<td><c:out value="${result.title}"/></td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			<div style="clear: both;"></div>
		</div>
	</div>
</body>
</html>