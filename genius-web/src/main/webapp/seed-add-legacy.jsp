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
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
</head>
<body style="background: none">

	<div class="contentcontainer">
		<div class="headings altheading">
			<h2>添加/修改种子</h2>
		</div>
		<div class="contentbox">
			<form action="update" method="get">
				<p>
					<label for="textfield"><strong>种子名称</strong> </label> <input
						type="text" id="textfield" class="inputbox" name="name"
						value="${seed.name}" />
				</p>
				<p>
					<label for="textfield"><strong>种子地址</strong> </label>
					<c:if test="${seed.initialUrls!=null}">
						<c:forEach var="url" items="${seed.initialUrls}">
							<input type="text" id="initialUrls" name="urls" class="inputbox" rows="5"
								cols="75" value="${url}">
						</c:forEach>
					</c:if>
					<c:if test="${seed.initialUrls==null}">
						<input type="text" id="initialUrls" name="urls" class="inputbox" />
					</c:if>
				</p>
				<p>
					<label for="smallbox"><strong>抓取深度:</strong> </label> <input
						type="text" id="smallbox" class="inputbox smallbox" name="depth"
						value="${seed.depth}" />
				</p>
				<p>
					<label for="smallbox"><strong>时间间隔:</strong> </label> <input
						type="text" id="smallbox" class="inputbox smallbox" name="refresh"
						value="${seed.refresh}" />
				</p>
				<p>
					<c:if test="${seed.enabled==true}">
						<br />
						<input type="checkbox" name="enabled" checked="checked" />
					启用
				</c:if>
					<c:if test="${seed.enabled==false}">
						<br />
						<input type="checkbox" name="enabled" />
					启用
				</c:if>
				</p>
				<p>
					地址正则<br />
					<c:if test="${seed.interests!=null}">
						<c:forEach var="interest" items="${seed.interests}">
							<input class="inputbox" id="wysiwyg" name="interests" rows="10"
								cols="75" value="${interest.regexp.toString()}">
						</c:forEach>
					</c:if>
					<c:if test="${seed.interests==null}">
						<textarea class="inputbox" id="wysiwyg" name="interests" rows="10" cols="75"></textarea>
					</c:if>
				</p>
				<input type="submit" value="提交" class="btn" />
			</form>
		</div>
	</div>
</body>
</html>