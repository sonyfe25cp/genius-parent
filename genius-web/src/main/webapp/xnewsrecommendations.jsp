<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>dlde news recommendations</title>
<link rel="stylesheet" type="text/css" href="/css/base.css" />
<link rel="stylesheet" type="text/css" href="/css/news.css" />
<script type="text/javascript" src="/js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="/js/news.js"></script>
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
		<li class="hrl"><a href="/xnews/boxes" >boxes</a></li>
	</c:if>
</ul>
</div>
<div id="main">
<div id="cc">
<ol id="cco">
	<c:if test="${recommendations!=null}">
		<c:forEach var="res" items="${recommendations}">
		<li class="ccl">
		<div class="cclbs"></div>
		<div class="cclc">
		<h1><a class="cclha" href="${res.fullUrl}">${res.title}</a></h1>
		<div class="cclde">
		<div class="ccldu"><a class="ccldua"
			href="${res.fullUrl}">${res.url}</a></div>
		<div class="ccldm"><span class="ccldmti">ID:</span> <span
			class="ccldmi">${res.id}</span> <span
			class="ccldmta">Author:</span> <span class="ccldma">${res.author}</span> <span class="ccldmtt">Time:</span> <span
			class="ccldmd">${res.publishTime}</span></div>
		<div class="ccldc">${res.content}</div>
		</div>
		</div>
		</li>
		</c:forEach>
	</c:if>
</ol>
<ul class="pagination">
	<li class="text">
		<c:if test="${pageNo!=1}">
			<a href="/xnews/recommendations?pageNo=${pageNo-1}&pageSize=10">Previous</a>
		</c:if>
	</li>
	<c:forEach   var="num"   begin="${pb}"   end="${pe}"   step="1">   
		<c:choose>
			<c:when test="${num==pageNo}">
				<li class="page"><span>${num}</span></li>
			</c:when>
		<c:otherwise>
			<li><a href="/xnews/recommendations?pageNo=${num}&pageSize=10" title="">${num}</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	<li class="text">
		<c:if test="${pageNo!=totalP}">
			<a href="/xnews/recommendations?pageNo=${pageNo+1}&pageSize=10" title="">Next</a>
		</c:if>
	</li>
</ul>
<div id="foot"></div>
</div>
</div>
</div>
</body>
</html>
