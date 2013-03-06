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
			<h2>资源显示列表</h2>
		</div>
		<div class="contentbox">
			<table width="100%">
				<thead>
					<tr>
						<th>序号</th>
						<th>文件标题</th>
						<th>抓取时间</th>
						<th>操作</th>
						<th><input name="" type="checkbox" value="" id="checkboxall" /></th>
					</tr>
				</thead>
				<tbody>
					<!--<c:set var="index" value="1" />-->
					<c:set var="index" value="${(pageNo-1)*10+1}" />
					<c:forEach var="news" items="${list}">
						<tr>
							<%-- <td>${news.id.toStringMongod()}</td> --%>
							<td>${index}</td>
							<c:set var="index" value="${index+1}" />

							<td><a href="${news.url}" target="_blank">${news.title}</a></td>
							<td>${news.publishTime}</td>
							<td>
								<a href=""><img src="/img/icons/icon_edit.png"	alt="编辑" /></a>
								<a href="" title=""><img src="/img/icons/icon_unapprove.png" alt="禁用" /></a>
								<a href="" title=""><img src="/img/icons/icon_delete.png" alt="删除" /></a>
							</td>
							<td><input type="checkbox" value="" name="checkall" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="extrabottom">
				<ul>
					<li><img src="/img/icons/icon_edit.png" alt="Edit" /> 编辑</li>
					<li><img src="/img/icons/icon_approve.png" alt="Approve" />
						启用</li>
					<li><img src="/img/icons/icon_unapprove.png" alt="Unapprove" />
						停用</li>
					<li><img src="/img/icons/icon_delete.png" alt="Delete" /> 删除</li>
				</ul>
			</div>
			<ul class="pagination">
				<li class="text">
					<c:if test="${pageNo!=1}">
						<a href="/admin/resource/list?pageNo=${pageNo-1}">Previous</a>
					</c:if>
				</li>
				
				<c:forEach   var="num"   begin="${pb}"   end="${pe}"   step="1">   
					<c:choose>
						<c:when test="${num==pageNo}">
							<li class="page"><a href="#" title="">${num}</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="/admin/resource/list?pageNo=${num}" title="">${num}</a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<li class="text">
					<c:if test="${pageNo!=totalP}">
						<a href="/admin/resource/list?pageNo=${pageNo+1}" title="">Next</a></li>
					</c:if>
			</ul>
			<div style="clear: both;"></div>
		</div>
	</div>
</body>
</html>