<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html>
<head>	
<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
<link href="css/bingo.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="js/jquery-1.7.1.js"></script>
<script  type="text/javascript" src="js/rs.js"></script>
<script language="javascript" type="text/javascript" src="js/atuocomplete.js"></script>
</head>
<body onload="autocomplete()">
  <div id="container">
	<header>
		<input type="hidden" id="qid" value="${qid}" />
		
		  <div id="logo" style="float:left;"> <a href="/"><img src="../images/bingo.png"></img></a> </div>
		  <div id="searchForm" style="float:left;" >
		    
			<form action="/g">
				<input id="query" type="text" name="q" class="keyword" value="${query}"/>
				 <input type="submit" value="Bingo" class="btn" />
			</form>
			<div id="ajaxresult"></div>
		  </div>
		
	</header>
	</div>
	<div id="seperator">
		<span class="lab">搜索</span> <span class="time"> <!--共${unit.totalNum}条结果,-->用时${unit.time}毫秒</span>
		<div class="line"></div>
	</div>
	<!--
	<nav>
		<div id="sort_source">
			<ul>
				<li class="clicked">全部</li>
				<li>华尔街</li>
				<li>新浪财经</li>
				<li>网易财经</li>
				<li>QQ财经</li>
			</ul>
		</div>
		<div id="sort_time">
			<ul>
				<li class="clicked">时间不限</li>
				<li>一小时内</li>
				<li>一天内</li>
				<li>一周内</li>
				<li>一月内</li>
				<li>一年内</li>
			</ul>
		</div>
	</nav>
	-->

	<div id="body_right">
		<div id="results">
			<table width="60%">
				<c:if test="${unit!=null}">
					<c:forEach var="res" items="${unit.resultsList}">
						<tr>
							<td>
								<h3>
									<a href="${res.link}" target="_blank">${res.title}</a>
								</h3> <font size=-1>${res.body}<br> <span>${res.link}</span> <span>更新时间:
										${res.time}</span> </font></td>
						</tr>
						<!--<li class="gift">
							<div class="title">
								<a href="${res.link}" target="_blank">${res.title}</a>
							</div> <a class="link" href="${res.link}" target="_blank">${res.link}</a>
							<div class="time">更新时间: ${res.time}</div>
							<p />
							<div class="abstract">${res.body}</div>
						</li>-->
					</c:forEach>
				</c:if>
			</table>
		</div>
		<c:if test="${tn!=0}">
			<div class="pagination">
				<table>
					<tbody>
						<tr>
							<c:if test="${pageNo!=1}">
								<td><a href="/g?q=${query}&pageNo=${pageNo-1}">前一页</a>
								</td>
							</c:if>
							<c:forEach var="num" begin="${pb}" end="${en}" step="1">
								<%-- 							   <c:when test="${num==pageNo}"> --%>
								<%-- 							      <td>${num}</td> --%>
								<%-- 							   </c:when> --%>
								<%-- 							   <c:otherwise> --%>
								<td><a href="/g?q=${query}&pageNo=${num}">${num}</a>
								</td>
								<%-- 							   </c:otherwise> --%>
							</c:forEach>
							<c:if test="${pageNo!=tp}">
								<td><a href="/g?q=${query}&pageNo=${pageNo+1}">后一页</a>
								</td>
							</c:if>
						</tr>
					</tbody>
				</table>
			</div>
		</c:if>
	</div>
  <div id='space'></div>
  <div id="rs"></div>
<footer></footer>
	
	<input type="hidden" id="relatedSearch" value="${relatedSearch}" />
</body>
</html>