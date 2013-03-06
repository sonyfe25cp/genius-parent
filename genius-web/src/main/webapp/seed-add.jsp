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
	<div class="contentcontainer" id="tabs">
		<div class="headings altheading">
			<h2 class="left">添加种子</h2>
			<ul class="smltabs">
				<li><a href="#tabs-1">新闻</a></li>
				<li><a href="#tabs-2">论坛</a></li>
				<li><a href="#tabs-3">新浪微博</a></li>
				<!-- <li><a href="#tabs-2">腾讯微博</a></li> -->
			</ul>
		</div>
		<div class="contentbox" id="tabs-1">
			<form action="update" method="get">
				<input hidden="true" type="text" id="textfield" class="inputbox"
					name="type" value="NEWS" />
				<p>
					<label for="textfield"><strong>种子名称</strong> </label> <input
						type="text" id="textfield" class="inputbox" name="name"
						value="${seed.name}" />
				</p>
				<p>
					<label for="textfield"><strong>种子地址</strong> </label>
					<c:if test="${seed.initialUrls!=null}">
						<c:forEach var="url" items="${seed.initialUrls}">
							<input type="text" id="initialUrls" name="urls" class="inputbox"
								value="${url}">
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
							<input class="inputbox" id="text" name="interests"
								value="${interest.regexp.toString()}">
						</c:forEach>
					</c:if>
					<c:if test="${seed.interests==null}">
						<input class="inputbox" id="text" name="interests"></input>
					</c:if>
				</p>
				<input type="submit" value="提交" class="btn" />
			</form>
		</div>
		<div class="contentbox" id="tabs-2">
			<form action="update" method="get">
				<input hidden="true" type="text" id="textfield" class="inputbox"
					name="type" value="FORUM" />
				<p>
					<label for="textfield"><strong>种子名称</strong> </label> <input
						type="text" id="textfield" class="inputbox" name="name"
						value="${seed.name}" />
				</p>
				<p>
					<label for="textfield"><strong>种子地址</strong> </label>
					<c:if test="${seed.initialUrls!=null}">
						<c:forEach var="url" items="${seed.initialUrls}">
							<input type="text" id="initialUrls" name="urls" class="inputbox"
								value="${url}">
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
							<input class="inputbox" id="text" name="interests"
								value="${interest.regexp.toString()}">
						</c:forEach>
					</c:if>
					<c:if test="${seed.interests==null}">
						<input class="inputbox" id="text" name="interests"></input>
					</c:if>
				</p>
				<input type="submit" value="提交" class="btn" />
			</form>
		</div>
		<div class="contentbox" id="tabs-3">
			<form action="update" method="get">
				<input hidden="true" type="text" id="textfield" class="inputbox"
					name="type" value="WEIBO" />
				<p>
					<label for="textfield"><strong>种子名称</strong> </label> <input
						type="text" id="textfield" class="inputbox" name="name"
						value="${seed.name}" />
				</p>
				<p hidden="true">
					<label for="textfield"><strong>种子地址</strong> </label> <input
						type="text" id="initialUrls" name="urls" class="inputbox"
						value="http://weibo.com/">
				</p>
				<p>
					<label for="smallbox"><strong>帐号:</strong> </label> <input
						type="text" id="smallbox" class="inputbox smallbox" name="account"
						value="${seed.account}" />
				</p>
				<p>
					<label for="smallbox"><strong>密码:</strong> </label> <input
						type="text" id="smallbox" class="inputbox smallbox"
						name="password" value="${seed.password}" />
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
							<input class="inputbox" id="text" name="interests"
								value="${interest.regexp.toString()}">
						</c:forEach>
					</c:if>
					<c:if test="${seed.interests==null}">
						<input class="inputbox" id="text" name="interests"></input>
					</c:if>
				</p>
				<input type="submit" value="提交" class="btn" />
			</form>
		</div>
	</div>
	<script type="text/javascript" src="/scripts/enhance.js"></script>
	<script type='text/javascript' src='/scripts/excanvas.js'></script>
	<script type='text/javascript' src='/scripts/jquery.min.js'></script>
	<script type='text/javascript' src='/scripts/jquery-ui.min.js'></script>
	<script type='text/javascript' src='/scripts/jquery.wysiwyg.js'></script>
	<script type='text/javascript' src='/scripts/visualize.jQuery.js'></script>
	<script type="text/javascript" src='/scripts/functions.js'></script>
</body>
</html>