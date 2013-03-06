<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>用户${userinf.username}信息</title>
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<link href="/css/genius-admin.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
.name{
	font-size: 1.2em;
	font-weight: bold;
}
div.contentbox div{
	margin:10px auto;
	padding:10px 15px;
	border:thin;
	border-style: dashed;
}
#collections a,#histories a,#xnewsClusters a{
	color: blue;
}
</style>
</head>
<body style="background: none">
	<div class="contentcontainer">
		<div class="headings altheading">
			<h1>用户:${userinf.username}</h1>
		</div>
		<div class="contentbox">
			<div id="username">
				<span class="name">username:</span>
				<span class="value">${userinf.username}</span>
			</div>
			<div id="password">
				<span class="name">password:</span>
				<span class="value">${userinf.password}</span>
			</div>
			<div id="email">
				<span class="name">email:</span>
				<span class="value">${userinf.email}</span>
			</div>
			<div id="accept">
				<span class="name">accept Email Notification:</span>
				<span class="value">${userinf.acceptEmailNotification}</span>
			</div>
			<div id="seeds">
				<span class="name">seeds:</span>
				<span class="value">
					<c:forEach var="seedname" items="${userinf.seeds}">
						${seedname}
					</c:forEach>
				</span>
			</div>
			<div id="keywords">
				<span class="name">keywords:</span>
				<span class="value">
					<c:forEach var="keyname" items="${userinf.keywords}">
						${keyname}
					</c:forEach>
				</span>
			</div>
			<div id="xnewsClusters">
				<span class="name">xnewsClusters:</span>
				<span class="value">
					<c:forEach var="xnewscluster" items="${userinf.xnewsClusters}">
						<a href="/admin/xnewscluster/showone?clusterId=${xnewscluster.clusterId}">${xnewscluster.clusterId}</a>
					</c:forEach>
				</span>
			</div>
			<div id="collections">
				<span class="name">collections:</span>
				<span class="value">
					<c:forEach var="con" items="${userinf.collections}">
						<a href="/admin/xnews/showone?id=${con.id}">${con.id}</a>
					</c:forEach>
				</span>
			</div>
			<div id="histories">
				<span class="name">histories:</span>
				<span class="value">
					<c:forEach var="his" items="${userinf.histories}">
					<a href="/admin/xnews/showone?id=${his.id}">${his.id}</a>						
					</c:forEach>
				</span>
			</div>
		</div>
	</div>
</body>
</html>