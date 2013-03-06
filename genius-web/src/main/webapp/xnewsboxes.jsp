<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>dlde news boxes</title>
<link rel="stylesheet" type="text/css" href="/css/base.css" />
<link rel="stylesheet" type="text/css" href="/css/boxes.css" />
<script type="text/javascript" src="/js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="/js/boxes.js"></script>
<!--  <base target="_blank" />-->
</head>
<body>
<div id="wapper">
<div id="head">
<ul id="hru">
	<li class="hrl"><a id="user" href="/user/login">
		<c:if test="${username!=null}">
			${username}
		</c:if>
		<c:if test="${username==null}">
		登录
		</c:if>
		</a></li>
	<li class="hrl"><a href="/xnews/list" target="_self">news</a></li>
	<c:if test="${username!=null}">
		<li class="hrl"><a href="/xnews/recommendations">recommendations</a></li>
	</c:if>
</ul>
</div>
<div id="main">
<div id="cc">
<ol id="cco">
	<c:if test="${xnewsboxes!=null}">
	<c:forEach var="res" items="${xnewsboxes}">
		<li class="ccl">		
		<div class="cclc">
		<div class="cclbs">${res.selected}</div>
		<h1><a class="cclha" href="${res.fullUrl}">${res.title}</a></h1>
		<div class="cclde">
		<div class="ccldu"><a class="ccldua"
			href="${res.fullUrl}">${res.url}</a></div>
		<div class="ccldc">${res.content}</div>
		<div class="ccldm">
		<ul>
			<li><span class="ccldmi">${res.id}</span>
			<span class="ccldmti">:ID</span></li>
			<li><span class="ccldma">${res.author}</span>
			<span class="ccldmta">:AUT</span></li>
			<li><span class="ccldmd">${res.publishTime}</span>
			<span class="ccldmtt">:Time</span></li>
		</ul>
		</div>

		</div>
		</div>
		</li>
	</c:forEach>
	</c:if>
</ol>
<div id="foot"></div>
</div>
</div>
</div>
</body>
</html>