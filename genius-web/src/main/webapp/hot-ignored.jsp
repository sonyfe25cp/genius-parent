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
			<h2>屏蔽热点词显示列表</h2>
		</div>
		<div class="contentbox">
			<c:if test="${!empty terms }">
			<form action="/admin/hot/ignore_delete" method="get">
				<table align="left" cellspacing="0" cellpadding="0" width="100%">
					
					<c:set var="idx" value="0" />
					
					<c:forEach var="t" items="${terms}">		
						<c:if test="${!empty t }">
						<tr>
						<c:forEach var="y" items="${t}">
						<c:if test="${!empty y }">
							<c:set var="idx" value="${i+1}" />
							<td width="15%">${y}</td>
							<td width="5%"><input type="checkbox" value=“${y}“ name="checkbox"
									id="Item${idx}" /></td>
									</c:if>
						</c:forEach>
						</tr>
						</c:if>
					</c:forEach>
				</table>
				<div class="extrabottom">
					<input type="submit" value="取消屏蔽" />
				</div>
			</form>
			</c:if>
			<c:if test="${empty terms }">
			<h2><a href="/admin/hot/list">请添加屏蔽热点词</a></h2>
			</c:if>

			<ul class="pagination">
				<c:if test="${pageNo != 1 }">
					<li class="text"><a href="/admin/hot/ignore_show?pageNo=${pageNo-1 }">Previous</a>
					</li>
				</c:if>
				<c:forEach var="num" begin="${pb}" end="${pe}" step="1">
					<c:if test="${num == pageNo }">
						<li class="page"><a href="#" title="">${num}</a>
						</li>
					</c:if>
					<c:if test="${num != pageNo }">
						<li><a href="/admin/hot/ignore_show?pageNo=${num }" title="">${num
								}</a>
						</li>
					</c:if>
				</c:forEach>
				<c:if test="${(pageNo != totalP)&&totalP!=0 }">
					<!-- syl -->
					<li class="text"><a href="/admin/hot/ignore_show?pageNo=${pageNo+1 }">Next</a>
					</li>
				</c:if>
			</ul>
		</div>
	</div>

</body>
</html>