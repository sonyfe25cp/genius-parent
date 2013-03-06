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
			<h2>PLSA分类显示列表</h2>
		</div>
		<div class="contentbox">
			<table width="100%">
				<thead>
					<tr>
						<th>序号</th>
						<th>类别名称</th>
						<th>文档数</th>
						<th>查看</th>					
					</tr>
				</thead>
				<tbody>
					<c:set var="index" value="1" />
					<c:forEach var="type" items="${types}">
						<tr>
							<td>${index}</td>
							<c:set var="index" value="${index+1}" />

							<td>${type.key}</td>
							<td>${type.value}</td>
							<c:url var="url" value="/admin/plsa/show" >
								<c:param name="type" value="${type.key}"/>
						    </c:url>
							<td><a href="${url}">查看</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<p></p>
			<form action="classify" method="get">
				<input type="submit" class="btn" value="分类" />
			</form>
				
				<!-- <a href="/plsa/classify">分类</a> -->
			
			<ul class="pagination">				
				<c:if test="${pageNo != 1 }">
					<li class="text"><a href="/admin/plsa/list?pageNo=${pageNo-1 }">Previous</a></li>
				</c:if>
				<c:forEach var="num" begin="1" end="${totalP }" step="1">				
					<c:if test="${num == pageNo }">
						<li class="page"><a href="#" title="">${num}</a></li>
					</c:if>
					<c:if test="${num != pageNo }">
						<li><a href="/admin/plsa/list?pageNo=${num }" title="">${num }</a></li>
					</c:if>
				</c:forEach>				
				<c:if test="${pageNo != totalP }">
					<li class="text"><a href="/admin/plsa/list?pageNo=${pageNo+1 }">Next</a></li>
				</c:if>
			</ul>
			<div style="clear: both;"></div>
		</div>
	</div>

</body>
</html>