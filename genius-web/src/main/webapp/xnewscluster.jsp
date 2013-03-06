<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>新闻集群${xnewscluster.clusterId}信息</title>
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<link href="/css/genius-admin.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
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
#clusterMembers a{
	color: blue;
}
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body style="background: none">
	<div class="contentcontainer">
		<div class="headings altheading">
			<h1>新闻集群:${xnewscluster.clusterId}</h1>
		</div>
		<div class="contentbox">
			<div id="clusterId">
				<span class="name">clusterId:</span>
				<span class="value">${xnewscluster.clusterId}</span>
			</div>
			<div id="threadhold">
				<span class="name">threadhold:</span>
				<span class="value">${xnewscluster.threadhold}</span>
			</div>
			<div id="minNumber">
				<span class="name">minNumber:</span>
				<span class="value">${xnewscluster.minNumber}</span>
			</div>
			<div id="centerXnews">
				<span class="name">centerXnews:</span>
				<span class="value"><a href="/admin/xnews/showone?id=${xnewscluster.centerXnews.id}" target="_blank">${xnewscluster.centerXnews.title}</a></span>
			</div>
			<div id="clusterMembers">
				<span class="name">clusterMembers(size ${xnewscluster.size}) :</span>
				<span class="value">
					<c:forEach var="member" items="${xnewscluster.clusterMembers}">
						{<a href="/admin/xnews/showone?id=${member.xnews.id}" target="_blank">${member.xnews.id}</a>:${member.sim}}
					</c:forEach>
				</span>
			</div>
		</div>
	</div>
</body>
</html>